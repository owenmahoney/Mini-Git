.globl relu

.text
# ==============================================================================
# FUNCTION: Performs an inplace element-wise ReLU on an array of ints
# Arguments:
# 	a0 (int*) is the pointer to the array
#	a1 (int)  is the # of elements in the array
# Returns:
#	None
# Exceptions:
# - If the length of the vector is less than 1,
#   this function terminates the program with error code 78.
# ==============================================================================
relu:
    # Prologue
    
    li t0, 1
    blt a1, t0, exit
    li, t1, 0 #create counter

loop_start:
    
    beq t1, a1, done #if the counter reaches the size of the array, end loop
    lw t2, 0(a0) #store what is in the ith position in t2
    
    blt t2, x0, zeroify
    j next
    
    zeroify:
        sub t2, t2, t2 #subtracts t2 from itself to make it 0
    
    next:

        sw t2, 0(a0) #stores t2 back into the array position
        addi a0, a0, 4 #increment a0 to point to the next element in the array
        addi t1, t1, 1 #add one to the counter
    
        j loop_start

done:
    ret

exit:
    li a1, 78
	jal exit2
    