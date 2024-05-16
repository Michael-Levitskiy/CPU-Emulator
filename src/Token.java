public class Token {

    enum TokenType {REGISTER, NUMBER, ENDOFLINE, MATH, ADD, SUB, MULT, AND, OR, NOT, XOR, COPY,
                    HALT, BRANCH, JUMP, CALL, PUSH, LOAD, RETURN, STORE, PEEK, POP, EQUAL, UNEQUAL,
                    GREATER, LESS, greaterOrEqual, lessOrEqual, SHIFT, LEFT, RIGHT}

    //////////////////////////////
    // Class Instance Variables //
    //////////////////////////////
    private TokenType tokenType;
    private String value = new String();
    private int lineNumber;
    private int position;
    

    //////////////////
    // Constructors //
    //////////////////
    /**
     * Constructor given every variable instance
     * @param tokenType
     * @param value
     * @param lineNumber
     * @param position
     */
    public Token(TokenType tokenType, String value, int lineNumber, int position) {
        this.tokenType = tokenType;
        this.value = value;
        this.lineNumber = lineNumber;
        this.position = position;
    }

    /**
     * Constructor given every variable instance except 'value'
     * @param tokenType
     * @param lineNumber
     * @param position
     */
    public Token(TokenType tokenType, int lineNumber, int position) {
        this.tokenType = tokenType;
        this.lineNumber = lineNumber;
        this.position = position;
    }
    

    ///////////////
    // Accessors //
    ///////////////
    public TokenType getType(){
        return this.tokenType;
    }

    public String getValue() {
        return value;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getPosition() {
        return position;
    }

    
    /**
     * Overridden method to return class variable as a string
     */
    @Override
    public String toString() {
        return this.tokenType + "(" + this.value + ") " + this.lineNumber + "-" + this.position;
    }
}