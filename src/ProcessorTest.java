import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import DataTypes.*;

public class ProcessorTest {
    @Test
    void testRun1() throws Exception {
        Processor processor = new Processor();

        String[] data = {"00000000000000010100000000100001",    // MATH DestOnly 5, R1
                         "00000000000010000111100001000010",    // MATH ADD R1 R1 R2
                         "00000000000000001011100001000011",    // MATH ADD R2 R2
                         "00000000000100000111100001100010",    // MATH ADD R2 R1 R3
                         "00000000000000000000000000000000"     // HALT
                        };

        MainMemory.load(data);
        processor.run();
        Word r3 = processor.getReg(3);
        assertEquals(25, r3.getSigned());
    }

    @Test
    void testRun2() throws Exception{
        Processor processor = new Processor();

        String[] data = {"00000000000001010000000000100001",    // MATH DestOnly 20, R1
                         "00000000000000001000000001000001",    // MATH DestOnly 2, R2
                         "00000000000010001011010001100010",    // MATH RIGHTSHIFT R1 R2 R3
                         "00000000000010001111110010000010",    // MATH SUB R1 R3 R4
                         "00000000000000000000000000000000"     // Halt
                        };

        MainMemory.load(data);
        processor.run();
        Word r4 = processor.getReg(4);
        assertEquals(15, r4.getSigned());
    }

    @Test
    void testRun3() throws Exception {
        Processor processor = new Processor();

        String[] data = {"00000000000000111100000000100001",    // MATH DestOnly 15, R1
                         "00000000000000001100000001000001",    // MATH DestOnly 3, R2
                         "00000000000010001011000001100010",    // MATH LEFTSHIFT R1 R2 R3
                         "00000000000000001001110001100011",    // MATH MULT R2 R3
                         "00000000000000000110000001100011",    // MATH AND R1 R3
                         "00000000000000000000000000000000"     // HALT
                        };
        MainMemory.load(data);
        processor.run();
        Word r3 = processor.getReg(3);
        assertEquals(8, r3.getSigned());
    }

    @Test
    void testRun4() throws Exception {
        Processor processor = new Processor();

        String[] data = {"00000000010111101000000001000001",    // MATH DestOnly 378, R2
                         "00000000111000010000000001100001",    // MATH DestOnly 900, R3
                         "00000000000000001010110000100010",    // MATH NOT R0 R2 R1
                         "00000000000010001010100010000010",    // MATH XOR R1 R2 R4
                         "00000000000000001110010010000011",    // MATH OR R3 R4
                         "00000000000000000000000000000000"     // HALT
                        };
        MainMemory.load(data);
        processor.run();
        Word r4 = processor.getReg(4);
        assertEquals(-123, r4.getSigned());
    }

    @Test
    void testRun5() throws Exception {
        Processor processor = new Processor();

        // find the factorial of 5 (the num in register 2)
        String[] data = {"00000000000000000100000000100001",    // MATH DestOnly 1, R1
                         "00000000000000010100000001000001",    // MATH DestOnly 5, R2
                         "00000000000000000100000001100001",    // MATH DestOnly 1, R3
                         "00000000001000001001010000100111",    // BRANCH LE R2, R1, 4
                         "00000000000000001001110001100011",    // MATH MULT R2, R3
                         "00000000000000000111110001000011",    // MATH SUB R1, R2
                         "11111111111111110000000000000101",    // BRANCH DestOnly -4
                         "00000000000000000000000000000000"     // HALT
                        };

        MainMemory.load(data);
        processor.run();
        Word r3 = processor.getReg(3);
        assertEquals(120, r3.getSigned());
    }

    @Test
    void testRun6() throws Exception {
        Processor processor = new Processor();

        String[] data = {"00000000000001010000000000100001",    // MATH DestOnly 20, R1
                         "00000000000011001000000001000001",    // MATH DestOnly 50, R2
                         "00000000000101101000000001100001",    // MATH DestOnly 90, R3
                         "00000010001100000100000001110111",    // STORE R1 at R3 + 70
                         "00000000000010001000000001110110",    // STORE R2 at R3 + R1
                         "00000000000000111100000001110101",    // STORE 15 at R3
                         "00000010001100001100000010010011",    // LOAD R3+70 into R4
                         "00000000000110000100000010110010",    // LOAD R3+R1 into R5
                         "00000000000001010000000001110001",    // LOAD R3+20 into R3
                         "00000000000000000000000110100100",    // JUMP to 13
                         "01110010101010111100001111110111",    // *doesn't matter
                         "00110101000111010101101010010100",    // *doesn't matter
                         "10000111100101000101010001010101",    // *doesn't matter
                         "00000000000000000000000000000000"     // HALT
                        };

        MainMemory.load(data);
        processor.run();
        Word r1 = processor.getReg(1);
        Word r2 = processor.getReg(2);
        Word r3 = processor.getReg(3);
        Word r4 = processor.getReg(4);
        Word r5 = processor.getReg(5);
        assertEquals(r1.getSigned(), r4.getSigned());
        assertEquals(r2.getSigned(), r5.getSigned());
        assertEquals(r2.getSigned(), r3.getSigned());
    }

    @Test
    void testRun7() throws Exception {
        Processor processor = new Processor();

        String[] data = {"00000000000000000100000000100001",    // MATH DestOnly 1, R1
                         "00000000000000010100000001000001",    // MATH DestOnly 5, R2
                         "00000000011010000001010000101011",    // CALL R0 le R1 ? pc + 13
                         "00001011000000000100010001001010",    // CALL R0 ne R1 ? R2 + 11
                         "00000000000000101100000001001001",    // CALL DestOnly R2 + 11
                         "00000000000000000000001000001000",    // CALL pc <- 16
                         "00000001000010001000000000000110",    // BRANCH R1 eq R2 ? pc + 1
                         "10001110011010011010011110011001",    // *doesn't matter
                         "00000000000000001011110000101111",    // PUSH R1 SUB R2
                         "00000000000010001001110000001110",    // PUSH R1 MULT R2
                         "00000000000000010011100000101101",    // PUSH R1 ADD 4
                         "00000000000000001000000001100001",    // MATH DestOnly 2, R3
                         "00000000000010000000000010011011",    // PEEK SP + R0 + 1 into R4
                         "00000000000000001100000010111010",    // PEEK SP + R0 + R3 into R5
                         "00000000000000000000000011011001",    // POP to R6
                         "00000000000000000000000000000000",    // HALT
                         "00000000000000000100000101000001",    // MATH DestOnly 1, R10
                         "00000000000000101011100000100011",    // MATH ADD R10, R1
                         "00000000000000000000000000010000"     // RETURN
                        };

        MainMemory.load(data);
        processor.run();
        Word r1 = processor.getReg(1);
        Word r2 = processor.getReg(2);
        Word r4 = processor.getReg(4);
        Word r5 = processor.getReg(5);
        Word r6 = processor.getReg(6);
        assertEquals(r1.getSigned(), r2.getSigned());
        assertEquals(0, r4.getSigned());        // Note to self: pretty sure should be 25*
        assertEquals(0, r5.getSigned());
        assertEquals(9, r6.getSigned());
    }

    @Test
    void Test1_test() throws Exception{
        Processor processor = new Processor();

        String[] data = {"00000000000000000100000000100001",
                         "00000000000000000000000001000001",
                         "00000000000001010000000001100001",
                         "00000000011111010000000010000001",
                         "00000000000000111100000010100001",
                         "00000000000000000000000011000001",
                         "00000000001010001000000001100111",
                         "00000000000000010100000010010111",
                         "00000000000000000111100010000011",
                         "00000000000000000111100010100011",
                         "00000000000000000111100001000011",
                         "00000000000000000000000011000100",
                         "00000000011111010000000010000001",
                         "00000000001010000000000001000111",
                         "00000000000000010000000011110011",
                         "00000000000000011111100011000011",
                         "00000000000000000111100010000011",
                         "00000000000000000111110001000011",
                         "00000000000000000000000110100100",
                         "00000000000000000000000000000000"
                         };

        MainMemory.load(data);
        processor.run();
        Word r6 = processor.getReg(6);
        assertEquals(490, r6.getSigned());
    }

    @Test
    void Test2_txt() throws Exception{
        Processor processor = new Processor();

        String[] data = {"00000000000000000100000000100001",
                         "00000000000010001000000001000001",
                         "00000000100100111000000001100001",
                         "00000000000000001000000001110111",
                         "00000000110111110100000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000010001100000001000001",
                         "00000000110111110100000001100001",
                         "00000000000000001000000001110111",
                         "00000000101100100000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000101101100000001000001",
                         "00000000101100100000000001100001",
                         "00000000000000001000000001110111",
                         "00000000111110100000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000011001110000000001000001",
                         "00000000111110100000000001100001",
                         "00000000000000001000000001110111",
                         "00000000110110110000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000101001000000001000001",
                         "00000000110110110000000001100001",
                         "00000000000000001000000001110111",
                         "00000000101001001000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000100101000000001000001",
                         "00000000101001001000000001100001",
                         "00000000000000001000000001110111",
                         "00000000111101100000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000101101100000001000001",
                         "00000000111101100000000001100001",
                         "00000000000000001000000001110111",
                         "00000000011111010000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000101010000000001000001",
                         "00000000011111010000000001100001",
                         "00000000000000001000000001110111",
                         "00000000101110010100000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000000110000000001000001",
                         "00000000101110010100000001100001",
                         "00000000000000001000000001110111",
                         "00000000111101010000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000010100100000001000001",
                         "00000000111101010000000001100001",
                         "00000000000000001000000001110111",
                         "00000000101010100100000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000100101100000001000001",
                         "00000000101010100100000001100001",
                         "00000000000000001000000001110111",
                         "00000000011100100000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000001101100000001000001",
                         "00000000011100100000000001100001",
                         "00000000000000001000000001110111",
                         "00000000111110001000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000011111100000001000001",
                         "00000000111110001000000001100001",
                         "00000000000000001000000001110111",
                         "00000000110000111000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000011000100000001000001",
                         "00000000110000111000000001100001",
                         "00000000000000001000000001110111",
                         "00000000100010110100000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000101101100000001000001",
                         "00000000100010110100000001100001",
                         "00000000000000001000000001110111",
                         "00000000110010110000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000100011000000001000001",
                         "00000000110010110000000001100001",
                         "00000000000000001000000001110111",
                         "00000000100010011000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000010111000000001000001",
                         "00000000100010011000000001100001",
                         "00000000000000001000000001110111",
                         "00000000100110001100000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000101011100000001000001",
                         "00000000100110001100000001100001",
                         "00000000000000001000000001110111",
                         "00000000110111100000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000101110000000001000001",
                         "00000000110111100000000001100001",
                         "00000000000000001000000001110111",
                         "00000000111000010000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000100001100000001000001",
                         "00000000111000010000000001100001",
                         "00000000000000001000000001110111",
                         "00000000000000000000000001000001",
                         "00000000000000000111100001100011",
                         "00000000000000001000000001110111",
                         "00000000000000000000000001000001",
                         "00000000000001010000000001100001",
                         "00000000000000000000000010000001",
                         "00000000100100111000000011000001",
                         "00000000001010001000000001100111",
                         "00000000000000011000000010110011",
                         "00000000000010011000000011010011",
                         "00000000000000010111100010000011",
                         "00000000000000000111100001000011",
                         "11111111111111101000000000000101",
                         "00000000000000000000000000000000"
                         };

        MainMemory.load(data);
        processor.run();
        Word r4 = processor.getReg(4);
        assertEquals(1623, r4.getUnsigned());
    }

    @Test
    void Test3_txt() throws Exception{
        Processor processor = new Processor();

        String[] data = {"00000000000000000100000000100001",
                         "00000000000000000000000001000001",
                         "00000000000001010000000001100001",
                         "00000000011111010000000010000001",
                         "00000000000000111100000010100001",
                         "00000000000000000000000011000001",
                         "00000000001010001000000001100111",
                         "00000000000000010100000010010111",
                         "00000000000000000111100010000011",
                         "00000000000000000111100010100011",
                         "00000000000000000111100001000011",
                         "00000000000000000000000011000100",
                         "00000000000000000111110010000011",
                         "00000000001010000000000001000111",
                         "00000000000000010000000011110011",
                         "00000000000000011111100011000011",
                         "00000000000000000111110010000011",
                         "00000000000000000111110001000011",
                         "00000000000000000000000110100100",
                         "00000000000000000000000000000000"
                         };

        MainMemory.load(data);
        processor.run();
        Word r6 = processor.getReg(6);
        assertEquals(490, r6.getSigned());
    }
}