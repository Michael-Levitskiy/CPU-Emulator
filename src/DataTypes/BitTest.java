package DataTypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;

public class BitTest {

    /**
     * tests set() and set(Boolean) methods
     */
    @Test
    void testSet() {
        Bit Bit1 = new Bit();

        Bit1.set();
        assertTrue(Bit1.getValue());

        Bit1.set(false);
        assertFalse(Bit1.getValue());

        Bit1.set(true);
        assertTrue(Bit1.getValue());
    }

    /**
     * tests toggle() method
     */
    @Test
    void testToggle() {
        Bit Bit1 = new Bit(false);

        Bit1.toggle();
        assertTrue(Bit1.getValue());

        Bit1.toggle();
        assertFalse(Bit1.getValue());

        Bit1.toggle();
        assertTrue(Bit1.getValue());
    }

    /**
     * tests clear() method
     */
    @Test
    void testClear() {
        Bit Bit1 = new Bit(true);

        assertTrue(Bit1.getValue());

        Bit1.clear();
        assertFalse(Bit1.getValue());

        Bit1.clear();
        assertFalse(Bit1.getValue());
    }

    /**
     * tests getValue() method
     */
    @Test
    void testGetValue() {
        Bit Bit1 = new Bit(false);
        Bit Bit2 = new Bit(true);
        boolean x = Bit1.getValue();
        boolean y = Bit2.getValue();

        assertFalse(x);
        assertTrue(y);
    }

    /**
     * tests and(Bit) method
     */
    @Test
    void testAnd() {
        Bit Bit1 = new Bit(false);
        Bit Bit2 = new Bit(false);
        Bit Bit3 = new Bit(true);
        Bit Bit4 = new Bit(true);

        assertFalse(Bit1.and(Bit2).getValue());
        assertFalse(Bit1.and(Bit3).getValue());
        assertFalse(Bit3.and(Bit2).getValue());
        assertTrue(Bit3.and(Bit4).getValue());
    }

    /**
     * tests or(Bit) method
     */
    @Test
    void testOr() {
        Bit Bit1 = new Bit(false);
        Bit Bit2 = new Bit(false);
        Bit Bit3 = new Bit(true);
        Bit Bit4 = new Bit(true);

        assertFalse(Bit1.or(Bit2).getValue());
        assertTrue(Bit1.or(Bit3).getValue());
        assertTrue(Bit3.or(Bit2).getValue());
        assertTrue(Bit3.or(Bit4).getValue());
    }

    /**
     * tests xor(Bit) method
     */
    @Test
    void testXor() {
        Bit Bit1 = new Bit(false);
        Bit Bit2 = new Bit(false);
        Bit Bit3 = new Bit(true);
        Bit Bit4 = new Bit(true);

        assertFalse(Bit1.xor(Bit2).getValue());
        assertTrue(Bit1.xor(Bit3).getValue());
        assertTrue(Bit3.xor(Bit2).getValue());
        assertFalse(Bit3.xor(Bit4).getValue());
    }

    /**
     * tests not() method
     */
    @Test
    void testNot() {
        Bit Bit1 = new Bit(false);
        Bit Bit2 = Bit1.not();
        
        assertTrue(Bit2.getValue());

        Bit1 = Bit2.not();
        assertFalse(Bit1.getValue());
    }

    /**
     * tests toString() method
     */
    @Test
    void testToString() {
        Bit Bit1 = new Bit(false);
        Bit Bit2 = new Bit(true);

        assertTrue(Bit1.toString().equals("f"));
        assertTrue(Bit2.toString().equals("t"));
    }
}