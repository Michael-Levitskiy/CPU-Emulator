import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;
import DataTypes.*;

public class MainMemoryTest {
    /**
     * tests read(Word) and write(Word, Word) methods
     * @throws Exception 
     */
    @Test
    void testRead_Write() throws Exception {
        Word address = new Word();
        Word value = new Word();

        address.set(0);
        value.set(0x0);
        MainMemory.write(address, value);
        assertEquals(value.getUnsigned(), MainMemory.read(address).getUnsigned());

        address.set(1023);
        value.set(0xffffffff);
        MainMemory.write(address, value);
        assertEquals(value.getUnsigned(), MainMemory.read(address).getUnsigned());

        address.set(432);
        value.set(0xabfc3620);
        MainMemory.write(address, value);
        assertEquals(value.getUnsigned(), MainMemory.read(address).getUnsigned());

        address.set(1000);
        value.set(0xbbbf7238);
        MainMemory.write(address, value);

        address.set(149);
        value.set(0xfbac9128);
        MainMemory.write(address, value);

        address.set(1000);
        value.set(0xbbbf7238);
        assertEquals(value.getUnsigned(), MainMemory.read(address).getUnsigned());

        address.set(149);
        value.set(0xfbac9128);
        assertEquals(value.getUnsigned(), MainMemory.read(address).getUnsigned());
    }
    
    /**
     * tests load(String[]) method
     * @throws Exception 
     */
    @Test
    void testLoad() throws Exception {
        Word address = new Word();
        String[] data = {"01101001011010011001011100111111",
                         "11111111111111111111111111111111",
                         "00000000000000000000000000000000",
                         "01101001011110001010101001110101",
                         "10011111011001001010001010100110",
                         "10010111100011101100101011100000",
                         "01110101011100001000100100101110"};
        
        MainMemory.load(data);
        
        address.set(0);
        assertEquals(0x6969973f, MainMemory.read(address).getSigned());

        address.set(1);
        assertEquals(0xffffffff, MainMemory.read(address).getSigned());
        
        address.set(2);
        assertEquals(0x0, MainMemory.read(address).getSigned());
        
        address.set(3);
        assertEquals(0x6978aa75, MainMemory.read(address).getSigned());
        
        address.set(4);
        assertEquals(0x9f64a2a6, MainMemory.read(address).getSigned());
        
        address.set(5);
        assertEquals(0x978ecae0, MainMemory.read(address).getSigned());
        
        address.set(6);
        assertEquals(0x7570892e, MainMemory.read(address).getSigned());
    }
}