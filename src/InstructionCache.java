public class InstructionCache {
    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private static Word[] Cache = new Word[8];
    private static Word startAddress;
    public static int iCacheClockCycles = 0;
    public static int InstructionCacheHits = 0;
    public static int InstructionCacheMisses = 0;


    /**
     * Null Constructor
     */
    private InstructionCache(){
        startAddress = new Word();
        startAddress.set(0xffffffff);
        for (int i = 0; i < 8; i++){
            Cache[i] = new Word();
        }
    }
    
    
    //////////////////////////
    // Public Static Method //
    //////////////////////////
    /**
     * @param address
     * @return a new Word object containing the info at index 'address'
     * @throws Exception 
     */
    public static Word read(Word address) throws Exception{
        // Check that our cache is initialized
        if (startAddress == null){
            new InstructionCache();
        }
        
        // Check if the address is within our range
        if ((address.getUnsigned() < startAddress.getUnsigned()) ||
                (address.getUnsigned() >= startAddress.getUnsigned()+8)){
            // If address is not in cache range, fill cache from memory
            Word copyAddress = new Word();
            copyAddress.copy(address);
            for (int i = 0; i < 8; i++){
                Cache[i].copy(L2Cache.read(copyAddress));
                copyAddress.increment();
            }
            startAddress.copy(address);     // update startAddress
            InstructionCacheMisses++;
        }
        else{
            // if address in cache range, update clock cycle for cache hit
            iCacheClockCycles += 10;
            InstructionCacheHits++;
        }
        Word toReturn = new Word();
        toReturn.copy(Cache[(int)(address.getUnsigned() - startAddress.getUnsigned())]);
        return toReturn;
    }
}