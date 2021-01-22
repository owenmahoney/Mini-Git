.globl matmul

.text
# =======================================================
# FUNCTION: Matrix Multiplication of 2 integer matrices
# 	d = matmul(m0, m1)
# Arguments:
# 	a0 (int*)  is the pointer to the start of m0 
#	a1 (int)   is the # of rows (height) of m0
#	a2 (int)   is the # of columns (width) of m0
#	a3 (int*)  is the pointer to the start of m1
# 	a4 (int)   is the # of rows (height) of m1
#	a5 (int)   is the # of columns (width) of m1
#	a6 (int*)  is the pointer to the the start of d
# Returns:
#	None (void), sets d = matmul(m0, m1)
# Exceptions:
#   Make sure to check in top to bottom order!
#   - If the dimensions of m0 do not make sense,
#     this function terminates the program with exit code 72.
#   - If the dimensions of m1 do not make sense,
#     this function terminates the program with exit code 73.
#   - If the dimensions of m0 and m1 don't match,
#     this function terminates the program with exit code 74.
# =======================================================
matmul:

    # Error checks

    li t0, 1
    blt a1, t0, incorrect_m0_dims
    blt a2, t0, incorrect_m0_dims
    blt a4, t0, incorrect_m1_dims
    blt a5, t0, incorrect_m1_dims
    bne a2, a4, no_dim_match #check that num of columns in m0 = num rows in m1

    # Prologue

    addi sp, sp, -40
    sw s0, 0(sp) 
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)
    sw s4, 16(sp)
    sw s5, 20(sp)
    sw s6, 24(sp)
    sw s7, 28(sp)
    sw s8, 32(sp)
    sw s9, 36(sp)

    mv s0, a0
    mv s1, a1
    mv s2, a2
    mv s3, a3
    mv s4, a4
    mv s5, a5
    mv s6, a6
    mv s9, ra
    
    li s7, 0 #initialize row counter
    li s8, 0 #initialize column counter



outer_loop_start:

    beq s7, s1, outer_loop_end #check if it has iterated through all the rows
    
    j inner_loop_start

    inner_loop_start:

        beq s8, s5, inner_loop_end #check if it has iterated through all the cols for that row 
        
        mv a0, s0
        mv a1, s3
        mv a2, s2
        li a3, 1
        mv a4, s5

        jal dot #sets the new dot product to a0 
        
        sw a0, 0(s6) #stores dot product in the current spot of d
        addi s6, s6, 4 #increment a6/d to point to the next element in the array

        addi s8, s8, 1 #increment col counter 
        addi s3, s3, 4 #move to the next column
        j inner_loop_start

    inner_loop_end:
        
        li t1, -4
        li t4, 0
        sub t2, s5, t4
        mul t3, t1, t2
        add s3, s3, t3 #moving the pointer to the column back to the start 
        li t6, 4
        mul t5, t6, s2
        
        add s0, s0, t5 # increment the row pointer to next row
        addi s7, s7, 1 #increment row counter 
        li s8, 0 #reset col counter to 0
        j outer_loop_start



outer_loop_end:

    mv a6, s6 #may or may not work/ pointer to start of d
    mv ra, s9

    lw s0, 0(sp) 
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    lw s4, 16(sp)
    lw s5, 20(sp)
    lw s6, 24(sp)
    lw s7, 28(sp)
    lw s8, 32(sp)
    lw s9, 36(sp)
    addi sp, sp, 40
    # Epilogue

    ret

incorrect_m0_dims:
    li a1, 72
    jal exit2

incorrect_m1_dims:
    li a1, 73
    jal exit2

no_dim_match:
    li a1, 74
    jal exit2

