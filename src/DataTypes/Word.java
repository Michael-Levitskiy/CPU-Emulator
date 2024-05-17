package DataTypes;

public class Word {
    /////////////////////////////
    // Class instance variable //
    /////////////////////////////
    private Bit[] word = new Bit[32];


    /////////////////
    // Constructor //
    /////////////////
    /**
     * Null Constructor
     */
    public Word() {
        for (int i = 0; i < 32; i++){
            word[i] = new Bit();
        }
    }


    ///////////////
    // Accessors //
    ///////////////
    /**
     * Get a new Bit that has the same value as Bit[i]
     * @param i (int) - index in the array to retrieve the value from
     * @return a Bit with the same value as Bit[i]
     */
    public Bit getBit(int i){
        Bit result = new Bit();
        result.set(this.word[i].getValue());
        return result;
    }

    /**
     * @return the value of this word as an unsigned long
     */
    public long getUnsigned(){
        long result = 0;
        long powOf2 = 1;
        for (int i = 0; i < 32; i++){
            long x = this.word[i].getValue() ? 1 : 0;
            result += x * powOf2;
            powOf2 *= 2;
        }
        return result;
    }
    
    /**
     * @return the value of this word as a signed int
     */
    public int getSigned(){
        int result = 0;
        int powOf2 = 1;
        
        // if the sign bit is on, call twosComp() and negate answer at the end
        if (this.word[31].getValue() == true){
            Word twosComp = this.twosComp();
            for (int i = 0; i < 32; i++){
                int x = twosComp.getBit(i).getValue() ? 1 : 0;
                result += x * powOf2;
                powOf2 *= 2;
            }
            result *= -1;
        }
        else{
            for (int i = 0; i < 32; i++){
                int x = this.word[i].getValue() ? 1 : 0;
                result += x * powOf2;
                powOf2 *= 2;
            }
        }
        return result;
    }
    

    //////////////
    // Mutators //
    //////////////
    /**
     * Set Bit i's value
     * @param i (int) - index of the Bit to set
     * @param value (Bit) - value to be set at i
     */
    public void setBit(int i, Bit value){
        word[i].set(value.getValue());
    }
    
    /**
     * Set the value of the bits of this Word
     * @param value (int) - the number to be stored
     */
    public void set(int value){
        int bit = 0;
        int remainder = value;
        
        // if value is negative, negate remainder and find twosComp later
        if (value < 0){
            remainder *= -1;
        }
        // for loop to perform integer and modular division to determine bit values
        for (int i = 31; i >= 0; i--){
            bit = remainder / (int) Math.pow(2, i);
            remainder %= (int) Math.pow(2, i);
            
            if (bit == 1){
                this.word[i].set(true);
            }
            else{
                this.word[i].set(false);
            }
        }
        // if value is negative, perform twosComp and copy into 'this' Word
        if (value < 0){
            Word newWord = this.twosComp();
            this.copy(newWord);
        }
    }

    /**
     * Copies the values of the bits from another Word into 'this' Word
     * @param other (Word) - needs to be copied into 'this' Word
     */
    public void copy(Word other){
        for (int i = 0; i < 32; i++){
            this.word[i].set(other.getBit(i).getValue());
        }
    }

    /**
     * Increment the current value in 'this' word by 1
     */
    public void increment(){
        Bit carry = new Bit(true);

        for(int i = 0; i < 32; i++){
            Bit newBit = this.getBit(i).xor(carry);
            carry = this.getBit(i).and(carry);
            this.setBit(i, newBit);
            if (carry.getValue() == false) {break;}
        }
    }

    /**
     * Decrement the current value in 'this' word by 1
     */
    public void decrement(){
        for(int i = 0; i < 32; i++){
            if (this.getBit(i).getValue()){
                this.setBit(i, new Bit(false));
                return;
            }
            else{
                this.setBit(i, new Bit(true));
            }
        }
    }


    /////////////////////
    // Word Operations //
    /////////////////////
    /**
     * Perform AND between two words
     * @param other (Word) to perform AND with 'this' Word
     * @return a new word with the resulting value
     */
    public Word and(Word other){
        Word result = new Word();
        for (int i = 0; i < 32; i++){
            Bit newBit = new Bit();
            newBit = this.word[i].and(other.getBit(i));
            result.setBit(i, newBit);
        }
        return result;
    }
    
    /**
     * Perform OR between two words
     * @param other (Word) to perform OR with 'this' Word
     * @return a new Word with the resulting value
     */
    public Word or(Word other){
        Word result = new Word();
        for (int i = 0; i < 32; i++){
            Bit newBit = new Bit();
            newBit = this.word[i].or(other.getBit(i));
            result.setBit(i, newBit);
        }
        return result;
    }
    
    /**
     * Perform XOR between two words
     * @param other (Word) to perform XOR with 'this' Word
     * @return a new Word with the resulting value
     */
    public Word xor(Word other){
        Word result = new Word();
        for (int i = 0; i < 32; i++){
            Bit newBit = new Bit();
            newBit = this.word[i].xor(other.getBit(i));
            result.setBit(i, newBit);
        }
        return result;
    }
    
    /**
     * Negate 'this' Word
     * @return a new Word with the resulting value
     */
    public Word not(){
        Word result = new Word();
        for (int i = 0; i < 32; i++){
            Bit newBit = new Bit();
            newBit = this.word[i].not();
            result.setBit(i, newBit);
        }
        return result;
    }

    /**
     * Use arithmetic right shift on 'this' word by a certain amount of bits
     * @param amount (int) - the value of how many bits to shift
     * @return a new Word with the resulting value
     */
    public Word arithmeticRightShift(int amount){
        Word result = new Word();
        for (int i = 0; i < 32-amount; i++){
            result.word[i].set(this.word[i+amount].getValue());
        }
        if (this.getBit(31).getValue()){
            for (int i = 31; i >= 32-amount; i--){
                result.word[i].set();
            }
        }
        return result;
    }

    /**
     * Use logical right shift 'this' word by a certain amount of bits
     * @param amount (int) - the value of how many bits to shift
     * @return a new Word with the resulting value
     */
    public Word logicalRightShift(int amount){
        Word result = new Word();
        for (int i = 0; i < 32-amount; i++){
            result.word[i].set(this.word[i+amount].getValue());
        }
        return result;
    }
    
    /**
     * Left shift 'this' word by a certain amount of bits
     * @param amount (int) - the value of how many bits to shift
     * @return a new Word with the resulting value
     */
    public Word leftShift(int amount){
        Word result = new Word();
        for (int i = 31; i >= amount; i--){
            result.word[i].set(this.word[i-amount].getValue());
        }
        return result;
    }
    
    /**
     * Method to find the twos compliment of a number
     * @return a new Word which contains the result
     */
    public Word twosComp(){
        Word result = this.not();
        result.increment();
        return result;
    }


    ///////////////////////
    // Overridden Method //
    ///////////////////////
    /**
     * @return a string of multiple "t" and/or "f" separated by commas
     */
    @Override
    public String toString() {
        String toReturn = "";
        for (int i = 31; i >= 0; i--){
            toReturn += this.word[i].toString();
            if(i != 0){
                toReturn += ",";
            }
        }
        return toReturn;
    }
}