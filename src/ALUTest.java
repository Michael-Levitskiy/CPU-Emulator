import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

public class ALUTest {
    /**
     * Tests the add2() method
     */
    @Test
    void testAdd2() {
        ALU alu = new ALU();
        Word word1 = new Word();
        Word word2 = new Word();

        word1.set(0);
        word2.set(0);
        assertEquals(0, alu.add2(word1, word2).getSigned());

        word1.set(0xffffffff);
        word2.set(0xffffffff);
        assertEquals(0xfffffffe, alu.add2(word1, word2).getSigned());

        word1.set(0xffffffff);
        word2.set(0);
        assertEquals(0xffffffff, alu.add2(word1, word2).getSigned());

        word1.set(0xabd38271);
        word2.set(0x7862bbac);
        assertEquals(0x24363e1d, alu.add2(word1, word2).getSigned());

        word1.set(0x78634fad);
        word2.set(0xffba7283);
        assertEquals(0x781dc230, alu.add2(word1, word2).getSigned());
    }

    /**
     * Tests the add4 method
     */
    @Test
    void testAdd4() {
        ALU alu = new ALU();
        Word word1 = new Word();
        Word word2 = new Word();
        Word word3 = new Word();
        Word word4 = new Word();

        word1.set(0);
        word2.set(0);
        word3.set(0);
        word4.set(0);
        assertEquals(0, alu.add4(word1, word2, word3, word4).getSigned());

        word1.set(0xffffffff);
        word2.set(0xffffffff);
        word3.set(0xffffffff);
        word4.set(0xffffffff);
        assertEquals(0xfffffffc, alu.add4(word1, word2, word3, word4).getSigned());

        word1.set(0xffffffff);
        word2.set(0xffffffff);
        word3.set(0xffffffff);
        word4.set(0);
        assertEquals(0xfffffffd, alu.add4(word1, word2, word3, word4).getSigned());

        word1.set(0x683924ff);
        word2.set(0x8713657b);
        word3.set(0xbface789);
        word4.set(0x881baddf);
        assertEquals(0x37151fe2, alu.add4(word1, word2, word3, word4).getSigned());

        word1.set(0xda671238);
        word2.set(0x7980d33a);
        word3.set(0x8900bdac);
        word4.set(0x12356874);
        assertEquals(0xef1e0b92, alu.add4(word1, word2, word3, word4).getSigned());

        word1.set(0x12837321);
        word2.set(0xabddcd23);
        word3.set(0x12448bcd);
        word4.set(0x18274bdc);
        assertEquals(0xe8cd17ed, alu.add4(word1, word2, word3, word4).getSigned());
    }

    /**
     * Tests the doOperation() method
     */
    @Test
    void testDoOperation() {
        ALU alu = new ALU();
        Word word1 = new Word();
        Word word2 = new Word();
        Bit[] bits = {new Bit(), new Bit(), new Bit(), new Bit()};

        /**
         * op1 = 0, op2 = 0
         */
        word1.set(0);
        word2.set(0);
        alu.op1.copy(word1);
        alu.op2.copy(word2);
        bits[0].set(false);
        bits[1].set(false);
        bits[2].set(false);
        bits[3].set(true);

        alu.doOperation(bits);  // perform AND
        assertEquals(0, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // perform OR
        assertEquals(0, alu.result.getSigned());

        bits[0].toggle();
        bits[1].toggle();
        alu.doOperation(bits);  // perform XOR
        assertEquals(0, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // perform NOT on op1
        assertEquals(0xffffffff, alu.result.getSigned());

        bits[0].toggle();
        bits[1].toggle();
        bits[2].toggle();
        alu.doOperation(bits);  // left shift op1 by 5 LO bits in op2
        assertEquals(0, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // right shift op1 by 5 LO bits in op2
        assertEquals(0, alu.result.getSigned());

        bits[0].toggle();
        bits[1].toggle();
        alu.doOperation(bits);  // add op1 and op2
        assertEquals(0, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // subtract op1 and op2
        assertEquals(0, alu.result.getSigned());

        bits[3].toggle();
        alu.doOperation(bits);  // multiply op1 and op2
        assertEquals(0, alu.result.getSigned());


        /**
         * op1 = 0xffffffff, op2 = 0xffffffff
         */
        word1.set(0xffffffff);
        word2.set(0xffffffff);
        alu.op1.copy(word1);
        alu.op2.copy(word2);
        bits[0].set(false);
        bits[1].set(false);
        bits[2].set(false);
        bits[3].set(true);

        alu.doOperation(bits);  // perform AND
        assertEquals(0xffffffff, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // perform OR
        assertEquals(0xffffffff, alu.result.getSigned());

        bits[0].toggle();
        bits[1].toggle();
        alu.doOperation(bits);  // perform XOR
        assertEquals(0, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // perform NOT on op1
        assertEquals(0, alu.result.getSigned());

        bits[0].toggle();
        bits[1].toggle();
        bits[2].toggle();
        alu.doOperation(bits);  // left shift op1 by 5 LO bits in op2
        assertEquals(0x80000000, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // right shift op1 by 5 LO bits in op2
        assertEquals(0x1, alu.result.getSigned());

        bits[0].toggle();
        bits[1].toggle();
        alu.doOperation(bits);  // add op1 and op2
        assertEquals(0xfffffffe, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // subtract op1 and op2
        assertEquals(0, alu.result.getSigned());

        bits[3].toggle();
        alu.doOperation(bits);  // multiply op1 and op2
        assertEquals(0x1, alu.result.getSigned());


        /**
         * op1 = 0x12345678, op2 =0xfedcba98 
         */
        word1.set(0x12345678);
        word2.set(0xfedcba98);
        alu.op1.copy(word1);
        alu.op2.copy(word2);
        bits[0].set(false);
        bits[1].set(false);
        bits[2].set(false);
        bits[3].set(true);

        alu.doOperation(bits);  // perform AND
        assertEquals(0x12141218, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // perform OR
        assertEquals(0xfefcfef8, alu.result.getSigned());

        bits[0].toggle();
        bits[1].toggle();
        alu.doOperation(bits);  // perform XOR
        assertEquals(0xece8ece0, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // perform NOT on op1
        assertEquals(0xedcba987, alu.result.getSigned());

        bits[0].toggle();
        bits[1].toggle();
        bits[2].toggle();
        alu.doOperation(bits);  // left shift op1 by 5 LO bits in op2
        assertEquals(0x78000000, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // right shift op1 by 5 LO bits in op2
        assertEquals(0x12, alu.result.getSigned());

        bits[0].toggle();
        bits[1].toggle();
        alu.doOperation(bits);  // add op1 and op2
        assertEquals(0x11111110, alu.result.getSigned());

        bits[0].toggle();
        alu.doOperation(bits);  // subtract op1 and op2
        assertEquals(0x13579be0, alu.result.getSigned());

        bits[3].toggle();
        alu.doOperation(bits);  // multiply op1 and op2
        assertEquals(0x35068740, alu.result.getSigned());
    }
}
