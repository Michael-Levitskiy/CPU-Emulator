public class LoadNode extends StatementNode{
    
    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
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
    public LoadNode(int rd, int rs1, int rs2, int imm) {
        Rd = rd;
        Rs1 = rs1;
        Rs2 = rs2;
        this.imm = imm;
    }
    

    ///////////////
    // Accessors //
    ///////////////
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

        if (this.Rs1 == -1){        // if Rs1 = -1, then DestOnly and get immediate
            Word opCode = new Word();
            opCode.set(0b10001);
            Word immediate = new Word();
            immediate.set(this.imm);
            Word instruction = opCode.or(rd.leftShift(5)).or(immediate.leftShift(14));
            return this.toBits(instruction);
        }
        else if(this.Rs2 == -1){    // if Rs2 = -1, then 2R
            Word opCode = new Word();
            opCode.set(0b10011);
            Word rs1 = new Word();
            rs1.set(this.Rs1);
            Word immediate = new Word();
            immediate.set(this.imm);
            Word instruction = opCode.or(rd.leftShift(5)).or(rs1.leftShift(14))
                                .or(immediate.leftShift(19));
            return this.toBits(instruction);
        }
        else{                       // else 3R
            Word opCode = new Word();
            opCode.set(0b10010);
            Word rs1 = new Word();
            rs1.set(this.Rs1);
            Word rs2 = new Word();
            rs2.set(this.Rs2);
            Word instruction = opCode.or(rd.leftShift(5)).or(rs2.leftShift(14))
                                .or(rs1.leftShift(19));
            return this.toBits(instruction);
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