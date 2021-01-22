.globl classify

.text
classify:
    # =====================================
    # COMMAND LINE ARGUMENTS
    # =====================================
    # Args:
    #   a0 (int)    argc
    #   a1 (char**) argv
    #   a2 (int)    print_classification, if this is zero, 
    #               you should print the classification. Otherwise,
    #               this function should not print ANYTHING.
    # Returns:
    #   a0 (int)    Classification
    # Exceptions:
    # - If there are an incorrect number of command line args,
    #   this function terminates the program with exit code 89.
    # - If malloc fails, this function terminats the program with exit code 88.
    #
    # Usage:
    #   main.s <M0_PATH> <M1_PATH> <INPUT_PATH> <OUTPUT_PATH>


	# =====================================
    # LOAD MATRICES
    # =====================================

    addi sp, sp, -56
    sw s0, 0(sp) #store a1 pointer 
    sw s1, 4(sp) #store a2 pointer 
    sw s2, 8(sp) # malloced m0 
    sw s3, 12(sp) # rows of m0
    sw s4, 16(sp) # cols of m0 /gets overwriten to hold the last d in the last mat mul call 
    sw s5, 20(sp) #malloced m1 
    sw s6, 24(sp) # rows of m1
    sw s7, 28(sp) # cols of m1
    sw s8, 32(sp)  #malloced input
    sw s9, 36(sp) #rows of input / gets overwritten after args max to the return value 
    sw s10, 40(sp) #cols of input 
    sw s11, 44(sp) # pointer to malloced m0 * input 
    sw ra, 48(sp) 
    sw a2, 52(sp)

    mv s0, a1
    lw s1, 52(sp)

    li t0, 5
    bne a0, t0, invalid_num_args # check for correct arguments

    # Load pretrained m0
    lw a0, 4(s0) # got the filename for m0
    addi a1, sp, 12
    addi a2, sp, 16
    jal read_matrix
    mv s2, a0 #store it for later

    # Load pretrained m1
    lw a0, 8(s0) #got the filename for m1 
    addi a1, sp, 24
    addi a2, sp, 28
    jal read_matrix
    mv s5, a0 #store it for later

    # Load input matrix
    lw a0, 12(s0)
    addi a1, sp, 36
    addi a2, sp, 40
    jal read_matrix
    mv s8, a0 #store it for later

    # =====================================
    # RUN LAYERS
    # =====================================
    # 1. LINEAR LAYER:    m0 * input
    # 2. NONLINEAR LAYER: ReLU(m0 * input)
    # 3. LINEAR LAYER:    m1 * ReLU(m0 * input)

    #setting up matmul m0 * input
    
    lw a1, 12(sp)
    lw a5, 40(sp)
    li t0, 4 
    mul t1, a1, a5
    mul a0, t0, t1
    jal malloc
    beq a0, x0, invalid_malloc 
    mv s11, a0

    mv a0, s2
    lw a1, 12(sp)
    lw a2, 16(sp)
    mv a3, s8
    lw a4, 36(sp)
    lw a5, 40(sp)
    mv a6, s11 #assume this also modifies s11
    jal matmul

    #calling ReLU
    mv a0, s11
    lw t0, 12(sp)
    lw t1, 40(sp)
    mul a1, t0, t1
    jal relu #this should modify d at sp 44

    #setting up matmul m1 * s9
    
    lw a1, 24(sp)
    lw a5, 40(sp)
    li t0, 4 
    mul t1, a1, a5
    mul a0, t0, t1
    jal malloc
    beq a0, x0, invalid_malloc 
    mv s4, a0 

    mv a0, s5
    lw a1, 24(sp)
    lw a2, 28(sp)
    mv a3, s11
    lw a4, 12(sp)
    lw a5, 40(sp)
    mv a6, s4 #assume this also modifies s11
    jal matmul



    # =====================================
    # WRITE OUTPUT
    # =====================================
    # Write output matrix

    #setting up the write for result stored in s4
    
    lw a0, 16(s0) #accessing OUTPATH
    mv a1, s4
    lw a2, 24(sp)
    lw a3, 40(sp)
    jal write_matrix

    # =====================================
    # CALCULATE CLASSIFICATION/LABEL
    # =====================================
    # Call argmax

  
    #setting up argmax

    mv a0, s4
    lw t0, 24(sp) 
    lw t1, 40(sp)
    mul a1, t0, t1
    jal argmax #a0 is set to the arg max by return

    mv s0, a0

    bne s1, x0, epilogue

    #print classification 
    mv a1, a0 
    jal print_int 

    #print new line 
    li a1, '\n'
    jal print_char 
    
epilogue: 

    mv a0, s2 
    jal free 

    mv a0, s5 
    jal free 

    mv a0, s8
    jal free 

    mv a0, s4 
    jal free 

    mv a0, s11
    jal free 

    mv a0, s0

    li t0, 0
    sw t0, 12(sp)
    sw t0, 16(sp)
    sw t0, 24(sp)
    sw t0, 28(sp)
    sw t0, 36(sp)
    sw t0, 40(sp)

    lw s0, 0(sp) #store a1 pointer 
    lw s1, 4(sp) #store a2 pointer 
    lw s2, 8(sp) # malloced m0 
    lw s3, 12(sp) # rows of m0
    lw s4, 16(sp) # cols of m0
    lw s5, 20(sp) #malloced m1 
    lw s6, 24(sp) # rows of m1
    lw s7, 28(sp) # cols of m1
    lw s8, 32(sp)  #malloced input
    lw s9, 36(sp) #rows of input  
    lw s10, 40(sp) #cols of input 
    lw s11, 44(sp) #matmul m0 * input 
    lw ra, 48(sp) 
    lw a2, 52(sp)
    addi sp, sp, 56

    ret

invalid_num_args:
    li a1, 89
    jal exit2

invalid_malloc:
    li a1, 88
    jal exit2