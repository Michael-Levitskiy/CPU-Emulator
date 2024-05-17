package ASTNodes;

import DataTypes.*;

public class PopNode extends StatementNode{
    
    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private final int Rd;


    /**
     * Constructor
     * @param rd
     */
    public PopNode(int rd) {
        Rd = rd;
    }


    /**
     * Accessor
     */
    public int getRd() {
        return Rd;
    }


    /**
     * Overridden toString() method
     */
    @Override
    public String toString() {
        Word opCode = new Word();
        opCode.set(0b11001);
        Word rd = new Word();
        rd.set(Rd);
        Word instruction = opCode.or(rd.leftShift(5));
        return this.toBits(instruction);
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