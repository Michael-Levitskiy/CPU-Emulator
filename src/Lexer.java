import java.util.HashMap;
import java.util.LinkedList;

public class Lexer {

    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private CodeHandler codeHandler;
    private int lineNumber = 1;
    private int position = 0;
    private HashMap<String, Token.TokenType> knownWords;


    /////////////////
    // Constructor //
    /////////////////
    /**
     * @param fileName (String) to be passed into the CodeHandler variable
     */
    public Lexer(String fileName) {
        this.codeHandler = new CodeHandler(fileName);
        this.fillHashMap();
    }


    ///////////////////
    // Public Method //
    ///////////////////
    /**
     * Given a file name, use code handler to break down the contents of the file
     *      into separate tokens and store the tokens in a linked list
     * @return a LinkedList of Tokens made from the file
     * @throws Exception 
     */
    public LinkedList<Token> lex() throws Exception{
        LinkedList<Token> toReturn = new LinkedList<>();
        
        while(!this.codeHandler.IsDone()){
            char currentChar = this.codeHandler.Peek(0);
            
            // If space or tab, move past it and increment position
            if(currentChar == ' ' || currentChar == '\t'){
                this.codeHandler.Swallow(1);
                this.position++;
            }
            // If linefeed, create new EndOfLine Token, add to Linked list, increment lineNumber as set position to 0
            else if(currentChar == '\n'){
                toReturn.add(new Token(Token.TokenType.ENDOFLINE, this.lineNumber, this.position));
                this.lineNumber++;
                this.position = 0;
                this.codeHandler.Swallow(1);
            }
            // If carriage return, ignore it
            else if(currentChar == '\r'){
                this.codeHandler.Swallow(1);
            }
            // If char is an 'R', create a register token
            else if(currentChar == 'R' && Character.isDigit(this.codeHandler.Peek(1))){
                toReturn.add(this.processRegister());
            }
            // If letter, call helper method to create token and add to LinkedList
            else if(Character.isLetter(currentChar)){
                toReturn.add(this.processWord());
            }
            // If digit, call helper method to create token and add to LinkedList
            else if(Character.isDigit(currentChar) || currentChar == '-'){
                toReturn.add(this.processNumber());
            }
            // Throw an exception if the char is not listed above
            else{
                throw new Exception("Unknown Character on Line: " + this.lineNumber + ", Position: " + this.position);
            }
        }
        // If the file doesn't end with a ENDOFLINE token, add token
        if (toReturn.getLast().getType() != Token.TokenType.ENDOFLINE){
            toReturn.add(new Token(Token.TokenType.ENDOFLINE, this.lineNumber, this.position));
        }
        return toReturn;
    }


    /////////////////////
    // Private Methods //
    /////////////////////
    /**
     * Create a Register Token
     * @return the Token
     * @throws Exception 
     */
    private Token processRegister() throws Exception{
        String regNum = new String();                   // string to hold the register number
        int startPosition = this.position++;            // store the beginning position of the register name
        char currentChar = this.codeHandler.GetChar();  // get the current char and make sure that it's a 'R'
        if (currentChar == 'R'){
            currentChar = this.codeHandler.Peek(0);   // get the next char
            while (Character.isDigit(currentChar)){     // while the char is a digit
                regNum += this.codeHandler.GetChar();   // add the number to the string
                this.position++;                        // increment position

                // check if the file ended before peeking another char
                if (this.codeHandler.IsDone()){break;}
                currentChar = codeHandler.Peek(0);
            }
            // check that the register number is valid (0-31)
            if (Integer.parseInt(regNum) > 31){
                throw new Exception("Invalid register number at line " + this.lineNumber + ", position " + this.position);
            }
            else{
                return new Token(Token.TokenType.REGISTER, regNum, this.lineNumber, startPosition);
            }
        }
        else{
            throw new Exception("processRegister() method was improperly called");
        }
    }

    /**
     * Create a WORD token
     * @return the Token
     * @throws Exception 
     */
    private Token processWord() throws Exception{
        String value = "" + this.codeHandler.GetChar();     // initialize value of the token
        int startPosition = this.position++;                // hold the value of the starting position, post-increment to preserve position
        char currentChar = this.codeHandler.Peek(0);      // get the current char  

        // Loop continues as long as the currentChar is a letter or a digit
        while(Character.isLetter(currentChar) || Character.isDigit(currentChar)){
            value += this.codeHandler.GetChar();
            this.position++;
            
            // check if file ended before peeking another char
            if (this.codeHandler.IsDone()){break;}
            currentChar = this.codeHandler.Peek(0);
        }
        // check if value is a known word or if it's a label
        if (knownWords.containsKey(value)){
            return new Token(knownWords.get(value), this.lineNumber, startPosition);
        }
        throw new Exception("Unknown Word at line number " + this.lineNumber + ", position " + this.position);
    }

    /**
     * Create a NUMBER token
     * @return the Token
     */
    private Token processNumber(){
        String value = "" + codeHandler.GetChar();  // initialize value of the token
        int startPosition = this.position++;        // store the value of the starting position, post-increment to preserve position
        char currentChar = codeHandler.Peek(0);   // get the current char

        // loop continues as long as the char is a digit or decimal
        while(Character.isDigit(currentChar)){
            value += this.codeHandler.GetChar();
            this.position++;

            // check if the file ended before peeking another char
            if (this.codeHandler.IsDone()){break;}
            currentChar = codeHandler.Peek(0);
        }
        // create token
        Token toReturn = new Token(Token.TokenType.NUMBER, value, this.lineNumber, startPosition);
        return toReturn;
    }

    /**
     * Private method to fill the hash map of known words
     */
    private void fillHashMap(){
        // create and fill knownWords
        this.knownWords = new HashMap<>();
        this.knownWords.put("MATH", Token.TokenType.MATH);
        this.knownWords.put("ADD", Token.TokenType.ADD);
        this.knownWords.put("SUB", Token.TokenType.SUB);
        this.knownWords.put("MULT", Token.TokenType.MULT);
        this.knownWords.put("AND", Token.TokenType.AND);
        this.knownWords.put("OR", Token.TokenType.OR);
        this.knownWords.put("NOT", Token.TokenType.NOT);
        this.knownWords.put("XOR", Token.TokenType.XOR);
        this.knownWords.put("COPY", Token.TokenType.COPY);
        this.knownWords.put("HALT", Token.TokenType.HALT);
        this.knownWords.put("BRANCH", Token.TokenType.BRANCH);
        this.knownWords.put("JUMP", Token.TokenType.JUMP);
        this.knownWords.put("CALL", Token.TokenType.CALL);
        this.knownWords.put("PUSH", Token.TokenType.PUSH);
        this.knownWords.put("LOAD", Token.TokenType.LOAD);
        this.knownWords.put("RETURN", Token.TokenType.RETURN);
        this.knownWords.put("STORE", Token.TokenType.STORE);
        this.knownWords.put("PEEK", Token.TokenType.PEEK);
        this.knownWords.put("POP", Token.TokenType.POP);
        this.knownWords.put("EQ", Token.TokenType.EQUAL);
        this.knownWords.put("NEQ", Token.TokenType.UNEQUAL);
        this.knownWords.put("GT", Token.TokenType.GREATER);
        this.knownWords.put("LT", Token.TokenType.LESS);
        this.knownWords.put("GE", Token.TokenType.greaterOrEqual);
        this.knownWords.put("LE", Token.TokenType.lessOrEqual);
        this.knownWords.put("SHIFT", Token.TokenType.SHIFT);
        this.knownWords.put("LEFT", Token.TokenType.LEFT);
        this.knownWords.put("RIGHT", Token.TokenType.RIGHT);
    }
}