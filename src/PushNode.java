import java.util.HashMap;

public class PushNode extends StatementNode {
    
    enum MOP {add, sub, mult, and, or, xor, not, leftShift, rightShift};

    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private final HashMap<PushNode.MOP, Word> functions;
    private final MOP mop;
    private final int Rd;
    private final int Rs1;
    private final int Rs2;
    private final int imm;
    

    /**
     * Constructor
     * @param mop
     * @param rd
     * @param rs1
     * @param rs2
     * @param imm
     */
    public PushNode(PushNode.MOP mop, int rd, int rs1, int rs2, int imm) {
        this.mop = mop;
        Rd = rd;
        Rs1 = rs1;
        Rs2 = rs2;
        this.imm = imm;
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
    public int getImm() {
        return imm;
    }


    /**
     * Overridden toString() method
     */
    @Override
    public String toString() {
        Word rd = new Word();
        rd.set(Rd);
        Word mop = this.functions.get(this.mop);

        if (this.Rs1 == -1){        // if Rs1 = -1, then DestOnly and get immediate
            Word opCode = new Word();
            opCode.set(0b01101);
            Word immediate = new Word();
            immediate.set(this.imm);
            Word instruction = opCode.or(rd.leftShift(5)).or(mop.leftShift(10))
                                .or(immediate.leftShift(14));
            return this.toBits(instruction);
        }
        else if(this.Rs2 == -1){    // if Rs2 = -1, then 2R
            Word opCode = new Word();
            opCode.set(0b01111);
            Word rs1 = new Word();
            rs1.set(this.Rs1);
            Word instruction = opCode.or(rd.leftShift(5)).or(mop.leftShift(10))
                                .or(rs1.leftShift(14));
            return this.toBits(instruction);
        }
        else{                       // else 3R
            Word opCode = new Word();
            opCode.set(0b01110);
            Word rs1 = new Word();
            rs1.set(this.Rs1);
            Word rs2 = new Word();
            rs2.set(this.Rs2);
            Word instruction = opCode.or(rd.leftShift(5)).or(mop.leftShift(10))
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
    private HashMap<PushNode.MOP, Word> setHashMap(){
        HashMap<PushNode.MOP, Word> functions = new HashMap<>();
        
        // add AND
        Word andFunction = new Word();
        andFunction.set(8);
        functions.put(PushNode.MOP.and, andFunction);

        // add OR
        Word orFunction = new Word();
        orFunction.set(9);
        functions.put(PushNode.MOP.or, orFunction);

        // add XOR
        Word xorFunction = new Word();
        xorFunction.set(10);
        functions.put(PushNode.MOP.xor, xorFunction);

        // add NOT
        Word notFunction = new Word();
        notFunction.set(11);
        functions.put(PushNode.MOP.not, notFunction);

        // add leftShift
        Word lsFunction = new Word();
        lsFunction.set(12);
        functions.put(PushNode.MOP.leftShift, lsFunction);

        // add rightShift
        Word rsFunction = new Word();
        rsFunction.set(13);
        functions.put(PushNode.MOP.rightShift, rsFunction);

        // add ADD
        Word addFunction = new Word();
        addFunction.set(14);
        functions.put(PushNode.MOP.add, addFunction);

        // add SUB
        Word subFunction = new Word();
        subFunction.set(15);
        functions.put(PushNode.MOP.sub, subFunction);

        // add MULT
        Word multFunction = new Word();
        multFunction.set(7);
        functions.put(PushNode.MOP.mult, multFunction);

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