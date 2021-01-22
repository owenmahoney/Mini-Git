from unittest import TestCase
from framework import AssemblyTest, print_coverage


class TestAbs(TestCase):

    def test_minus_one(self):
        t = AssemblyTest(self, "abs.s")
        t.input_scalar("a0", -1)
        t.call("abs")
        t.check_scalar("a0", 1)
        t.execute()

    def test_zero(self):
        t = AssemblyTest(self, "abs.s")
        # load 0 into register a0
        t.input_scalar("a0", 0)
        # call the abs function
        t.call("abs")
        # check that after calling abs, a0 is equal to 0 (abs(0) = 0)
        t.check_scalar("a0", 0)
        # generate the `assembly/TestAbs_test_zero.s` file and run it through venus
        t.execute()

    def test_one(self):
        # same as test_zero, but with input 1
        t = AssemblyTest(self, "abs.s")
        t.input_scalar("a0", 1)
        t.call("abs")
        t.check_scalar("a0", 1)
        t.execute()

    @classmethod
    def tearDownClass(cls):
        print_coverage("abs.s", verbose=False)


class TestRelu(TestCase):
    def test_simple(self):
        t = AssemblyTest(self, "relu.s")
        # create an array in the data section
        array0 = t.array([1, -2, 3, -4, 5, -6, 7, -8, 9])
        # load address of `array0` into register a0
        t.input_array("a0", array0)
        # set a1 to the length of our array
        t.input_scalar("a1", len(array0))
        # call the relu function
        t.call("relu")
        # check that the array0 was changed appropriately
        t.check_array(array0, [1, 0, 3, 0, 5, 0, 7, 0, 9])
        # generate the `assembly/TestRelu_test_simple.s` file and run it through venus
        t.execute()
    def test_simple2(self):
        t = AssemblyTest(self, "relu.s")
        # create an array in the data section
        array0 = t.array([])
        # load address of `array0` into register a0
        t.input_array("a0", array0)
        # set a1 to the length of our array
        t.input_scalar("a1", len(array0))
        # call the relu function
        t.call("relu")
        # check that the array0 was changed appropriately
        # generate the `assembly/TestRelu_test_simple.s` file and run it through venus
        t.execute(code=78)

    @classmethod
    def tearDownClass(cls):
        print_coverage("relu.s", verbose=False)


class TestArgmax(TestCase):
    def test_simple(self):
        t = AssemblyTest(self, "argmax.s")
        # create an array in the data section
        # TODO
        # load address of the array into register a0
        array0 = t.array([1, 2, 3, -4, 5, -6, 7, -8, 9])
        t.input_array("a0", array0)
        # TODO
        # set a1 to the length of the array
        t.input_scalar("a1", len(array0))
        # TODO
        # call the `argmax` function
        t.call("argmax")
        # TODO
        # check that the register a0 contains the correct output
        t.check_scalar("a0", 8)
        # TODO
        # generate the `assembly/TestArgmax_test_simple.s` file and run it through venus
        t.execute()
    def test_simple2(self):
        t = AssemblyTest(self, "argmax.s")
        # create an array in the data section
        # TODO
        # load address of the array into register a0
        array0 = t.array([])
        t.input_array("a0", array0)
        # TODO
        # set a1 to the length of the array
        t.input_scalar("a1", len(array0))
        # TODO
        # call the `argmax` function
        t.call("argmax")
        # TODO
        # check that the register a0 contains the correct output
        t.execute(code=77)
        # TODO
        # generate the `assembly/TestArgmax_test_simple.s` file and run it through venus
      

    @classmethod
    def tearDownClass(cls):
        print_coverage("argmax.s", verbose=False)


class TestDot(TestCase):
    def test_simple(self):
        t = AssemblyTest(self, "dot.s")
        # create arrays in the data section
        array0 = t.array([1, 2, 3, 4, 5, 6, 7, 8, 9])
        t.input_array("a0", array0)
        array1 = t.array([1, 2, 3, 4, 5, 6, 7, 8, 9])
        t.input_array("a1", array1)
        t.input_scalar("a2", len(array0))
        t.input_scalar("a3", 1)
        t.input_scalar("a4", 1)
        t.call("dot")
        t.check_scalar("a0", 285)
        # TODO
        # load array addresses into argument registers
        # TODO
        # load array attributes into argument registers
        # TODO
        # call the `dot` function
        # check the return value
        # TODO
        t.execute()
    def test_simple2(self):
        t = AssemblyTest(self, "dot.s")
        # create arrays in the data section
        array0 = t.array([1, 2, 3, 4, 5, 6, 7, 8, 9])
        t.input_array("a0", array0)
        array1 = t.array([1, 2, 3, 4, 5, 6, 7, 8, 9])
        t.input_array("a1", array1)
        t.input_scalar("a2", len(array0))
        t.input_scalar("a3", 0)
        t.input_scalar("a4", 1)
        t.call("dot")
        # TODO
        # load array addresses into argument registers
        # TODO
        # load array attributes into argument registers
        # TODO
        # call the `dot` function
        # check the return value
        # TODO
        t.execute(code=76)
    def test_simple3(self):
        t = AssemblyTest(self, "dot.s")
        # create arrays in the data section
        array0 = t.array([1, 2, 3, 4])
        t.input_array("a0", array0)
        array1 = t.array([1, 2, 3, 4])
        t.input_array("a1", array1)
        t.input_scalar("a2", 0)
        t.input_scalar("a3", 1)
        t.input_scalar("a4", 1)
        t.call("dot")
        # TODO
        # load array addresses into argument registers
        # TODO
        # load array attributes into argument registers
        # TODO
        # call the `dot` function
        # check the return value
        # TODO
        t.execute(code=75)
        # generate the `assembly/TestArgmax_test_simple.s` file and run it through venus
    def test_stride(self):
        t = AssemblyTest(self, "dot.s")
        # create arrays in the data section 
        array0 = t.array([1, 2, 3, 4])
        #[2, 4, 6, 8]
        #[1, 2, 3, 4]
        t.input_array("a0", array0)
        array1 = t.array([1, 2, 3, 4])
        t.input_array("a1", array1)
        t.input_scalar("a2", 2)
        t.input_scalar("a3", 1)
        t.input_scalar("a4", 2)
        t.call("dot")
        t.check_scalar("a0", 7)
        # TODO
        # load array addresses into argument registers
        # TODO
        # load array attributes into argument registers
        # TODO
        # call the `dot` function
        # check the return value
        # TODO
        t.execute()

    @classmethod
    def tearDownClass(cls):
        print_coverage("dot.s", verbose=False)


class TestMatmul(TestCase):

    def do_matmul(self, m0, m0_rows, m0_cols, m1, m1_rows, m1_cols, result, code=0):
        t = AssemblyTest(self, "matmul.s")
        # we need to include (aka import) the dot.s file since it is used by matmul.s
        t.include("dot.s")

        # create arrays for the arguments and to store the result
        array0 = t.array(m0)
        array1 = t.array(m1)
        array_out = t.array([0] * len(result))

        # load address of input matrices and set their dimensions
        # TODO
        t.input_array("a0", array0)
        t.input_array("a3", array1)

        t.input_scalar("a2", m0_cols)
        t.input_scalar("a1", m0_rows)

        t.input_scalar("a4", m1_rows)
        t.input_scalar("a5", m1_cols)
        t.input_array("a6", array_out)
        # load address of output array
        # TODO

        # call the matmul function
        t.call("matmul")

        # check the content of the output array
        # TODO
        t.check_array(array_out, result)
        # generate the assembly file and run it through venus, we expect the simulation to exit with code `code`
        t.execute()

    def do_matmul_m0_bad_dims(self, m0, m0_rows, m0_cols, m1, m1_rows, m1_cols, code = 0):
        t = AssemblyTest(self, "matmul.s")
        # we need to include (aka import) the dot.s file since it is used by matmul.s
        t.include("dot.s")

        # create arrays for the arguments and to store the result
        array0 = t.array(m0)
        array1 = t.array(m1)
        
        # load address of input matrices and set their dimensions
        # TODO
        t.input_array("a0", array0)
        t.input_array("a3", array1)

        t.input_scalar("a2", m0_cols)
        t.input_scalar("a1", m0_rows)

        t.input_scalar("a4", m1_rows)
        t.input_scalar("a5", m1_cols)
        # load address of output array
        # TODO

        # call the matmul function
        t.call("matmul")

        # generate the assembly file and run it through venus, we expect the simulation to exit with code `code`
        t.execute(code = 72)

    def do_matmul_m1_bad_dims(self, m0, m0_rows, m0_cols, m1, m1_rows, m1_cols, code = 0):
        t = AssemblyTest(self, "matmul.s")
        # we need to include (aka import) the dot.s file since it is used by matmul.s
        t.include("dot.s")

        # create arrays for the arguments and to store the result
        array0 = t.array(m0)
        array1 = t.array(m1)
        
        # load address of input matrices and set their dimensions
        # TODO
        t.input_array("a0", array0)
        t.input_array("a3", array1)

        t.input_scalar("a2", m0_cols)
        t.input_scalar("a1", m0_rows)

        t.input_scalar("a4", m1_rows)
        t.input_scalar("a5", m1_cols)
        # load address of output array
        # TODO

        # call the matmul function
        t.call("matmul")

        # generate the assembly file and run it through venus, we expect the simulation to exit with code `code`
        t.execute(code = 73)

    def do_matmul_bad_dims(self, m0, m0_rows, m0_cols, m1, m1_rows, m1_cols, code = 0):
        t = AssemblyTest(self, "matmul.s")
        # we need to include (aka import) the dot.s file since it is used by matmul.s
        t.include("dot.s")

        # create arrays for the arguments and to store the result
        array0 = t.array(m0)
        array1 = t.array(m1)
        
        # load address of input matrices and set their dimensions
        # TODO
        t.input_array("a0", array0)
        t.input_array("a3", array1)

        t.input_scalar("a2", m0_cols)
        t.input_scalar("a1", m0_rows)

        t.input_scalar("a4", m1_rows)
        t.input_scalar("a5", m1_cols)
        # load address of output array
        # TODO

        # call the matmul function
        t.call("matmul")

        # generate the assembly file and run it through venus, we expect the simulation to exit with code `code`
        t.execute(code = 74)

    def test_simple(self):
        self.do_matmul(
            [1, 2, 3, 4, 5, 6, 7, 8, 9], 3, 3,
            [1, 2, 3, 4, 5, 6, 7, 8, 9], 3, 3,
            [30, 36, 42, 66, 81, 96, 102, 126, 150]
        )

    def test_simple2(self):

        self.do_matmul_m0_bad_dims(
            [1, 2, 3, 4], -2, 2,
            [1, 2, 3, 4], 2, 2
        )

    def test_simple3(self):

        self.do_matmul_m1_bad_dims(
             [1, 2, 3, 4, 5, 6, 7, 8, 9], 3, 3,
             [1, 2, 3, 4, 5, 6, 7, 8, 9], -3, 3
         )
    def test_simple4(self):

        self.do_matmul_bad_dims(
             [1, 2, 3, 4], 2, 2,
             [1, 2, 3, 4, 5, 6], 3, 2
         )
       

    @classmethod
    def tearDownClass(cls):
        print_coverage("matmul.s", verbose=False)


class TestReadMatrix(TestCase):

    def do_read_matrix(self, fail='', code=0):
        t = AssemblyTest(self, "read_matrix.s")
        # load address to the name of the input file into register a0
        t.input_read_filename("a0", "inputs/test_read_matrix/test_input.bin")

        # allocate space to hold the rows and cols output parameters
        rows = t.array([3])
        cols = t.array([3])

        # load the addresses to the output parameters into the argument registers
        # raise NotImplementedError("TODO")
        t.input_array("a1", rows)
        t.input_array("a2", cols)
        # TODO

        # call the read_matrix function
        t.call("read_matrix")

        # check the output from the function
        # 
        t.check_array(rows, [3])
        t.check_array(cols, [3])
        t.check_array_pointer("a0", [1,2,3,4,5,6,7,8,9])

        # generate assembly and run it through venus
        #t.execute(fail=fail, code=0)
        t.execute()

    def do_read_matrix_mallocfail(self, fail='malloc', code=0):
        t = AssemblyTest(self, "read_matrix.s")
        # load address to the name of the input file into register a0
        t.input_read_filename("a0", "inputs/test_read_matrix/test_input.bin")

        # allocate space to hold the rows and cols output parameters
        rows = t.array([3])
        cols = t.array([3])

        # load the addresses to the output parameters into the argument registers
        # raise NotImplementedError("TODO")
        t.input_array("a1", rows)
        t.input_array("a2", cols)
        # TODO

        # call the read_matrix function
        t.call("read_matrix")

        # check the output from the function
        # 
        t.check_array(rows, [3])
        t.check_array(cols, [3])
        t.check_array_pointer("a0", [1,2,3,4,5,6,7,8,9])

        # generate assembly and run it through venus
        t.execute(fail=fail, code=88)

    def do_read_matrix_fopenfail(self, fail='fopen', code=0):
        t = AssemblyTest(self, "read_matrix.s")
        # load address to the name of the input file into register a0
        t.input_read_filename("a0", "inputs/test_read_matrix/test_input.bin")

        # allocate space to hold the rows and cols output parameters
        rows = t.array([3])
        cols = t.array([3])

        # load the addresses to the output parameters into the argument registers
        # raise NotImplementedError("TODO")
        t.input_array("a1", rows)
        t.input_array("a2", cols)
        # TODO

        # call the read_matrix function
        t.call("read_matrix")

        # check the output from the function
        # 
        t.check_array(rows, [3])
        t.check_array(cols, [3])
        t.check_array_pointer("a0", [1,2,3,4,5,6,7,8,9])

        # generate assembly and run it through venus
        t.execute(fail=fail, code=90)
    
    def do_read_matrix_fclosefail(self, fail='fclose', code=0):
        t = AssemblyTest(self, "read_matrix.s")
        # load address to the name of the input file into register a0
        t.input_read_filename("a0", "inputs/test_read_matrix/test_input.bin")

        # allocate space to hold the rows and cols output parameters
        rows = t.array([3])
        cols = t.array([3])

        # load the addresses to the output parameters into the argument registers
        # raise NotImplementedError("TODO")
        t.input_array("a1", rows)
        t.input_array("a2", cols)
        # TODO

        # call the read_matrix function
        t.call("read_matrix")

        # check the output from the function
        # 
        t.check_array(rows, [3])
        t.check_array(cols, [3])
        t.check_array_pointer("a0", [1,2,3,4,5,6,7,8,9])

        # generate assembly and run it through venus
        t.execute(fail=fail, code=92)
    
    def do_read_matrix_freadfail(self, fail='fread', code=0):
        t = AssemblyTest(self, "read_matrix.s")
        # load address to the name of the input file into register a0
        t.input_read_filename("a0", "inputs/test_read_matrix/test_input.bin")

        # allocate space to hold the rows and cols output parameters
        rows = t.array([3])
        cols = t.array([3])

        # load the addresses to the output parameters into the argument registers
        # raise NotImplementedError("TODO")
        t.input_array("a1", rows)
        t.input_array("a2", cols)
        # TODO

        # call the read_matrix function
        t.call("read_matrix")

        # check the output from the function
        # 
        t.check_array(rows, [3])
        t.check_array(cols, [3])
        t.check_array_pointer("a0", [1,2,3,4,5,6,7,8,9])

        # generate assembly and run it through venus
        t.execute(fail=fail, code=91)
    

    def test_simple(self):
        self.do_read_matrix()
    
    def test_simple_mallocfail(self):
        self.do_read_matrix_mallocfail()

    def test_simple_fopenfail(self):
        self.do_read_matrix_fopenfail()
    
    def test_simple_fclosefail(self):
        self.do_read_matrix_fclosefail()

    def test_simple_freadfail(self):
        self.do_read_matrix_freadfail()

    @classmethod
    def tearDownClass(cls):
        print_coverage("read_matrix.s", verbose=False)


class TestWriteMatrix(TestCase):

    def do_write_matrix(self, fail='', code=0):
        t = AssemblyTest(self, "write_matrix.s")
        outfile = "outputs/test_write_matrix/student.bin"
        # load output file name into a0 register
        t.input_write_filename("a0", outfile)
        # load input array and other arguments
        t.input_array("a1", t.array([1,2,3,4,5,6,7,8,9]))
        t.input_scalar("a2", 3)
        t.input_scalar("a3", 3)
        # call `write_matrix` function
        t.call("write_matrix")
        # generate assembly and run it through venus
        t.execute()
        # compare the output file against the reference
        t.check_file_output(outfile, "outputs/test_write_matrix/reference.bin")
    
    def do_write_matrix_fopenfail(self, fail='fopen', code=0):
        t = AssemblyTest(self, "write_matrix.s")
        outfile = "outputs/test_write_matrix/student.bin"
        # load output file name into a0 register
        t.input_write_filename("a0", outfile)
        # load input array and other arguments
        t.input_array("a1", t.array([1,2,3,4,5,6,7,8,9]))
        t.input_scalar("a2", 3)
        t.input_scalar("a3", 3)
        # call `write_matrix` function
        t.call("write_matrix")
        # generate assembly and run it through venus
        t.execute(fail=fail, code=93)
        # compare the output file against the reference
        #t.check_file_output(outfile, "outputs/test_write_matrix/reference.bin")
    
    def do_write_matrix_fwritefail(self, fail='fwrite', code=0):
        t = AssemblyTest(self, "write_matrix.s")
        outfile = "outputs/test_write_matrix/student.bin"
        # load output file name into a0 register
        t.input_write_filename("a0", outfile)
        # load input array and other arguments
        t.input_array("a1", t.array([1,2,3,4,5,6,7,8,9]))
        t.input_scalar("a2", 3)
        t.input_scalar("a3", 3)
        # call `write_matrix` function
        t.call("write_matrix")
        # generate assembly and run it through venus
        t.execute(fail=fail, code=94)
        # compare the output file against the reference
        #t.check_file_output(outfile, "outputs/test_write_matrix/reference.bin")
    
    def do_write_matrix_fclosefail(self, fail='fclose', code=0):
        t = AssemblyTest(self, "write_matrix.s")
        outfile = "outputs/test_write_matrix/student.bin"
        # load output file name into a0 register
        t.input_write_filename("a0", outfile)
        # load input array and other arguments
        t.input_array("a1", t.array([1,2,3,4,5,6,7,8,9]))
        t.input_scalar("a2", 3)
        t.input_scalar("a3", 3)
        # call `write_matrix` function
        t.call("write_matrix")
        # generate assembly and run it through venus
        t.execute(fail=fail, code=95)
        # compare the output file against the reference
        #t.check_file_output(outfile, "outputs/test_write_matrix/reference.bin")

    def test_simple(self):
        self.do_write_matrix()
    
    def test_simple2(self):
        self.do_write_matrix_fopenfail()
    
    def test_simple3(self):
        self.do_write_matrix_fwritefail()
    
    def test_simple4(self):
        self.do_write_matrix_fclosefail()

    @classmethod
    def tearDownClass(cls):
        print_coverage("write_matrix.s", verbose=False)


class TestClassify(TestCase):

    def make_test(self):
        t = AssemblyTest(self, "classify.s")
        t.include("argmax.s")
        t.include("dot.s")
        t.include("matmul.s")
        t.include("read_matrix.s")
        t.include("relu.s")
        t.include("write_matrix.s")
        return t

    def test_simple0_input0(self):
        t = self.make_test()
        out_file = "outputs/test_basic_main/student0.bin"
        ref_file = "outputs/test_basic_main/reference0.bin"
        args = ["inputs/simple0/bin/m0.bin", "inputs/simple0/bin/m1.bin",
                "inputs/simple0/bin/inputs/input0.bin", out_file]
        
        # call classify function
        t.call("classify")
        # generate assembly and pass program arguments directly to venus
        t.execute(args=args)

        # compare the output file and
        t.check_stdout("2")
        # TODO
        # compare the classification output with `check_stdout`
    
    def test_simple1_input0(self):
        t = self.make_test()
        out_file = "outputs/test_basic_main/student1.bin"
        ref_file = "outputs/test_basic_main/reference1.bin"
        args = ["inputs/simple1/bin/m0.bin", "inputs/simple1/bin/m1.bin",
                "inputs/simple1/bin/inputs/input0.bin", out_file]
         
        t.input_scalar("a2", 1)
        
        # call classify function
        t.call("classify")
        # generate assembly and pass program arguments directly to venus
        t.execute(args=args)

        # compare the output file and
        #t.check_file_output()
        # TODO
        # compare the classification output with `check_stdout`
        t.check_stdout("")
    
    def test_simple2_input0(self):
        t = self.make_test()
        out_file = "outputs/test_basic_main/student2.bin"
        ref_file = "outputs/test_basic_main/reference2.bin"
        args = ["inputs/simple2/bin/m0.bin", "inputs/simple2/bin/m1.bin",
                "inputs/simple2/bin/inputs/input0.bin", out_file]
        
        t.input_scalar("a2", 0)
        
        # call classify function
        t.call("classify")
        # generate assembly and pass program arguments directly to venus
        t.execute(args=args)

        # compare the output file and
        t.check_file_output("outputs/test_basic_main/student2.bin", "outputs/test_basic_main/reference2.bin")
        # TODOgood 
        # compare the classification output with `check_stdout`
        t.check_stdout("7")

    @classmethod
    def tearDownClass(cls):
        print_coverage("classify.s", verbose=False)


class TestMain(TestCase):

    def run_main(self, inputs, output_id, label):
        args = [f"{inputs}/m0.bin", f"{inputs}/m1.bin", f"{inputs}/inputs/input0.bin",
                f"outputs/test_basic_main/student{output_id}.bin"]
        reference = f"outputs/test_basic_main/reference{output_id}.bin"
        t = AssemblyTest(self, "main.s", no_utils=True)
        t.call("main")
        t.execute(args=args, verbose=False)
        t.check_stdout(label)
        t.check_file_output(args[-1], reference)

    def test0(self):
        self.run_main("inputs/simple0/bin", "0", "2")

    def test1(self):
        self.run_main("inputs/simple1/bin", "1", "1")
    
    def test2(self):
        self.run_main("inputs/simple2/bin","2", "7")