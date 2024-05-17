import DataTypes.*;

public class MainMemory {
    /////////////////////////////
    // Class Instance Variable //
    /////////////////////////////
    private static Word[] memory = new Word[1024];


    /**
     * Null constructor that fills the Word[]
     */
    private MainMemory(){
        for (int i = 0; i < 1024; i++){
            MainMemory.memory[i] = new Word();
        }
    }


    ///////////////////////////
    // Public Static Methods //
    ///////////////////////////
    /**
     * @param address
     * @return a new Word object containing the info at index 'address'
     * @throws Exception 
     */
    public static Word read(Word address) throws Exception{
        if (MainMemory.memory[(int) address.getUnsigned()] == null){
            new MainMemory();
        }
        Word toReturn = new Word();
        toReturn.copy(memory[(int) address.getUnsigned()]);
        return toReturn;
    }

    /**
     * Copy value into the array at index address
     * @param address
     * @param value
     * @throws Exception 
     */
    public static void write(Word address, Word value) throws Exception{
        if (MainMemory.memory[(int) address.getUnsigned()] == null){
            new MainMemory();
        }
        memory[(int) address.getUnsigned()].copy(value);
    }

    /**
     * Process an array of Strings into the simulated DRAM array
     * starting with 0
     * @param data
     * @throws Exception 
     */
    public static void load(String[] data) throws Exception{
        if (MainMemory.memory[0] == null){
                new MainMemory();
        }
        for (int i = 0; i < data.length; i++){    
            for (int j = 0; j < 32; j++){
                if (data[i].charAt(j) == '1') {
                    memory[i].setBit(31-j, new Bit(true));
                }
                else{
                    memory[i].setBit(31-j, new Bit(false));
                }
            }
        }
    }
}