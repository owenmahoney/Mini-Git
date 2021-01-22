.import ../../src/utils.s
.import ../../src/matmul.s
.import ../../src/dot.s

.data
m0: .word 1 1 1 1
m1: .word 1 1 1 1

.globl main_test
.text
# main_test function for testing
main_test:

    # load address to array m0 into a0
    la a0 m0

    # load address to array m1 into a3
    la a3 m1

    # load 2 into a2
    li a2 2

    # load 2 into a1
    li a1 2

    # load 2 into a4
    li a4 2

    # load 2 into a5
    li a5 2

    # call matmul function
    jal ra matmul
    # we expect matmul to exit early with code 72

    # exit normally
    jal exit
