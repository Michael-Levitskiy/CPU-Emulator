import java.util.HashMap;

public class BranchNode extends StatementNode{
    
    enum BOP {eq, neq, lt, ge, gt, le};

    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private final HashMap<BranchNode.BOP, Word> functions;
    private final BOP bop;
    private final int Rd;
    private final int Rs1;
    private final int Rs2;
    private final int imm;
    

    /**
     * Constructor for MATH
     * If rs2 == -1, then it's 2R, otherwise 3R
     * @param mop
     * @param rd
     * @param rs1
     * @param rs2
     */
    public BranchNode(BranchNode.BOP bop, int rd, int rs1, int rs2, int imm) {
        this.bop = bop;
        Rd = rd;
        Rs1 = rs1;
        Rs2 = rs2;
        this.imm = imm;
        this.functions = this.setHashMap();
    }

    
    ///////////////
    // Accessors //
    ///////////////
    public BOP getBop() {
        return bop;
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
        Word immediate = new Word();
        immediate.set(this.imm);
        Word function = functions.get(this.bop);
        

        if (Rs2 == -1){     // if Rs2 == -1, then 2R instruction
            Word opCode = new Word();
            opCode.set(0b00111);
            Word instruction = opCode.or(rd.leftShift(5)).or(function.leftShift(10))
                                .or(rs1.leftShift(14)).or(immediate.leftShift(19));
            return this.toBits(instruction);
        }
        else{               // else, 3R instruction
            Word rs2 = new Word();
            rs2.set(this.Rs2);
            Word opCode = new Word();
            opCode.set(0b00110);
            Word instruction = opCode.or(rd.leftShift(5)).or(function.leftShift(10))
                                .or(rs2.leftShift(14)).or(rs1.leftShift(19)).or(immediate.leftShift(24));
            return this.toBits(instruction);
        }
    }
    
    
    ////////////////////////////
    // Private Helper Methods //
    ////////////////////////////
    /**
     * Private helper method to fill a hashmap with the BOP with there appropriate code
     * @return a hashmap
     */
    private HashMap<BranchNode.BOP, Word> setHashMap() {
        HashMap<BranchNode.BOP, Word> functions = new HashMap<>();
        
        // add equals
        Word eqFunction = new Word();
        eqFunction.set(0);
        functions.put(BranchNode.BOP.eq, eqFunction);

        // add not equals
        Word neqFunction = new Word();
        neqFunction.set(1);
        functions.put(BranchNode.BOP.neq, neqFunction);

        // add less than
        Word ltFunction = new Word();
        ltFunction.set(2);
        functions.put(BranchNode.BOP.lt, ltFunction);

        // add greater than or equal
        Word geFunction = new Word();
        geFunction.set(3);
        functions.put(BranchNode.BOP.ge, geFunction);

        // add greater than
        Word gtFunction = new Word();
        gtFunction.set(4);
        functions.put(BranchNode.BOP.gt, gtFunction);

        // add less than or equal
        Word leFunction = new Word();
        leFunction.set(5);
        functions.put(BranchNode.BOP.le, leFunction);

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