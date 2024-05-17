import DataTypes.*;

public class L2Cache {
    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private static Word[] Cache1 = new Word[8];
    private static Word[] Cache2 = new Word[8];
    private static Word[] Cache3 = new Word[8];
    private static Word[] Cache4 = new Word[8];
    private static Word startAddress1;
    private static Word startAddress2;
    private static Word startAddress3;
    private static Word startAddress4;
    private static int nextToFill = 0;
    public static int L2CacheClockCycles = 0;
    public static int L2CacheHits = 0;
    public static int L2CacheMisses = 0;
    

    /**
     * Null Constructor
     */
    private L2Cache(){
        startAddress1 = new Word();
        startAddress1.set(0xffffffff);
        startAddress2 = new Word();
        startAddress2.set(0xffffffff);
        startAddress3 = new Word();
        startAddress3.set(0xffffffff);
        startAddress4 = new Word();
        startAddress4.set(0xffffffff);
        for (int i = 0; i < 8; i++){
            Cache1[i] = new Word();
            Cache2[i] = new Word();
            Cache3[i] = new Word();
            Cache4[i] = new Word();
        }
    }
    

    ///////////////////////////
    // Public Static Methods //
    ///////////////////////////
    /**
     * Copy value into the array at index address
     * @param address
     * @param value
     * @throws Exception 
     */
    public static void write(Word address, Word value) throws Exception{
        // Check that our cache is initialized
        if (startAddress1 == null){
            new L2Cache();
        }

        if ((address.getUnsigned() >= startAddress1.getUnsigned()) && 
                (address.getUnsigned() < startAddress1.getUnsigned()+8)){
            Cache1[(int)(address.getUnsigned() - startAddress1.getUnsigned())].copy(value);
            MainMemory.write(address, value);
        }
        else if((address.getUnsigned() >= startAddress2.getUnsigned()) && 
                (address.getUnsigned() < startAddress2.getUnsigned()+8)){
            Cache2[(int)(address.getUnsigned() - startAddress2.getUnsigned())].copy(value);
            MainMemory.write(address, value);
        }
        else if((address.getUnsigned() >= startAddress3.getUnsigned()) && 
                (address.getUnsigned() < startAddress3.getUnsigned()+8)){
            Cache3[(int)(address.getUnsigned() - startAddress3.getUnsigned())].copy(value);
            MainMemory.write(address, value);
        }
        else if((address.getUnsigned() >= startAddress4.getUnsigned()) && 
                (address.getUnsigned() < startAddress4.getUnsigned()+8)){
            Cache4[(int)(address.getUnsigned() - startAddress4.getUnsigned())].copy(value);
            MainMemory.write(address, value);
        }
        else{
            // if address is not in L2Cache, call method that determines which block to replace
            cacheMiss(address);
            L2Cache.write(address, value);
            return;
        }
        L2CacheClockCycles += 50;
    }
    
    /**
     * @param address
     * @return a new Word object containing the info at index 'address'
     * @throws Exception 
     */
    public static Word read(Word address) throws Exception{
        // Check that our cache is initialized
        if (startAddress1 == null){
            new L2Cache();
        }

        L2CacheClockCycles += 50;

        if ((address.getUnsigned() >= startAddress1.getUnsigned()) && 
                (address.getUnsigned() < startAddress1.getUnsigned()+8)){
            L2CacheHits++;
            return readCache1(address);
        }
        else if((address.getUnsigned() >= startAddress2.getUnsigned()) && 
                (address.getUnsigned() < startAddress2.getUnsigned()+8)){
            L2CacheHits++;
            return readCache2(address);
        }
        else if((address.getUnsigned() >= startAddress3.getUnsigned()) && 
                (address.getUnsigned() < startAddress3.getUnsigned()+8)){
            L2CacheHits++;
            return readCache3(address);
        }
        else if((address.getUnsigned() >= startAddress4.getUnsigned()) && 
                (address.getUnsigned() < startAddress4.getUnsigned()+8)){
            L2CacheHits++;
            return readCache4(address);
        }
        else{
            // if address is not in L2Cache, call method that determines which block to replace
            L2CacheMisses++;
            L2CacheClockCycles += 350;
            return cacheMiss(address);
        }
    }   


    /**
     * Method to execute when L2Cache miss
     * @param address
     * @return
     * @throws Exception 
     */
    private static Word cacheMiss(Word address) throws Exception{
        Word toReturn = new Word();
        Word copyAddress = new Word();

        // check that the address is not above 1016
        if (address.getUnsigned() > 1016){
            copyAddress.set(1016);
        }
        else{
            copyAddress.copy(address);
        }
        
        if(nextToFill % 4 == 0){
            for (int i = 0; i < 8; i++){
                Cache1[i].copy(MainMemory.read(copyAddress));
                copyAddress.increment();
            }
            startAddress1.copy(address);            // update startAddress
            toReturn.copy(Cache1[(int)(address.getUnsigned() - startAddress1.getUnsigned())]);
        }
        else if(nextToFill % 4 == 1){
            for (int i = 0; i < 8; i++){
                Cache2[i].copy(MainMemory.read(copyAddress));
                copyAddress.increment();
            }
            startAddress2.copy(address);            // update startAddress
            toReturn.copy(Cache2[(int)(address.getUnsigned() - startAddress2.getUnsigned())]);
        }
        else if(nextToFill % 4 == 2){
            for (int i = 0; i < 8; i++){
                Cache3[i].copy(MainMemory.read(copyAddress));
                copyAddress.increment();
            }
            startAddress3.copy(address);            // update startAddress
            toReturn.copy(Cache3[(int)(address.getUnsigned() - startAddress3.getUnsigned())]);
        }
        else{
            for (int i = 0; i < 8; i++){
                Cache4[i].copy(MainMemory.read(copyAddress));
                copyAddress.increment();
            }
            startAddress4.copy(address);            // update startAddress
            toReturn.copy(Cache4[(int)(address.getUnsigned() - startAddress4.getUnsigned())]);
        }
        nextToFill++;
        return toReturn;
    }


    //////////////////////////////////////////////////
    // Private methods to fill certain local caches //
    //////////////////////////////////////////////////
    private static Word readCache1(Word address) throws Exception{
        // update clock cycle for cache hit
        Word toReturn = new Word();
        toReturn.copy(Cache1[(int)(address.getUnsigned() - startAddress1.getUnsigned())]);
        return toReturn;
    }

    private static Word readCache2(Word address){
        // update clock cycle for cache hit
        Word toReturn = new Word();
        toReturn.copy(Cache2[(int)(address.getUnsigned() - startAddress2.getUnsigned())]);
        return toReturn;
    }

    private static Word readCache3(Word address){
        // update clock cycle for cache hit
        Word toReturn = new Word();
        toReturn.copy(Cache3[(int)(address.getUnsigned() - startAddress3.getUnsigned())]);
        return toReturn;
    }
    
    private static Word readCache4(Word address){
        // update clock cycle for cache hit
        Word toReturn = new Word();
        toReturn.copy(Cache4[(int)(address.getUnsigned() - startAddress4.getUnsigned())]);
        return toReturn;
    }
}