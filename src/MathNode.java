import java.util.HashMap;

public class MathNode extends StatementNode{
    
    enum MOP {add, sub, mult, and, or, xor, not, leftShift, rightShift};

    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private final HashMap<MathNode.MOP, Word> functions;
    private final MOP mop;
    private final int Rd;
    private final int Rs1;
    private final int Rs2;
    

    /**
     * Constructor for MATH
     * If rs2 == -1, then it's 2R, otherwise 3R
     * @param mop
     * @param rd
     * @param rs1
     * @param rs2
     */
    public MathNode(MathNode.MOP mop, int rd, int rs1, int rs2) {
        this.mop = mop;
        Rd = rd;
        Rs1 = rs1;
        Rs2 = rs2;
        this.functions = this.setHashMap();
    }


    ///////////////
    // Accessors //
    ///////////////
    public MOP getMop() {
        return mop;
    }

    public int getRd() {
        return Rd;
    }

    public int getRs1() {
        return Rs1;
    }

    public int getRs2() {
        return Rs2;
    }
    

    /**
     * Overridden toString() method
     */
    @Override
    public String toString() {
        Word rd = new Word();
        rd.set(this.Rd);
        Word rs1 = new Word();
        rs1.set(this.Rs1);
        Word function = functions.get(this.mop);

        if (Rs2 == -1){     // if Rs2 == -1, then 2R instruction
            Word opCode = new Word();
            opCode.set(0b00011);
            Word instruction = opCode.or(rd.leftShift(5)).or(function.leftShift(10))
                                .or(rs1.leftShift(14));
            return this.toBits(instruction);
        }
        else{               // else, 3R instruction
            Word rs2 = new Word();
            rs2.set(this.Rs2);
            Word opCode = new Word();
            opCode.set(0b00010);
            Word instruction = opCode.or(rd.leftShift(5)).or(function.leftShift(10))
                                .or(rs2.leftShift(14)).or(rs1.leftShift(19));
            return this.toBits(instruction);
        }
    }


    ////////////////////////////
    // Private Helper Methods //
    ////////////////////////////
    /**
     * Method to fill the hashmap with the MOP and their corresponding bit values
     * @return
     */
    private HashMap<MathNode.MOP, Word> setHashMap(){
        HashMap<MathNode.MOP, Word> functions = new HashMap<>();
        
        // add AND
        Word andFunction = new Word();
        andFunction.set(8);
        functions.put(MathNode.MOP.and, andFunction);

        // add OR
        Word orFunction = new Word();
        orFunction.set(9);
        functions.put(MathNode.MOP.or, orFunction);

        // add XOR
        Word xorFunction = new Word();
        xorFunction.set(10);
        functions.put(MathNode.MOP.xor, xorFunction);

        // add NOT
        Word notFunction = new Word();
        notFunction.set(11);
        functions.put(MathNode.MOP.not, notFunction);

        // add leftShift
        Word lsFunction = new Word();
        lsFunction.set(12);
        functions.put(MathNode.MOP.leftShift, lsFunction);

        // add rightShift
        Word rsFunction = new Word();
        rsFunction.set(13);
        functions.put(MathNode.MOP.rightShift, rsFunction);

        // add ADD
        Word addFunction = new Word();
        addFunction.set(14);
        functions.put(MathNode.MOP.add, addFunction);

        // add SUB
        Word subFunction = new Word();
        subFunction.set(15);
        functions.put(MathNode.MOP.sub, subFunction);

        // add MULT
        Word multFunction = new Word();
        multFunction.set(7);
        functions.put(MathNode.MOP.mult, multFunction);

        return functions;
    }

    /**
     * Private method to convert the type Word to a string of 1's and 0's
     * @param word
     * @return
     */
    private String toBits(Word word){
        String toReturn = "";
        for (int i = 31; i >= 0; i--){
            if(word.getBit(i).toString().equals("t")){
                toReturn += "1";
            }
            else{
                toReturn += "0";
            }
        }
        return toReturn;
    }
}