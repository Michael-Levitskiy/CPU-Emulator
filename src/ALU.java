import DataTypes.*;

public class ALU {
    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    public Word op1 = new Word();
    public Word op2 = new Word();
    public Word result = new Word();


    /////////////////
    // Constructor //
    /////////////////
    /**
     * Null Constructor
     */
    public ALU(){}


    //////////////
    // Mutators //
    //////////////
    /**
     * Perform a specific operation on op1 and op2 and store in result
     * @param operation (Bit[]) - Array of 4 Bits that state tbe operation
     */
    public void doOperation(Bit[] operation){
        // if statements to determine what operation instructs us to do
        if (operation[3].getValue() == true){
            if (operation[2].getValue() == false){
                if (operation[1].getValue() == false){
                    // if operation = 1000, perform AND
                    if (operation[0].getValue() == false){
                        this.result.copy(this.op1.and(this.op2));
                    }
                    // if operation = 1001, perform OR
                    else{
                        this.result.copy(this.op1.or(this.op2));
                    }
                }
                else{
                    // if operation = 1010, perform XOR
                    if (operation[0].getValue() == false){
                        this.result.copy(this.op1.xor(this.op2));
                    }
                    // if operation = 1011, perform NOT on op1
                    else{
                        this.result.copy(this.op1.not());
                    }
                }
            }
            else{
                if (operation[1].getValue() == false){
                    // if operation = 1100, left shift op1 by the value of the 5 low order bits in op2
                    if (operation[0].getValue() == false){
                        int shiftAmount = this.getLowOrder(5);
                        this.result.copy(this.op1.leftShift(shiftAmount));
                    }
                    // if operation = 1101, right shift op1 by the value of the 5 low order bits in op2
                    else{                                       
                        int shiftAmount = this.getLowOrder(5);
                        this.result.copy(this.op1.logicalRightShift(shiftAmount));
                    }
                }
                else{
                    // if operation = 1110, add op1 and op2
                    if (operation[0].getValue() == false){
                        this.result.copy(this.add2(this.op1, this.op2));
                    }
                    // if operation = 1111, subtract op1 and op2
                    else{
                        this.result.copy(this.add2(this.op1, this.op2.twosComp()));
                    }
                }
            }
        }
        else{
            if (operation[2].getValue() == true){
                if (operation[1].getValue() == true){
                    // if operation = 0111, multiply op1 and op2
                    if (operation[0].getValue() == true){
                        // initialize 32 words
                        Word word1 = new Word();
                        Word word2 = new Word();
                        Word word3 = new Word();
                        Word word4 = new Word();
                        Word word5 = new Word();
                        Word word6 = new Word();
                        Word word7 = new Word();
                        Word word8 = new Word();
                        Word word9 = new Word();
                        Word word10 = new Word();
                        Word word11 = new Word();
                        Word word12 = new Word();
                        Word word13 = new Word();
                        Word word14 = new Word();
                        Word word15 = new Word();
                        Word word16 = new Word();
                        Word word17 = new Word();
                        Word word18 = new Word();
                        Word word19 = new Word();
                        Word word20 = new Word();
                        Word word21 = new Word();
                        Word word22 = new Word();
                        Word word23 = new Word();
                        Word word24 = new Word();
                        Word word25 = new Word();
                        Word word26 = new Word();
                        Word word27 = new Word();
                        Word word28 = new Word();
                        Word word29 = new Word();
                        Word word30 = new Word();
                        Word word31 = new Word();
                        Word word32 = new Word();

                        // fill words with the appropriate values
                        if (this.op2.getBit(0).getValue()) {word1.copy(this.op1.leftShift(0));}
                        if (this.op2.getBit(1).getValue()) {word2.copy(this.op1.leftShift(1));}
                        if (this.op2.getBit(2).getValue()) {word3.copy(this.op1.leftShift(2));}
                        if (this.op2.getBit(3).getValue()) {word4.copy(this.op1.leftShift(3));}
                        if (this.op2.getBit(4).getValue()) {word5.copy(this.op1.leftShift(4));}
                        if (this.op2.getBit(5).getValue()) {word6.copy(this.op1.leftShift(5));}
                        if (this.op2.getBit(6).getValue()) {word7.copy(this.op1.leftShift(6));}
                        if (this.op2.getBit(7).getValue()) {word8.copy(this.op1.leftShift(7));}
                        if (this.op2.getBit(8).getValue()) {word9.copy(this.op1.leftShift(8));}
                        if (this.op2.getBit(9).getValue()) {word10.copy(this.op1.leftShift(9));}
                        if (this.op2.getBit(10).getValue()) {word11.copy(this.op1.leftShift(10));}
                        if (this.op2.getBit(11).getValue()) {word12.copy(this.op1.leftShift(11));}
                        if (this.op2.getBit(12).getValue()) {word13.copy(this.op1.leftShift(12));}
                        if (this.op2.getBit(13).getValue()) {word14.copy(this.op1.leftShift(13));}
                        if (this.op2.getBit(14).getValue()) {word15.copy(this.op1.leftShift(14));}
                        if (this.op2.getBit(15).getValue()) {word16.copy(this.op1.leftShift(15));}
                        if (this.op2.getBit(16).getValue()) {word17.copy(this.op1.leftShift(16));}
                        if (this.op2.getBit(17).getValue()) {word18.copy(this.op1.leftShift(17));}
                        if (this.op2.getBit(18).getValue()) {word19.copy(this.op1.leftShift(18));}
                        if (this.op2.getBit(19).getValue()) {word20.copy(this.op1.leftShift(19));}
                        if (this.op2.getBit(20).getValue()) {word21.copy(this.op1.leftShift(20));}
                        if (this.op2.getBit(21).getValue()) {word22.copy(this.op1.leftShift(21));}
                        if (this.op2.getBit(22).getValue()) {word23.copy(this.op1.leftShift(22));}
                        if (this.op2.getBit(23).getValue()) {word24.copy(this.op1.leftShift(23));}
                        if (this.op2.getBit(24).getValue()) {word25.copy(this.op1.leftShift(24));}
                        if (this.op2.getBit(25).getValue()) {word26.copy(this.op1.leftShift(25));}
                        if (this.op2.getBit(26).getValue()) {word27.copy(this.op1.leftShift(26));}
                        if (this.op2.getBit(27).getValue()) {word28.copy(this.op1.leftShift(27));}
                        if (this.op2.getBit(28).getValue()) {word29.copy(this.op1.leftShift(28));}
                        if (this.op2.getBit(29).getValue()) {word30.copy(this.op1.leftShift(29));}
                        if (this.op2.getBit(30).getValue()) {word31.copy(this.op1.leftShift(30));}
                        if (this.op2.getBit(31).getValue()) {word32.copy(this.op1.leftShift(31));}

                        // sum all of the Words together using add4 and add2
                        Word sum1 = this.add4(word1, word2, word3, word4);
                        Word sum2 = this.add4(word5, word6, word7, word8);
                        Word sum3 = this.add4(word9, word10, word11, word12);
                        Word sum4 = this.add4(word13, word14, word15, word16);
                        Word sum5 = this.add4(word17, word18, word19, word20);
                        Word sum6 = this.add4(word21, word22, word23, word24);
                        Word sum7 = this.add4(word25, word26, word27, word28);
                        Word sum8 = this.add4(word29, word30, word31, word32);
                        sum1 = add4(sum1, sum2, sum3, sum4);
                        sum2 = add4(sum5, sum6, sum7, sum8);
                        this.result.copy(this.add2(sum1, sum2));
                    }
                    else{
                        System.err.println("Operation '0110' does not exist");
                    }
                }
                else{
                    System.err.println("Operation '010-' does not exist");
                }
            }
            else{
                System.err.println("Operation '00--' does not exist");
            }
        }
    }


    ////////////////////
    // Public Methods //
    ////////////////////
    /**
     * Add two 32-bit Words together
     * @param word1 (Word)
     * @param word2 (Word)
     * @return the sum of the two words
     */
    public Word add2(Word word1, Word word2){
        Word toReturn = new Word();
        Bit carry = new Bit();
        Bit result, bit1, bit2;

        for (int i = 0; i < 32; i++){
            bit1 = word1.getBit(i);
            bit2 = word2.getBit(i);

            result = bit1.xor(bit2).xor(carry);
            carry = bit1.and(bit2).or((bit1.xor(bit2)).and(carry));

            toReturn.setBit(i, result);
        }
        return toReturn;
    }

    /**
     * Add four 32-bit words together
     * @param word1 (Word)
     * @param word2 (Word)
     * @param word3 (Word)
     * @param word4 (Word)
     * @return the sum of the four words
     */
    public Word add4(Word word1, Word word2, Word word3, Word word4){
        Word toReturn = new Word();
        Bit carryIn = new Bit();
        Bit carry1 = new Bit();
        Bit carry2 = new Bit();
        Bit carry3 = new Bit();
        Bit result, bit1, bit2, bit3, bit4;
        
        // for loop to add all of the bits
        for (int i = 0; i < 32; i++){
            bit1 = word1.getBit(i);
            bit2 = word2.getBit(i);
            bit3 = word3.getBit(i);
            bit4 = word4.getBit(i);

            // get result Bit by XOR among all of the bits
            result = bit1.xor(bit2).xor(bit3).xor(bit4).xor(carryIn);
            
            // before calculating the carry1 bit, check if bit is already set
            if (carry1.getValue()){
                carry1 = calcCarry1(carryIn, bit1, bit2, bit3, bit4);
                
                // if carry1 was true before and after calc
                if (carry1.getValue()){
                    carry1.clear();             // set carry1 to false and carry to carry2
                    
                    // check if carry2 is true
                    if (carry2.getValue()){
                        carry2 = calcCarry2(carryIn, bit1, bit2, bit3, bit4);
                        carry3.set();           // carry over and set carry3 to true
                    }
                    else{
                        carry2 = calcCarry2(carryIn, bit1, bit2, bit3, bit4);
                        
                        // if carry2 is true, set to false and carry over to carry3
                        if (carry2.getValue()){
                            carry2.clear();
                            carry3.set();
                        }
                        else{
                            carry2.set();       // preserve the original value
                        }
                    }
                }
                else{
                    carry1.set(true);   // preserve the original value

                    // check if carry2 is true
                    if (carry2.getValue()){
                        carry2 = calcCarry2(carryIn, bit1, bit2, bit3, bit4);
                        
                        // check if we need to carry to carry3
                        if (carry2.getValue()){
                            carry2.clear();         // set carry2 to false
                            carry3.set();           // carry over and set carry3 to true
                        }
                        else{
                            carry2.set();           // else, preserve the original value
                        }
                    }
                    else{
                        carry2 = calcCarry2(carryIn, bit1, bit2, bit3, bit4);
                    }
                }
            }
            else{   // if carry1 is originally false
                carry1 = calcCarry1(carryIn, bit1, bit2, bit3, bit4);

                // if carry2 is originally true
                if (carry2.getValue()){
                    carry2 = calcCarry2(carryIn, bit1, bit2, bit3, bit4);

                    // if carry2 is true again
                    if (carry2.getValue()){
                        carry2.clear();     // set to false
                        carry3.set();       // carry over and set carry3 to true
                    }
                    else{   // if carry2 becomes false
                        carry2.set();       // preserve original value
                    }
                }
                else{   // if carry2 if originally false
                    carry2 = calcCarry2(carryIn, bit1, bit2, bit3, bit4);
                }
            }
            
            // shift the carry's over
            carryIn = carry1;
            carry1 = carry2;
            carry2 = carry3;
            carry3.clear();

            // add result to word
            toReturn.setBit(i, result);
        }
        return toReturn;
    }


    /////////////////////
    // Private Methods //
    /////////////////////
    /**
     * Private method to get the value of the low order 'x' bits in op2
     * @param x (int) - the number of low order bits to calculate
     * @return the int value
     */
    private int getLowOrder(int x){
        int result = 0;
        int powOf2 = 1;
        for (int i = 0; i < x; i++){
            int y = op2.getBit(i).getValue() ? 1 : 0;
            result += y * powOf2;
            powOf2 *= 2;
        }
        return result;
    }

    /**
     * Private helper method to determine the value of the carry1 Bit when adding 4 bits and a carry
     * @param carryIn
     * @param bit1
     * @param bit2
     * @param bit3
     * @param bit4
     * @return
     */
    private Bit calcCarry1(Bit carryIn, Bit bit1, Bit bit2, Bit bit3, Bit bit4){
        return      (carryIn.and(bit1).and(bit2).and(bit3.or(bit4).not())).or(
                    carryIn.and(bit1).and(bit2.not()).and(bit3.and(bit4).not())).or(
                    carryIn.and(bit1.not()).and(bit2).and(bit3.and(bit4).not())).or(
                    carryIn.and(bit1.not()).and(bit2.not()).and(bit3.or(bit4))).or(
                    (carryIn.not()).and(bit1).and(bit2).and(bit3.and(bit4).not())).or(
                    (carryIn.not()).and(bit1).and(bit2.not()).and(bit3.or(bit4))).or(
                    (carryIn.or(bit1)).not().and(bit2).and(bit3.or(bit4))).or(
                    (carryIn.or(bit1).or(bit2)).not().and(bit3).and(bit4));
    }

    /**
     * Private helper method to determine the value of the carry2 Bit when adding 4 bits and a carry
     * @param carryIn
     * @param bit1
     * @param bit2
     * @param bit3
     * @param bit4
     * @return
     */
    private Bit calcCarry2(Bit carryIn, Bit bit1, Bit bit2, Bit bit3, Bit bit4){
        return      (carryIn.and(bit1).and(bit2).and(bit3).and(bit4)).or(
                    carryIn.and(bit1).and(bit2).and(bit3).and(bit4.not())).or(
                    carryIn.and(bit1).and(bit2).and(bit3.not()).and(bit4)).or(
                    carryIn.and(bit1).and(bit2.not()).and(bit3).and(bit4)).or(
                    carryIn.and(bit1.not()).and(bit2).and(bit3).and(bit4)).or(
                    (carryIn.not()).and(bit1).and(bit2).and(bit3).and(bit4));
    }
}