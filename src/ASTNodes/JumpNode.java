package ASTNodes;

import DataTypes.*;

public class JumpNode extends StatementNode {
    
    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private final int Rd;
    private final int imm;


    /**
     * Constructor
     * @param rd
     * @param imm
     */
    public JumpNode(int rd, int imm) {
        Rd = rd;
        this.imm = imm;
    }


    ///////////////
    // Accessors //
    ///////////////
    public int getRd() {
        return Rd;
    }
    public int getImm() {
        return imm;
    }


    /**
     * Overridden toString() Method
     */
    @Override
    public String toString() {
        Word immediate = new Word();
        immediate.set(this.imm);

        if (this.Rd == -1){     // if Rd == -1, then NoR instruction
            Word opCode = new Word();
            opCode.set(0b00100);
            Word instruction = opCode.or(immediate.leftShift(5));
            return toBits(instruction);
        }
        else{                   // else, DestOnly instruction
            Word rd = new Word();
            rd.set(this.Rd);
            Word opCode = new Word();
            opCode.set(0b00101);
            Word instruction = opCode.or(rd.leftShift(5)).or(immediate.leftShift(14));
            return toBits(instruction);
        }
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