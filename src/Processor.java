import DataTypes.*;

public class Processor {
    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private ALU alu = new ALU();
    private Word PC = new Word();
    private Word SP = new Word();
    private Word currentInstruction = new Word();
    private Bit Halted = new Bit();
    private Word[] registers = new Word[32];
    private Word opCode = new Word();
    private Word Rs1 = new Word();
    private Word Rs2 = new Word();
    private Word Rd  = new Word();
    private Word immediate = new Word();
    private Word function = new Word();
    private int currentClockCycle = 0;


    /**
     * Null Constructor
     */
    public Processor(){
        this.PC.set(0);
        this.SP.set(1024);
        for (int i = 0; i < 32; i++){
            registers[i] = new Word();
        }
    }


    /**
     * Accessor to get a certain register
     * @param regNum
     * @return
     */
    public Word getReg(int regNum){
        return registers[regNum];
    }


    /**
     * Method to execute code in 'memory'
     * @throws Exception 
     */
    public void run() throws Exception{
        this.Halted.set(false);
        while(this.Halted.not().getValue()){
            this.fetch();
            this.decode();
            this.execute();
            this.store();
        }

        currentClockCycle += InstructionCache.iCacheClockCycles + L2Cache.L2CacheClockCycles;
        System.out.println("Total Clock Cycles: " + currentClockCycle);
        System.out.println("Instruction Cache Hits: " + InstructionCache.InstructionCacheHits);
        System.out.println("Instruction Cache Misses: " + InstructionCache.InstructionCacheMisses);
        System.out.println("L2Cache Hits: " + L2Cache.L2CacheHits);
        System.out.println("L2Cache Misses: " + L2Cache.L2CacheMisses);
    }


    ////////////////////////////
    // Private Helper Methods //
    ////////////////////////////
    /**
     * Get the next instruction from memory, based on PC
     * Increment PC
     * @throws Exception 
     */
    private void fetch() throws Exception{
        this.currentInstruction.copy(InstructionCache.read(PC));
        PC.increment();
    }
    
    /**
     * Mask the OpCode in currentInstruction and fill Rs1, Rs2, and Rd appropriately
     */
    private void decode(){
        // create a mask and get the 5 LO bits in currentInstruction
        Word mask = new Word();
        mask.setBit(0, new Bit(true));
        mask.setBit(1, new Bit(true));
        mask.setBit(2, new Bit(true));
        mask.setBit(3, new Bit(true));
        mask.setBit(4, new Bit(true));
        this.opCode.copy(this.currentInstruction.and(mask));

        // set the Rs1, Rs2, and Rd registers appropriately
        if (this.opCode.getBit(1).getValue() == false){
            if (this.opCode.getBit(0).getValue() == false){
                // if XXX00, then no R
                // no need to set any Rs or Rd registers
                // set immediate value
                this.immediate.copy(this.currentInstruction.arithmeticRightShift(5));
            }
            else{   // if XXX01, then Dest Only
                // mask and determine which register is the Rd
                Word RegNum = this.currentInstruction.and(mask.leftShift(5)).logicalRightShift(5);
                this.Rd.copy(registers[toNum(RegNum)]);

                // set immediate value
                this.immediate.copy(this.currentInstruction.arithmeticRightShift(14));

                // mask and store function
                mask.setBit(4, new Bit(false));
                this.function.copy(this.currentInstruction.and(mask.leftShift(10)).logicalRightShift(10));
            }
        }
        else{
            if (this.opCode.getBit(0).getValue() == false){
                // if XXX10, then 3R
                // mask and determine which register is the Rd
                Word RegNum = this.currentInstruction.and(mask.leftShift(5)).logicalRightShift(5);
                this.Rd.copy(registers[toNum(RegNum)]);

                // mask and determine which register is the Rs2
                RegNum = this.currentInstruction.and(mask.leftShift(14)).logicalRightShift(14);
                this.Rs2.copy(registers[toNum(RegNum)]);

                // mask and determine which register is the Rs1
                RegNum = this.currentInstruction.and(mask.leftShift(19)).logicalRightShift(19);
                this.Rs1.copy(registers[toNum(RegNum)]);

                // set immediate value
                this.immediate.copy(this.currentInstruction.arithmeticRightShift(24));

                // mask and store function
                mask.setBit(4, new Bit(false));
                this.function.copy(this.currentInstruction.and(mask.leftShift(10)).logicalRightShift(10));
                
            }
            else{   // if XXX11, then 2R
                // mask and determine which register is the Rd
                Word RegNum = this.currentInstruction.and(mask.leftShift(5)).logicalRightShift(5);
                this.Rd.copy(registers[toNum(RegNum)]);

                // mask and determine which register is the Rs1
                RegNum = this.currentInstruction.and(mask.leftShift(14)).logicalRightShift(14);
                this.Rs1.copy(registers[toNum(RegNum)]);

                // set immediate value
                this.immediate.copy(this.currentInstruction.arithmeticRightShift(19));

                // mask and store function
                mask.setBit(4, new Bit(false));
                this.function.copy(this.currentInstruction.and(mask.leftShift(10)).logicalRightShift(10));
                
            }
        }
    }

    /**
     * Determine from the opcode what to do and how many registers to use
     * @throws Exception 
     */
    private void execute() throws Exception{
        // check 3 HO bits in OpCode to see what to do
        if (this.opCode.getBit(4).getValue() == false){
            if (this.opCode.getBit(3).getValue() == false){
                if (this.opCode.getBit(2).getValue() == false){
                    // if opCode = 000XX, call method to execute math
                    this.executeMath();
                }
                else{
                    // if opCode = 001XX, call method to execute branch
                    this.executeBranch();
                }
            }
            else{
                if (this.opCode.getBit(2).getValue() == false){
                    // if opCode = 010XX, call method to execute call
                    this.executeCall();
                }
                else{
                    // if opCode = 011XX, call method to execute push
                    this.executePush();
                }
            }
        }
        else{
            if (this.opCode.getBit(3).getValue() == false){
                if (this.opCode.getBit(2).getValue() == false){
                    // if opCode = 100XX, call method to execute load
                    this.executeLoad();
                }
                else{
                    // if opCode = 101XX, call method to execute store
                    this.executeStore();
                }
            }
            else{
                if (this.opCode.getBit(2).getValue() == false){
                    // if opCode = 110XX, call method to execute pop/interrupt
                    this.executePop();
                }
                else{
                    // if opCode = 111XX, error
                    throw new Exception("OpCode 111XX is not a valid instruction");
                }
            }
        }
    }

    /**
     * Determine from the opCode what to store and where
     * @throws Exception 
     */
    private void store() throws Exception{
        // check if it was a math operation
        if (this.opCode.getBit(4).getValue() == false && 
            this.opCode.getBit(3).getValue() == false &&
            this.opCode.getBit(2).getValue() == false){
            
            if (this.opCode.getBit(1).getValue() == false && this.opCode.getBit(0).getValue() == false){
                // if No R (halted instruction), nothing needs to be stored
                return;
            }
            // for every other mathOp (destOnly, 2R, or 3R), store Rd in correct register
            // create a mask of 5 bits to determine the register numbers to store values into
            Word mask = new Word();
            mask.setBit(0, new Bit(true));
            mask.setBit(1, new Bit(true));
            mask.setBit(2, new Bit(true));
            mask.setBit(3, new Bit(true));
            mask.setBit(4, new Bit(true));
            
            // mask and determine which register Rd belongs in
            Word RegNum = this.currentInstruction.and(mask.leftShift(5)).logicalRightShift(5);
            
            // if register destination is R0, don't store and return from function
            if (isZero(RegNum).getValue()) {return;}

            registers[toNum(RegNum)].copy(this.Rd);
        }

        // check if it was a branch operation
        else if(this.opCode.getBit(4).getValue() == false && 
                this.opCode.getBit(3).getValue() == false &&
                this.opCode.getBit(2).getValue()){
            // No need to do anything here, correct values are stored in PC as needed
        }

        // check if it was a call operation
        else if(this.opCode.getBit(4).getValue() == false && 
                this.opCode.getBit(3).getValue() &&
                this.opCode.getBit(2).getValue() == false){

            if (this.opCode.getBit(1).getValue() == false && this.opCode.getBit(0).getValue() == false){
                // if No R, immediate was already stored in PC at execution
                return;
            }
            // for every other opCode (destOnly, 2R, or 3R), store the result in ALU in the PC
            this.PC.copy(this.alu.result);
        }

        // check if it was a push operation
        else if(this.opCode.getBit(4).getValue() == false && 
                this.opCode.getBit(3).getValue() &&
                this.opCode.getBit(2).getValue()){

            // for every push opCode, decrement SP and write to memory at SP the result in the ALU
            this.SP.decrement();
            L2Cache.write(this.SP, this.alu.result);
        }

        // check if it was a load operation
        else if(this.opCode.getBit(4).getValue() && 
                this.opCode.getBit(3).getValue() == false &&
                this.opCode.getBit(2).getValue() == false){

            if (this.opCode.getBit(1).getValue() == false && this.opCode.getBit(0).getValue() == false){
                // if No R, Memory at SP was already stored in PC at execution and SP was incremented
                return;
            }
            // for other load opCodes, memory is stored in Rd and we need to store Rd into the correct register
            // create a mask of 5 bits to determine the register numbers to store values into
            Word mask = new Word();
            mask.setBit(0, new Bit(true));
            mask.setBit(1, new Bit(true));
            mask.setBit(2, new Bit(true));
            mask.setBit(3, new Bit(true));
            mask.setBit(4, new Bit(true));
            
            // mask and determine which register Rd belongs in
            Word RegNum = this.currentInstruction.and(mask.leftShift(5)).logicalRightShift(5);
            
            // if register destination is R0, don't store and return from function
            if (isZero(RegNum).getValue()) {return;}

            registers[toNum(RegNum)].copy(this.Rd);
        }

        // check if it was a store operation
        else if(this.opCode.getBit(4).getValue() && 
                this.opCode.getBit(3).getValue() == false &&
                this.opCode.getBit(2).getValue()){
            // no need to do anything, information was already written to memory during execution
        }

        // check if it was a pop operation
        else if(this.opCode.getBit(4).getValue() && 
                this.opCode.getBit(3).getValue() &&
                this.opCode.getBit(2).getValue() == false){
            // for all pop opCodes, memory is stored in Rd and we need to store Rd into the correct register
            // create a mask of 5 bits to determine the register numbers to store values into
            Word mask = new Word();
            mask.setBit(0, new Bit(true));
            mask.setBit(1, new Bit(true));
            mask.setBit(2, new Bit(true));
            mask.setBit(3, new Bit(true));
            mask.setBit(4, new Bit(true));
            
            // mask and determine which register Rd belongs in
            Word RegNum = this.currentInstruction.and(mask.leftShift(5)).logicalRightShift(5);
            
            // if register destination is R0, don't store and return from function
            if (isZero(RegNum).getValue()) {return;}

            registers[toNum(RegNum)].copy(this.Rd);
        }
        else{
            throw new Exception("Invalid Instruction in OpCode");
        }
    }    

    /**
     * Get the unsigned value of a given word
     * @param word (Word) - the word to calculate
     * @return the integer value of the Word
     */
    private int toNum(Word word){
        int result = 0;
        int powOf2 = 1;
        
        if (word.getBit(0).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(1).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(2).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(3).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(4).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(5).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(6).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(7).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(8).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(9).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(10).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(11).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(12).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(13).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(14).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(15).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(16).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(17).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(18).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(19).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(20).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(21).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(22).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(23).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(24).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(25).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(26).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(27).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(28).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(29).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(30).getValue()) {result += powOf2;}  powOf2 *= 2;
        if (word.getBit(31).getValue()) {result += powOf2;}
        
        return result;
    }

    /**
     * Given a Word, determine if the value is zero
     * @param word
     * @return a bit with the value of true if the word is equal to 0, false if not
     */
    private Bit isZero(Word word){
        if (word.getBit(0).getValue()) {return new Bit(false);}
        if (word.getBit(1).getValue()) {return new Bit(false);}
        if (word.getBit(2).getValue()) {return new Bit(false);}
        if (word.getBit(3).getValue()) {return new Bit(false);}
        if (word.getBit(4).getValue()) {return new Bit(false);}
        if (word.getBit(5).getValue()) {return new Bit(false);}
        if (word.getBit(6).getValue()) {return new Bit(false);}
        if (word.getBit(7).getValue()) {return new Bit(false);}
        if (word.getBit(8).getValue()) {return new Bit(false);}
        if (word.getBit(9).getValue()) {return new Bit(false);}
        if (word.getBit(10).getValue()) {return new Bit(false);}
        if (word.getBit(11).getValue()) {return new Bit(false);}
        if (word.getBit(12).getValue()) {return new Bit(false);}
        if (word.getBit(13).getValue()) {return new Bit(false);}
        if (word.getBit(14).getValue()) {return new Bit(false);}
        if (word.getBit(15).getValue()) {return new Bit(false);}
        if (word.getBit(16).getValue()) {return new Bit(false);}
        if (word.getBit(17).getValue()) {return new Bit(false);}
        if (word.getBit(18).getValue()) {return new Bit(false);}
        if (word.getBit(19).getValue()) {return new Bit(false);}
        if (word.getBit(20).getValue()) {return new Bit(false);}
        if (word.getBit(21).getValue()) {return new Bit(false);}
        if (word.getBit(22).getValue()) {return new Bit(false);}
        if (word.getBit(23).getValue()) {return new Bit(false);}
        if (word.getBit(24).getValue()) {return new Bit(false);}
        if (word.getBit(25).getValue()) {return new Bit(false);}
        if (word.getBit(26).getValue()) {return new Bit(false);}
        if (word.getBit(27).getValue()) {return new Bit(false);}
        if (word.getBit(28).getValue()) {return new Bit(false);}
        if (word.getBit(29).getValue()) {return new Bit(false);}
        if (word.getBit(30).getValue()) {return new Bit(false);}
        if (word.getBit(31).getValue()) {return new Bit(false);}
        return new Bit(true);
    }

    /**
     * Given 2 Words, look at function and determine the boolean operation to perform on the words
     * @param word1
     * @param word2
     * @return a Bit to represent whether the value is true or false
     * @throws Exception 
     */
    private Bit BOP(Word word1, Word word2) throws Exception{
        // create alu, copy words to alu, and create bits code for subtraction
        Bit[] subtract = {new Bit(true), new Bit(true), new Bit(true), new Bit(true)};
        this.alu.op1.copy(word1);
        this.alu.op2.copy(word2);
        this.alu.doOperation(subtract);

        this.currentClockCycle += 2;

        if (this.function.getBit(3).getValue()){
            // if BOP = 1XXX, operation does not exist, throw exception
            throw new Exception("Boolean Operation 1XXX does not exist");
        }
        else{
            if (this.function.getBit(2).getValue()){
                if (this.function.getBit(1).getValue()){
                    // if BOP = 011X, operation does not exist, throw exception
                    throw new Exception("Boolean Operation 011X does not exist");
                }
                else{
                    if (this.function.getBit(0).getValue()){
                        // if BOP = 0101, then less than or equal to operation
                        if ((this.alu.result.getBit(31).getValue()) || (this.isZero(this.alu.result).getValue())){
                            return new Bit(true);
                        }
                        else{
                            return new Bit(false);
                        }
                    }
                    else{
                        // if BOP = 0100, then greater than operation
                        if (this.alu.result.getBit(31).getValue()){
                            return new Bit(false);
                        }
                        else{
                            if (this.isZero(this.alu.result).getValue()){
                                return new Bit(false);
                            }
                            else{
                                return new Bit(true);
                            }
                        }
                    }
                }
            }
            else{
                if (this.function.getBit(1).getValue()){
                    if (this.function.getBit(0).getValue()){
                        // if BOP = 0011, then greater than or equal to operation
                        if (this.alu.result.getBit(31).getValue()){
                            return new Bit(false);
                        }
                        else{
                            return new Bit(true);
                        }
                    }
                    else{
                        // if BOP = 0010, then less than operation
                        if (this.alu.result.getBit(31).getValue()){
                            return new Bit(true);
                        }
                        else{
                            return new Bit(false);
                        }
                    }
                }
                else{
                    if (this.function.getBit(0).getValue()){
                        // if BOP = 0001, then not equal operation
                        Bit toReturn = isZero(this.alu.result);
                        toReturn.toggle();
                        return toReturn;
                    }
                    else{
                        // if BOP = 0000, then equals operation
                        return isZero(this.alu.result);
                    }
                }
            }
        }
    }

    /**
     * Method for when instructions state to perform Math
     */
    private void executeMath(){
        // if 000XX, perform Math,
        if (this.opCode.getBit(1).getValue()){
            Bit[] bits = {this.function.getBit(0), this.function.getBit(1),
                            this.function.getBit(2), this.function.getBit(3)};
                
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 00011, then copy Rd Math Operation Rs1 into Rd
                // set words in alu
                this.alu.op1.copy(this.Rd);
                this.alu.op2.copy(this.Rs1);              
                this.alu.doOperation(bits);
                this.Rd.copy(this.alu.result);
                
            }
            else{   // if opCode = 00010, then copy Rs1 Math Operation Rs2 into Rd
                // set words in alu
                this.alu.op1.copy(this.Rs1);
                this.alu.op2.copy(this.Rs2);
                this.alu.doOperation(bits);
                this.Rd.copy(this.alu.result);
            }
            this.aluClockCalc(bits);
        }
        else{
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 00001, then copy immediate value into Rd
                // Use 18 HO bits in instruction as Immediate value
                this.Rd.copy(this.immediate);
            }
            else{   // if opCode = 00000, then set Halt bit
                this.Halted.set(true);
            }
        }
    }

    /**
     * Method for when instructions state to perform Branch
     * @throws Exception 
     */
    private void executeBranch() throws Exception{
        if (this.opCode.getBit(1).getValue()){
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 00111, pc <- Rs BOP Rd? pc + imm : pc
                Bit BOP = this.BOP(this.Rs1, this.Rd);
                if (BOP.getValue()){
                    this.alu.op1.copy(this.PC);
                    this.alu.op2.copy(this.immediate);
                    Bit[] bits = {new Bit(false), new Bit(true), new Bit(true), new Bit(true)};
                    this.alu.doOperation(bits);
                    this.PC.copy(this.alu.result);

                    this.aluClockCalc(bits);
                }
            }
            else{   // if opCode = 00110, pc <- Rs1 BOP Rs2? pc + imm : pc
                Bit BOP = this.BOP(this.Rs1, this.Rs2);
                if (BOP.getValue()){
                    this.alu.op1.copy(this.PC);
                    this.alu.op2.copy(this.immediate);
                    Bit[] bits = {new Bit(false), new Bit(true), new Bit(true), new Bit(true)};
                    this.alu.doOperation(bits);
                    this.PC.copy(this.alu.result);

                    this.aluClockCalc(bits);
                }
            }
        }
        else{
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 00101, Jump: pc <- pc + imm
                this.alu.op1.copy(this.PC);
                this.alu.op2.copy(this.immediate);
                Bit[] bits = {new Bit(false), new Bit(true), new Bit(true), new Bit(true)};
                this.alu.doOperation(bits);
                this.PC.copy(this.alu.result);

                this.aluClockCalc(bits);
            }
            else{   // if opCode = 00100, Jump: pc <- imm
                this.PC.copy(this.immediate);
            }
        }
    }

    /**
     * Method for when instructions state to perform Call
     * @throws Exception 
     */
    private void executeCall() throws Exception{
        if (this.opCode.getBit(1).getValue()){
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 01011, pc <- Rs BOP Rd? push pc; pc + imm : pc
                Bit BOP = this.BOP(this.Rs1, this.Rd);
                if (BOP.getValue()){
                    this.SP.decrement();
                    L2Cache.write(this.SP, this.PC);
                    this.alu.op1.copy(this.PC);
                    this.alu.op2.copy(this.immediate);
                    Bit[] bits = {new Bit(false), new Bit(true), new Bit(true), new Bit(true)};
                    this.alu.doOperation(bits);

                    this.aluClockCalc(bits);
                }
            }
            else{   // if opCode = 01010, pc <- Rs1 BOP Rs2? push pc; Rd + imm : pc
                Bit BOP = this.BOP(this.Rs1, this.Rs2);
                if (BOP.getValue()){
                    this.SP.decrement();
                    L2Cache.write(this.SP, this.PC);
                    this.alu.op1.copy(this.Rd);
                    this.alu.op2.copy(this.immediate);
                    Bit[] bits = {new Bit(false), new Bit(true), new Bit(true), new Bit(true)};
                    this.alu.doOperation(bits);

                    this.aluClockCalc(bits);
                }
            }
        }
        else{
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 01001, push pc; pc <- Rd + imm
                this.SP.decrement();
                L2Cache.write(this.SP, this.PC);
                this.alu.op1.copy(this.Rd);
                this.alu.op2.copy(this.immediate);
                Bit[] bits = {new Bit(false), new Bit(true), new Bit(true), new Bit(true)};
                this.alu.doOperation(bits);

                this.aluClockCalc(bits);
            }
            else{   // if opCode = 01000, push pc; pc <- imm
                this.SP.decrement();
                L2Cache.write(this.SP, this.PC);
                this.PC.copy(this.immediate);
            }
        }
    }

    /**
     * Method for when instructions state to perform Push
     * @throws Exception 
     */
    private void executePush() throws Exception{
        Bit[] bits = {this.function.getBit(0), this.function.getBit(1),
                    this.function.getBit(2), this.function.getBit(3)};

        if (this.opCode.getBit(1).getValue()){
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 01111, mem[--sp] <- Rd MOP Rs
                this.alu.op1.copy(this.Rd);
                this.alu.op2.copy(this.Rs1);
                this.alu.doOperation(bits);
            }
            else{   // if opCode = 01110, mem[--sp] <- Rs1 MOP Rs2
                this.alu.op1.copy(this.Rs1);
                this.alu.op2.copy(this.Rs2);
                this.alu.doOperation(bits);
            }
            this.aluClockCalc(bits);
        }
        else{
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 01101, mem[--sp] <- Rd MOP imm
                this.alu.op1.copy(this.Rd);
                this.alu.op2.copy(this.immediate);
                this.alu.doOperation(bits);

                this.aluClockCalc(bits);
            }
            else{   // if opCode = 01100, UNUSED
                throw new Exception("01100 is not a valid OpCode");
            }
        }
    }

    /**
     * Method for when instructions state to perform Load
     * @throws Exception 
     */
    private void executeLoad() throws Exception{
        Bit[] bits = {new Bit(false), new Bit(true), new Bit(true), new Bit(true)};
        if (this.opCode.getBit(1).getValue()){
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 10011, Rd <- mem[Rs + imm]
                this.alu.op1.copy(this.Rs1);
                this.alu.op2.copy(this.immediate);
                this.alu.doOperation(bits);
                this.Rd.copy(L2Cache.read(this.alu.result));
            }
            else{   // if opCode = 10010, Rd <- mem[Rs1 + Rs2]
                this.alu.op1.copy(this.Rs1);
                this.alu.op2.copy(this.Rs2);
                this.alu.doOperation(bits);
                this.Rd.copy(L2Cache.read(this.alu.result));
            }
            this.aluClockCalc(bits);
        }
        else{
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 10001, Rd <- mem[Rd + imm]
                this.alu.op1.copy(this.Rd);
                this.alu.op2.copy(this.immediate);
                this.alu.doOperation(bits);
                this.Rd.copy(L2Cache.read(this.alu.result));
                
                this.aluClockCalc(bits);
            }
            else{   // if opCode = 10000, RETURN (pc <- pop)
                this.PC.copy(L2Cache.read(this.SP));
                this.SP.increment();
            }
        }
    }

    /**
     * Method for when instructions state to perform Store
     * @throws Exception 
     */
    private void executeStore() throws Exception{
        if (this.opCode.getBit(1).getValue()){
            Bit[] bits = {new Bit(false), new Bit(true), new Bit(true), new Bit(true)};
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 10111, mem[Rd + imm] <- Rs
                this.alu.op1.copy(this.Rd);
                this.alu.op2.copy(this.immediate);                
                this.alu.doOperation(bits);
                L2Cache.write(this.alu.result, this.Rs1);
            }
            else{   // if opCode = 10110, mem[Rd + Rs1] <- Rs2
                this.alu.op1.copy(this.Rd);
                this.alu.op2.copy(this.Rs1);
                this.alu.doOperation(bits);
                L2Cache.write(this.alu.result, this.Rs2);
            }
            this.aluClockCalc(bits);
        }
        else{
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 10101, mem[Rd] <- imm
                L2Cache.write(this.Rd, this.immediate);

                currentClockCycle += 300;
            }
            else{   // if opCode = 10100, UNUSED
                throw new Exception("10100 is not a valid OpCode");
            }
        }
    }

    /**
     * Method for when instructions state to perform Pop
     * @throws Exception 
     */
    private void executePop() throws Exception{
        if (this.opCode.getBit(1).getValue()){
            Bit[] bits = {new Bit(false), new Bit(true), new Bit(true), new Bit(true)};
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 11011, PEEK: Rd <- mem[sp - (Rs + imm)]
                this.alu.op1.copy(this.Rs1);
                this.alu.op2.copy(this.immediate);
                this.alu.doOperation(bits);
                // perform subtraction between sp and found sum
                this.alu.op1.copy(this.SP);
                this.alu.op2.copy(this.alu.result);
                this.alu.doOperation(bits);
                this.Rd.copy(L2Cache.read(this.alu.result));
            }
            else{   // if opCode = 11010, PEEK: Rd <- mem[sp - (Rs1 + Rs2)]
                // perform addition between Rs1 and Rs2
                this.alu.op1.copy(this.Rs1);
                this.alu.op2.copy(this.Rs2);
                this.alu.doOperation(bits);
                // perform subtraction between sp and found sum
                this.alu.op1.copy(this.SP);
                this.alu.op2.copy(this.alu.result);
                this.alu.doOperation(bits);
                this.Rd.copy(L2Cache.read(this.alu.result));
            }
            this.aluClockCalc(bits);
        }
        else{
            if (this.opCode.getBit(0).getValue()){
                // if opCode = 11001, POP: Rd <- mem[sp++]
                this.Rd.copy(L2Cache.read(this.SP));
                this.SP.increment();
            }
            else{   // if opCode = 11000, INTERRUPT: Push pc; pc <- intvec[imm]
                // Not implementing this code
                throw new Exception("11000 is not a valid OpCode");
            }
        }
    }


    /**
     * private method to update current clock cycle
     * according to bits operation
     * @param bits (Bit[]) - ALU operation to evaluate
     */
    private void aluClockCalc(Bit[] bits){
        if (bits[0].getValue() && bits[1].getValue() && bits[2].getValue() && !bits[3].getValue()){
            currentClockCycle += 10;
        }
        else{
            currentClockCycle += 2;
        }
    }
}