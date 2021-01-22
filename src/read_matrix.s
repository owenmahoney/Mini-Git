.globl read_matrix

.text
# ==============================================================================
# FUNCTION: Allocates memory and reads in a binary file as a matrix of integers
#
# FILE FORMAT:
#   The first 8 bytes are two 4 byte ints representing the # of rows and columns
#   in the matrix. Every 4 bytes afterwards is an element of the matrix in
#   row-major order.
# Arguments:
#   a0 (char*) is the pointer to string representing the filename
#   a1 (int*)  is a pointer to an integer, we will set it to the number of rows
#   a2 (int*)  is a pointer to an integer, we will set it to the number of columns
# Returns:
#   a0 (int*)  is the pointer to the matrix in memory
# Exceptions:
# - If malloc returns an error,
#   this function terminates the program with error code 88.
# - If you receive an fopen error or eof, 
#   this function terminates the program with error code 90.
# - If you receive an fread error or eof,
#   this function terminates the program with error code 91.
# - If you receive an fclose error or eof,
#   this function terminates the program with error code 92.
# ==============================================================================
read_matrix:

    # Prologue
	
    addi sp, sp, -36
    sw s0, 0(sp) # rows of our matrix
    sw s1, 4(sp) # columns of our new matrix
    sw ra, 8(sp)
    sw s2, 12(sp) 
    sw s3, 16(sp)
    sw s4, 20(sp)
    sw s5, 24(sp)
    sw s6, 28(sp)
    sw s7, 32(sp)
    
    #save arguments
    add s0, x0, a1 #save a1
    add s1, x0, a2 #save a2
    add s2, x0, a0 #save a0

    #open the file
    mv a1, s2
    li a2, 0
    jal fopen
    li t0, -1
    beq a0, t0, fopen_failure

    mv s3, a0 #save file descriptor a0 into s3

    #read in rows
    li t0, 4
    mv a3, t0
    mv a1, s3
    mv a2, s0
    jal fread
    li t0, 4
    bne a0, t0, invalid_num_bytes


    #read in cols
    li t0, 4
    mv a3, t0
    mv a1, s3
    mv a2, s1
    jal fread
    li t0, 4
    bne a0, t0, invalid_num_bytes
  

    
    #setting up malloc call
    lw t0, 0(s0) #loading in the int row and col values to temps
    lw t1, 0(s1)
    mul t3, t0, t1
    li, t4, 4
    mul t3, t3, t4
    mv s5, t3 #saving the number of bytes to read from the file
    mv a0, t3 
    jal malloc
    beq a0, x0, invalid_malloc
    mv s4, a0 #s4 stores the pointer to malloced memory for big matrix
    
    #set up call to fread
    mv a1, s3
    mv a2, s4
    mv a3, s5
    jal fread
    bne a0, s5, invalid_num_bytes

    mv a1, s3
    jal fclose
    li t0, -1
    beq a0, t0, close_failure

    mv a0, s4
    mv a1, s0
    mv a2, s1


    # Epilogue
    lw s0, 0(sp) # rows of our matrix
    lw s1, 4(sp) # columns of our new matrix
    lw ra, 8(sp)
    lw s2, 12(sp)
    lw s3, 16(sp)
    lw s4, 20(sp)
    lw s5, 24(sp)
    lw s6, 28(sp)
    lw s7, 32(sp)
    addi sp, sp, 36

    ret

fopen_failure:
    li a1, 90
    jal exit2

invalid_malloc:
    li a1, 88
    jal exit2

invalid_num_bytes:
    li a1, 91
    jal exit2

close_failure:
    li a1, 92
    jal exit2
