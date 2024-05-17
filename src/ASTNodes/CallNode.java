package ASTNodes;

import java.util.HashMap;
import DataTypes.*;

public class CallNode extends StatementNode {
    
    public enum BOP {eq, neq, lt, ge, gt, le};

    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private final HashMap<CallNode.BOP, Word> functions;
    private final BOP bop;
    private final int Rd;
    private final int Rs1;
    private final int Rs2;
    private final int imm;


    /**
     * Constructor
     * @param bop
     * @param rd
     * @param rs1
     * @param rs2
     * @param imm
     */
    public CallNode(CallNode.BOP bop, int rd, int rs1, int rs2, int imm) {
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
    public int getImm() {
        return imm;
    }


    /**
     * Overridden toString() method
     */
    @Override
    public String toString() {
        Word immediate = new Word();
        immediate.set(this.imm);

        if (this.Rd == -1){         // if Rd = -1, then NoR
            Word opCode = new Word();
            opCode.set(0b01000);
            Word instruction = opCode.or(immediate.leftShift(5));
            return toBits(instruction);
        }
        else if (this.Rs1 == -1){   // if Rs1 = -1, then DestOnly
            Word rd = new Word();
            rd.set(Rd);
            Word opCode = new Word();
            opCode.set(0b01001);
            Word instruction = opCode.or(rd.leftShift(5)).or(immediate.leftShift(14));
            return toBits(instruction);
        }
        else if (this.Rs2 == -1){   // if Rs2 == -1, then 2R
            Word rd = new Word();
            rd.set(Rd);
            Word rs1 = new Word();
            rs1.set(Rs1);
            Word bop = this.functions.get(this.bop);
            Word opCode = new Word();
            opCode.set(0b01011);
            Word instruction = opCode.or(rd.leftShift(5)).or(bop.leftShift(10))
                                .or(rs1.leftShift(14)).or(immediate.leftShift(19));
            return this.toBits(instruction);
        }
        else{                       // else 3R
            Word rd = new Word();
            rd.set(Rd);
            Word rs1 = new Word();
            rs1.set(Rs1);
            Word rs2 = new Word();
            rs2.set(Rs2);
            Word bop = this.functions.get(this.bop);
            Word opCode = new Word();
            opCode.set(0b01010);
            Word instruction = opCode.or(rd.leftShift(5)).or(bop.leftShift(10))
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
    private HashMap<CallNode.BOP, Word> setHashMap() {
        HashMap<CallNode.BOP, Word> functions = new HashMap<>();
        
        // add equals
        Word eqFunction = new Word();
        eqFunction.set(0b0000);
        functions.put(CallNode.BOP.eq, eqFunction);

        // add not equals
        Word neqFunction = new Word();
        neqFunction.set(0b0001);
        functions.put(CallNode.BOP.neq, neqFunction);

        // add less than
        Word ltFunction = new Word();
        ltFunction.set(0b0010);
        functions.put(CallNode.BOP.lt, ltFunction);

        // add greater than or equal
        Word geFunction = new Word();
        geFunction.set(0b0011);
        functions.put(CallNode.BOP.ge, geFunction);

        // add greater than
        Word gtFunction = new Word();
        gtFunction.set(0b0100);
        functions.put(CallNode.BOP.gt, gtFunction);

        // add less than or equal
        Word leFunction = new Word();
        leFunction.set(0b0101);
        functions.put(CallNode.BOP.le, leFunction);

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