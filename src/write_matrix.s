.globl write_matrix

.text
# ==============================================================================
# FUNCTION: Writes a matrix of integers into a binary file
# FILE FORMAT:
#   The first 8 bytes of the file will be two 4 byte ints representing the
#   numbers of rows and columns respectively. Every 4 bytes thereafter is an
#   element of the matrix in row-major order.
# Arguments:
#   a0 (char*) is the pointer to string representing the filename
#   a1 (int*)  is the pointer to the start of the matrix in memory
#   a2 (int)   is the number of rows in the matrix
#   a3 (int)   is the number of columns in the matrix
# Returns:
#   None
# Exceptions:
# - If you receive an fopen error or eof,
#   this function terminates the program with error code 93.
# - If you receive an fwrite error or eof,
#   this function terminates the program with error code 94.
# - If you receive an fclose error or eof,
#   this function terminates the program with error code 95.
# ==============================================================================
write_matrix:

    # Prologue

    addi sp, sp, -32
    sw s0, 0(sp) #pointer to the string filename
    sw s1, 4(sp) # pointer to the matrix in memory
    sw a2, 8(sp) # rows of our new matrix
    sw ra, 12(sp)
    sw a3, 16(sp) #cols of new matrix 
    sw s4, 20(sp) #file descriptor returned by fopen 
    sw s5, 24(sp) #size of matrix (rows *cols)
    sw s6, 28(sp) #size of matrix in bytes (4 * size of matrix) 

    #save arguments
    add s0, x0, a0 #save a0
    add s1, x0, a1 #save a1

    #fopen the file 

    mv a1, s0 
    li a2, 1
    jal fopen
    li t0, -1
    beq a0, t0, fopen_failure
    mv s4, a0 #saved file descriptor to s4

    #set up sizes for fwrite 
    li t0, 4 
    addi t0, sp, 8
    addi t1, sp, 16
    lw t2, 0(t0)
    lw t3, 0(t1)
    mul s5, t2, t3 #dimensions of matrix (ints)
    mul s6, s5, t0 #dimensions of matrix (bytes)

    #fwrite for rows
    mv a1, s4
    addi t0, sp, 8
    add a2, x0, t0
    li a3, 1 
    li a4, 4
    jal fwrite 
    li t0, 1
    bne a0, t0, fwrite_error

    #fwrite for cols 
    mv a1, s4
    addi t0, sp, 16
    add a2, x0, t0
    li a3, 1 
    li a4, 4
    jal fwrite 
    li t0, 1
    bne a0, t0, fwrite_error

    #fwrite for matrix 
    mv a1, s4 
    mv a2, s1 
    mv a3, s5
    li a4, 4
    jal fwrite 
    bne a0, s5, fwrite_error

    #fclose 

    mv a1, s4 
    jal fclose 
    li t0, -1
    beq a0, t0, fclose_failure


    # Epilogue

    lw s0, 0(sp) 
    lw s1, 4(sp) 
    lw a2, 8(sp)
    lw ra, 12(sp)
    lw a3, 16(sp)
    lw s4, 20(sp)
    lw s5, 24(sp)
    lw s6, 28(sp)
    addi sp, sp, 32

    ret
    
fopen_failure:
    li a1, 93
    jal exit2

fwrite_error:
    li a1, 94
    jal exit2

fclose_failure:
    li a1, 95
    jal exit2