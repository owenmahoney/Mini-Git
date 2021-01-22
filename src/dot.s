.globl dot

.text
# =======================================================
# FUNCTION: Dot product of 2 int vectors
# Arguments:
#   a0 (int*) is the pointer to the start of v0
#   a1 (int*) is the pointer to the start of v1
#   a2 (int)  is the length of the vectors
#   a3 (int)  is the stride of v0
#   a4 (int)  is the stride of v1
# Returns:
#   a0 (int)  is the dot product of v0 and v1
# Exceptions:
# - If the length of the vector is less than 1,
#   this function terminates the program with error code 75.
# - If the stride of either vector is less than 1,
#   this function terminates the program with error code 76.
# =======================================================
dot:
    # Prologue

    li t1, 1
    li t5, 1 #initialize loop counter
    li t6, 4
   
    blt a2, t1, size_violation #trigger a size violation
    blt a3, t1, stride_violation
    blt a4, t1, stride_violation

    addi sp, sp, -16
    sw s0, 0(sp) 
    sw s1, 4(sp)
    sw s2, 8(sp)
    sw s3, 12(sp)
    li, s0, 0 #initialize the running sum = 0
    
    mul s2, t6, a3
    mul s3, t6, a4
    addi a2, a2, 1
    
loop_start:

    beq t5, a2, loop_end
    lw t2, 0(a0) # place the ith element in t2 and t3
    lw t3, 0(a1)

    mul t4, t2, t3 #multiply the vector elements together and store in t4
    add s0, s0, t4 #add t4 to the running sum

    add a0, a0, s2 #increment the pointers
    add a1, a1, s3
    addi t5, t5, 1

    j loop_start

loop_end:
# Epilogue 

    mv a0, s0
    lw s0, 0(sp)
    lw s1, 4(sp)
    lw s2, 8(sp)
    lw s3, 12(sp)
    addi sp, sp, 16

    ret

size_violation:
    li a1, 75
    jal exit2

stride_violation:
    li a1, 76
    jal exit2