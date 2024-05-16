public class Bit {
    /////////////////////////////
    // Class instance variable //
    /////////////////////////////
    private Boolean bit = false;


    //////////////////
    // Constructors //
    //////////////////
    /**
     * Constructor when given the bit value
     * @param value (Boolean) to set the bit
     */
    public Bit(Boolean value){
        this.bit = value;
    }

    /**
     * Null constructor
     * bit will be set to false as default
     */
    public Bit() {}


    //////////////
    // Mutators //
    //////////////
    /**
     * Sets the value of the bit
     * @param value (Boolean) for the bit to be set as
     */
    public void set(Boolean value){
        this.bit = value;
    }

    /**
     * Changes the bit from true to false or false to true
     */
    public void toggle(){
        if(this.bit == true){
            this.bit = false;
        }
        else{
            this.bit = true;
        }
    }

    /**
     * Sets the bit to be true
     */
    public void set(){
        this.bit = true;
    }

    /**
     * Sets the bit to be false
     */
    public void clear(){
        this.bit = false;
    }


    //////////////
    // Accessor //
    //////////////
    /**
     * @return a Boolean of the current bit value
     */
    public Boolean getValue(){
        return this.bit;
    }


    ////////////////////
    // Bit Operations //
    ////////////////////
    /**
     * Performs AND on twos bits and returns a new bit with the result
     * @param other (Bit) to perform AND with 'this' Bit
     * @return a Bit with the result of the AND operation
     */
    public Bit and(Bit other){
        Bit result = new Bit();
        if(this.bit == true){
            if(other.getValue() == true){
                result.set(true);
            }
        }
        return result;
    }

    /**
     * Performs OR on two bits and returns a new bit with the result
     * @param other (Bit) to perform OR with 'this' Bit
     * @return a Bit with the result of the OR operation
     */
    public Bit or(Bit other){
        Bit result = new Bit();
        if(this.bit == true){
            result.set(true);
        }
        else{
            if(other.getValue() == true){
                result.set(true);
            }
        }
        return result;
    }

    /**
     * Performs an XOR on two bits and returns a new Bit with the result
     * @param other (Bit) to perform XOR with 'this' Bit
     * @return a Bit with the result of the XOR operation
     */
    public Bit xor(Bit other){
        Bit result = new Bit();
        if(this.bit != other.getValue()){
            result.set(true);
        }
        return result;
    }

    /**
     * Performs NOT on the existing bit
     * @return the result as a new Bit
     */
    public Bit not(){
        Bit result = new Bit();
        if(this.bit == false){
            result.set(true);
        }
        return result;
    }


    ///////////////////////
    // Overridden Method //
    ///////////////////////
    /**
     * @return "t" or "f" depending on bit value
     */
    @Override
    public String toString(){
        if(this.bit == true){return "t";}
        else{return "f";}
    }
}