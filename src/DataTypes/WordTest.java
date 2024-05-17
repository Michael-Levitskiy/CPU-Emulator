package DataTypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;

public class WordTest {

    /**
     * tests getBit(int) method
     */
    @Test
    void testGetBit() {
        Word word = new Word();
        
        word.set(0x00000000);
        assertFalse(word.getBit(0).getValue());
        assertFalse(word.getBit(14).getValue());
        assertFalse(word.getBit(27).getValue());
        assertFalse(word.getBit(31).getValue());

        word.set(0xffffffff);
        assertTrue(word.getBit(0).getValue());
        assertTrue(word.getBit(15).getValue());
        assertTrue(word.getBit(24).getValue());
        assertTrue(word.getBit(31).getValue());

        word.set(0x123afabb);
        assertTrue(word.getBit(0).getValue());
        assertFalse(word.getBit(10).getValue());
        assertTrue(word.getBit(25).getValue());
        assertFalse(word.getBit(31).getValue());

        word.set(0x984179ff);
        assertTrue(word.getBit(0).getValue());
        assertFalse(word.getBit(17).getValue());
        assertTrue(word.getBit(22).getValue());
        assertTrue(word.getBit(31).getValue());
    }
    
    /**
     * tests setBit(int, Bit) method
     */
    @Test
    void testSetBit() {
        Word word = new Word();
        Bit bit = new Bit(true);

        word.setBit(0, bit);
        assertTrue(word.getBit(0).getValue());
        assertFalse(word.getBit(7).getValue());
        assertFalse(word.getBit(13).getValue());
        assertFalse(word.getBit(20).getValue());
        assertFalse(word.getBit(28).getValue());
        assertFalse(word.getBit(31).getValue());

        word.setBit(7, bit);
        assertTrue(word.getBit(0).getValue());
        assertTrue(word.getBit(7).getValue());
        assertFalse(word.getBit(13).getValue());
        assertFalse(word.getBit(20).getValue());
        assertFalse(word.getBit(28).getValue());
        assertFalse(word.getBit(31).getValue());

        word.setBit(13, bit);
        assertTrue(word.getBit(0).getValue());
        assertTrue(word.getBit(7).getValue());
        assertTrue(word.getBit(13).getValue());
        assertFalse(word.getBit(20).getValue());
        assertFalse(word.getBit(28).getValue());
        assertFalse(word.getBit(31).getValue());

        word.setBit(20, bit);
        assertTrue(word.getBit(0).getValue());
        assertTrue(word.getBit(7).getValue());
        assertTrue(word.getBit(13).getValue());
        assertTrue(word.getBit(20).getValue());
        assertFalse(word.getBit(28).getValue());
        assertFalse(word.getBit(31).getValue());
    }
    
    /**
     * tests and(Word) method
     */
    @Test
    void testAnd() {
        Word word1 = new Word();
        Word word2 = new Word();
        Word actual = new Word();
        Word expected = new Word();

        actual = word1.and(word2);
        expected.set(0x00000000);
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        word2.set(0x00000000);      // == 0b 0000 0000 0000 0000 0000 0000 0000 0000
        actual = word1.and(word2);
        expected.set(0x00000000);   // == 0b 0000 0000 0000 0000 0000 0000 0000 0000
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0x00000000);      // == 0b 0000 0000 0000 0000 0000 0000 0000 0000
        word2.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        actual = word1.and(word2);
        expected.set(0x00000000);   // == 0b 0000 0000 0000 0000 0000 0000 0000 0000
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        word2.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        actual = word1.and(word2);
        expected.set(0xffffffff);   // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xaaaaaaaa);      // == 0b 1010 1010 1010 1010 1010 1010 1010 1010
        word2.set(0x55555555);      // == 0b 0101 0101 0101 0101 0101 0101 0101 0101
        actual = word1.and(word2);
        expected.set(0x00000000);   // == 0b 0000 0000 0000 0000 0000 0000 0000 0000
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xabcdef12);      // == 0b 1010 1011 1100 1101 1110 1111 0001 0010
        word2.set(0x12345678);      // == 0b 0001 0010 0011 0100 0101 0110 0111 1000
        actual = word1.and(word2);
        expected.set(0x02044610);   // == 0b 0000 0010 0000 0100 0100 0110 0001 0000
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0x871625ba);      // == 0b 1000 0111 0001 0110 0010 0101 1011 1010
        word2.set(0xabcc6731);      // == 0b 1010 1011 1100 1100 0110 0111 0011 0001
        actual = word1.and(word2);
        expected.set(0x83042530);   // == 0b 1000 0011 0000 0100 0010 0101 0011 0000
        assertEquals(expected.getSigned(), actual.getSigned());
    }

    /**
     * tests or(Word) method
     */
    @Test
    void testOr() {
        Word word1 = new Word();
        Word word2 = new Word();
        Word actual = new Word();
        Word expected = new Word();

        actual = word1.or(word2);
        expected.set(0x00000000);
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        word2.set(0x00000000);      // == 0b 0000 0000 0000 0000 0000 0000 0000 0000
        actual = word1.or(word2);
        expected.set(0xffffffff);   // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0x00000000);      // == 0b 0000 0000 0000 0000 0000 0000 0000 0000
        word2.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        actual = word1.or(word2);
        expected.set(0xffffffff);   // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        word2.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        actual = word1.or(word2);
        expected.set(0xffffffff);   // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xaaaaaaaa);      // == 0b 1010 1010 1010 1010 1010 1010 1010 1010
        word2.set(0x55555555);      // == 0b 0101 0101 0101 0101 0101 0101 0101 0101
        actual = word1.or(word2);
        expected.set(0xffffffff);   // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xabcdef12);      // == 0b 1010 1011 1100 1101 1110 1111 0001 0010
        word2.set(0x12345678);      // == 0b 0001 0010 0011 0100 0101 0110 0111 1000
        actual = word1.or(word2);
        expected.set(0xbbfdff7a);   // == 0b 1011 1011 1111 1101 1111 1111 0111 1010
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0x871625ba);      // == 0b 1000 0111 0001 0110 0010 0101 1011 1010
        word2.set(0xabcc6731);      // == 0b 1010 1011 1100 1100 0110 0111 0011 0001
        actual = word1.or(word2);
        expected.set(0xafde67bb);   // == 0b 1010 1111 1101 1110 0110 0111 1011 1011
        assertEquals(expected.getSigned(), actual.getSigned());
    }

    /**
     * tests xor(Word) method
     */
    @Test
    void testXor() {
        Word word1 = new Word();
        Word word2 = new Word();
        Word actual = new Word();
        Word expected = new Word();

        actual = word1.xor(word2);
        expected.set(0x00000000);
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        word2.set(0x00000000);      // == 0b 0000 0000 0000 0000 0000 0000 0000 0000
        actual = word1.xor(word2);
        expected.set(0xffffffff);   // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0x00000000);      // == 0b 0000 0000 0000 0000 0000 0000 0000 0000
        word2.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        actual = word1.xor(word2);
        expected.set(0xffffffff);   // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        word2.set(0xffffffff);      // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        actual = word1.xor(word2);
        expected.set(0x00000000);   // == 0b 0000 0000 0000 0000 0000 0000 0000 0000
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xaaaaaaaa);      // == 0b 1010 1010 1010 1010 1010 1010 1010 1010
        word2.set(0x55555555);      // == 0b 0101 0101 0101 0101 0101 0101 0101 0101
        actual = word1.xor(word2);
        expected.set(0xffffffff);   // == 0b 1111 1111 1111 1111 1111 1111 1111 1111
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0xabcdef12);      // == 0b 1010 1011 1100 1101 1110 1111 0001 0010
        word2.set(0x12345678);      // == 0b 0001 0010 0011 0100 0101 0110 0111 1000
        actual = word1.xor(word2);
        expected.set(0xb9f9b96a);   // == 0b 1011 1001 1111 1001 1011 1001 0110 1010
        assertEquals(expected.getSigned(), actual.getSigned());

        word1.set(0x871625ba);      // == 0b 1000 0111 0001 0110 0010 0101 1011 1010
        word2.set(0xabcc6731);      // == 0b 1010 1011 1100 1100 0110 0111 0011 0001
        actual = word1.xor(word2);
        expected.set(0x2cda428b);   // == 0b 0010 1100 1101 1010 0100 0010 1000 1011
        assertEquals(expected.getSigned(), actual.getSigned());
    }
    
    /**
     * tests not() method
     */
    @Test
    void testNot() {
        Word word = new Word();

        assertEquals(0, word.getSigned());

        word = word.not();
        assertEquals(0xffffffff, word.getSigned());

        word.set(0x55555555);
        word = word.not();
        assertEquals(0xaaaaaaaa, word.getSigned());

        word.set(0x1234abcd);
        word = word.not();
        assertEquals(0xedcb5432, word.getSigned());

        word.set(0xab2859ff);
        word = word.not();
        assertEquals(0x54d7a600, word.getSigned());
    }
    
    /**
     * tests both rightShift(int) methods
     */
    @Test
    void testRightShift() {
        Word word = new Word();

        word.set(0xffffffff);
        word = word.logicalRightShift(1);
        assertEquals(0x7fffffff, word.getSigned());

        word = word.logicalRightShift(4);
        assertEquals(0x07ffffff, word.getSigned());

        word = word.logicalRightShift(9);
        assertEquals(0x0003ffff, word.getSigned());

        word.set(0xa7483921);
        word = word.logicalRightShift(31);
        assertEquals(0x00000001, word.getSigned());

        word.set(0xa23bfad0);
        word = word.arithmeticRightShift(9);
        assertEquals(0xffd11dfd, word.getSigned());

        word = word.arithmeticRightShift(16);
        assertEquals(0xffffffd1, word.getSigned());
    }
    
    /**
     * tests leftShift(int) method
     */
    @Test
    void testLeftShift() {
        Word word = new Word();

        word.set(0xffffffff);
        word = word.leftShift(1);
        assertEquals(0xfffffffe, word.getSigned());

        word = word.leftShift(4);
        assertEquals(0xffffffe0, word.getSigned());

        word = word.leftShift(9);
        assertEquals(0xffffc000, word.getSigned());

        word.set(0xa7483921);
        word = word.leftShift(31);
        assertEquals(0x80000000, word.getSigned());
    }
    
    /**
     * tests toString() method
     */
    @Test
    void testToString() {
        Word word = new Word();
        
        word.set(0x00000000);
        assertEquals("f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f", word.toString());

        word.set(0xffffffff);
        assertEquals("t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t,t", word.toString());

        word.set(0xabcd3657);
        assertEquals("t,f,t,f,t,f,t,t,t,t,f,f,t,t,f,t,f,f,t,t,f,t,t,f,f,t,f,t,f,t,t,t", word.toString());
    }
    
    /**
     * tests copy(Word) method
     */
    @Test
    void testCopy() {
        Word word1 = new Word();
        Word word2 = new Word();

        word2.copy(word1);
        assertEquals(word1.getSigned(), word2.getSigned());

        word1.set(0xffffffff);
        word2.copy(word1);
        assertEquals(word1.getSigned(), word2.getSigned());

        word1.set(0x98345872);
        word2.copy(word1);
        assertEquals(word1.getSigned(), word2.getSigned());

        word1.set(0xadf375fe);
        word2.copy(word1);
        assertEquals(word1.getSigned(), word2.getSigned());

        word1.set(0x8720bfcc);
        word2.copy(word1);
        assertEquals(word1.getSigned(), word2.getSigned());
    }

    /**
     * tests set(int), getSigned(), and getUnsigned() methods
     */
    @Test
    void testSet_GetSigned_GetUnsigned() {
        Word word = new Word();

        word.set(0x00000000);   // 0x00000000 == 0 == 0
        assertEquals(0L, word.getUnsigned());
        assertEquals(0, word.getSigned());

        word.set(0xffffffff);   // 0xffffffff == 4,294,967,295 == -1
        assertEquals(4294967295L, word.getUnsigned());
        assertEquals(-1,          word.getSigned());

        word.set(0x234fbc78);   // 0x234fbc78 == 592,428,152 == 592,428,152
        assertEquals(592428152L, word.getUnsigned());
        assertEquals(592428152,  word.getSigned());

        word.set(0x0031fad9);   // 0x0031fad9 == 3,275,481 == 3,275,481
        assertEquals(3275481L, word.getUnsigned());
        assertEquals(3275481,  word.getSigned());

        word.set(0xa09832fb);   // 0xa09832fb == 2,694,329,083 == -1,600,638,213
        assertEquals(2694329083L, word.getUnsigned());
        assertEquals(-1600638213, word.getSigned());
    }

    /**
     * tests increment() method
     */
    @Test
    void testIncrement(){
        Word word = new Word();

        word.increment();
        assertEquals(1, word.getSigned());

        word.increment();
        assertEquals(2, word.getSigned());

        word.set(12345);
        word.increment();
        assertEquals(12346, word.getSigned());
    }

    /**
     * tests decrement() method
     */
    @Test
    void testDecrement(){
        Word word = new Word();
        
        word.decrement();;
        assertEquals(-1, word.getSigned());

        word.set(53);
        word.decrement();
        assertEquals(52, word.getSigned());

        word.set(235512);
        word.decrement();
        assertEquals(235511, word.getSigned());
    }

    /**
     * tests twosComp() method
     */
    @Test
    void testTwosComp(){
        Word word = new Word();

        word.set(20);
        assertEquals(20, word.getSigned());
        assertEquals(-20, word.twosComp().getSigned());

        word.set(-500);
        assertEquals(-500 ,word.getSigned());
        assertEquals(500 ,word.twosComp().getSigned());
    }
}