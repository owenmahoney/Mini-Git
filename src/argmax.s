.globl argmax

.text
# =================================================================
# FUNCTION: Given a int vector, return the index of the largest
#	element. If there are multiple, return the one
#	with the smallest index.
# Arguments:
# 	a0 (int*) is the pointer to the start of the vector
#	a1 (int)  is the # of elements in the vector
# Returns:
#	a0 (int)  is the first index of the largest element
# Exceptions:
# - If the length of the vector is less than 1,
#   this function terminates the program with error code 77.
# =================================================================
argmax:

    # Prologue
    
    li t0, 1
    blt a1, t0, exit #checks if length of vector is less than 1 
    li, t1, 0 #create counter
    

    addi sp, sp, -8 
    sw s0, 0(sp) 
    sw s1, 4(sp)
    li, s0, 0 #create index of the largest element
    li, s1, 0 #create the largest element

loop_start:
    
    beq t1, a1, loop_end #if the counter reaches the size of the array, end loop
    lw t3, 0(a0) #store what is in the ith position in t2
    
    blt s1, t3, update #if t3 is greater than the largest element, update
    j next
    
    update:
        sub s1, s1, s1 #subtracts s1 from self to make 0
        sub s0, s0, s0 #subtracts s0 from self to  make 0
        add s1, x0, t3 #makes t3 the largest element 
        add s0, x0, t1 #makes the new index of largest element the count number 
    
    next:

        
        addi a0, a0, 4 #increment a0 to point to the next element in the array
        addi t1, t1, 1 #add one to the counter
    
        j loop_start

loop_end:
    #Epilogue
        
        mv a0, s0 
        lw s0, 0(sp)
        lw s1, 4(sp) 
        addi sp, sp, 8


        ret

exit:
    li a1, 77
	jal exit2