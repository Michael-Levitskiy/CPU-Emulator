import java.util.LinkedList;
import java.util.Optional;

public class Parser {
    
    // Class Instance Variable
    private TokenManager tokenManager;

    
    /**
     * Constructor
    */
    public Parser(LinkedList<Token> tokens) {
        this.tokenManager = new TokenManager(tokens);
    }


    /**
     * Public method to 'parse' the LinkedList of Tokens
     * Using TokenManager
     * @return the root, ProgramNode
     * @throws Exception 
     */
    public StatementsNode parse() throws Exception{
        LinkedList<StatementNode> statements = new LinkedList<>();
        
        // while there are more tokens
        while(tokenManager.MoreTokens()){
            statements.add(this.Statement());   // call statement and add to linked list
            this.AcceptSeparators();            // acceptor linefeed separators
        }
        return new StatementsNode(statements);
    }
    

    ////////////////////////////
    // Private Helper Methods //
    ////////////////////////////
    /**
     * Checks for linefeed, and continues checking if true
     * @return true if there was a linefeed and false if not
     */
    private boolean AcceptSeparators(){
        Optional<Token> next = tokenManager.Peek(0);    // peek the next token
        // return false if it's null or not an end of line
        if ((next.isEmpty()) || (next.get().getType() != Token.TokenType.ENDOFLINE)){
            return false;
        }
        // while loop that ends when we don't come across end of line in token manager
        while ((next.isPresent()) && (next.get().getType() == Token.TokenType.ENDOFLINE)){
            tokenManager.MatchAndRemove(Token.TokenType.ENDOFLINE);
            next = tokenManager.Peek(0);
        }
        return true;
    }

    /**
     * @return a StatementNode, which could be a PrintNode or AssignmentNode
     * @return null if node couldn't be made
     * @throws Exception 
     */
    private StatementNode Statement() throws Exception{
        // Check that we haven't reached the end of the list of tokens
        if (tokenManager.Peek(0).isPresent()){
            Token.TokenType type = tokenManager.Peek(0).get().getType();    // get the TokenType

            if (type == Token.TokenType.MATH){
                return this.Math();
            }
            if (type == Token.TokenType.BRANCH){
                return this.Branch();
            }
            if (type == Token.TokenType.HALT){
                this.tokenManager.MatchAndRemove(type);
                return new HaltNode();
            }
            if (type == Token.TokenType.COPY){
                return this.Copy();
            }
            if (type == Token.TokenType.JUMP){
                return this.Jump();
            }
            if (type == Token.TokenType.CALL){
                return this.Call();
            }
            if (type == Token.TokenType.PUSH){
                return this.Push();
            }
            if (type == Token.TokenType.POP){
                return this.Pop();
            }
            if (type == Token.TokenType.LOAD){
                return this.Load();
            }
            if (type == Token.TokenType.STORE){
                return this.Store();
            }
            if (type == Token.TokenType.RETURN){
                this.tokenManager.MatchAndRemove(type);
                return new ReturnNode();
            }
            if (type == Token.TokenType.PEEK){
                return this.Peek();
            }
        }
        return null;
    }

    /**
     * Method to run when MATH operation is indicated
     * @return a MathNode
     * @throws Exception 
     */
    private MathNode Math() throws Exception{
        // Double check that MATH is the operation
        if (tokenManager.MatchAndRemove(Token.TokenType.MATH).isPresent()){
            Optional<Token> token = tokenManager.Peek(0);
            if (token.isPresent()){
                Token.TokenType type = token.get().getType();
                tokenManager.MatchAndRemove(type);
                
                MathNode.MOP MOP;
                // check the type of the MOP and store the appropriate operation
                if (type == Token.TokenType.ADD){MOP = MathNode.MOP.add;}
                else if (type == Token.TokenType.SUB){MOP = MathNode.MOP.sub;}
                else if (type == Token.TokenType.MULT){MOP = MathNode.MOP.mult;}
                else if (type == Token.TokenType.AND){MOP = MathNode.MOP.and;}
                else if (type == Token.TokenType.OR){MOP = MathNode.MOP.or;}
                else if (type == Token.TokenType.XOR){MOP = MathNode.MOP.xor;}
                else if (type == Token.TokenType.NOT){MOP = MathNode.MOP.not;}
                else if (type == Token.TokenType.SHIFT){
                    // if shift, check whether its a left or right shift
                    if (tokenManager.MatchAndRemove(Token.TokenType.LEFT).isPresent()){
                        MOP = MathNode.MOP.leftShift;
                    }
                    else if (tokenManager.MatchAndRemove(Token.TokenType.RIGHT).isPresent()){
                        MOP = MathNode.MOP.rightShift;
                    }
                    else{throw new Exception("Expected 'LEFT' or 'RIGHT' after 'SHIFT'");}
                }
                else{throw new Exception("Invalid MOP in MATH");}

                // expecting either 2 or 3 registers to follow
                // Get the Rd register
                Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (RdToken.isEmpty()){throw new Exception("Expecting a Register after MATH MOP");}
                int Rd = Integer.parseInt(RdToken.get().getValue());

                // Get the Rs1 register
                Optional<Token> Rs1Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (Rs1Token.isEmpty()){throw new Exception("Expecting a Register after MATH MOP Rd");}
                int Rs1 = Integer.parseInt(Rs1Token.get().getValue());

                // If Rs2 register exists, get int value, otherwise -1
                Optional<Token> Rs2Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                int Rs2 = -1;
                if (Rs2Token.isPresent()){
                    Rs2 = Integer.parseInt(Rs2Token.get().getValue());
                }

                return new MathNode(MOP, Rd, Rs1, Rs2);
            }
        }
        return null;
    }

    /**
     * Method to run when COPY operation is indicated
     * @return a CopyNode
     * @throws Exception
     */
    private CopyNode Copy() throws Exception{
        // Double check that COPY is the operation
        if (tokenManager.MatchAndRemove(Token.TokenType.COPY).isPresent()){
            // expecting a register next
            Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
            if (RdToken.isEmpty()){throw new Exception("Expected a Rd after COPY");}
            int Rd = Integer.parseInt(RdToken.get().getValue());

            // expecting a number next
            Optional<Token> immToken = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
            if (immToken.isEmpty()){throw new Exception("Expected an immediate value after COPY Rd");}
            int imm = Integer.parseInt(immToken.get().getValue());

            return new CopyNode(Rd, imm);
        }
        return null;
    }

    /**
     * Method to run when BRANCH operation is indicated
     * @return a BranchNode
     * @throws Exception 
     */
    private BranchNode Branch() throws Exception{
        // Double check that BRANCH is the operation
        if (tokenManager.MatchAndRemove(Token.TokenType.BRANCH).isPresent()){
            Optional<Token> token = tokenManager.Peek(0);
            if (token.isPresent()){
                Token.TokenType type = token.get().getType();
                tokenManager.MatchAndRemove(type);
                
                BranchNode.BOP BOP;
                // check the type of the MOP and store the appropriate operation
                if (type == Token.TokenType.EQUAL){BOP = BranchNode.BOP.eq;}
                else if (type == Token.TokenType.UNEQUAL){BOP = BranchNode.BOP.neq;}
                else if (type == Token.TokenType.LESS){BOP = BranchNode.BOP.le;}
                else if (type == Token.TokenType.greaterOrEqual){BOP = BranchNode.BOP.ge;}
                else if (type == Token.TokenType.GREATER){BOP = BranchNode.BOP.gt;}
                else if (type == Token.TokenType.lessOrEqual){BOP = BranchNode.BOP.le;}
                else{throw new Exception("Invalid BOP in BRANCH");}

                // expecting either 2 or 3 registers to follow
                // Get the Rs1 register
                Optional<Token> Rs1Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (Rs1Token.isEmpty()){throw new Exception("Expecting a Register after BRANCH BOP");}
                int Rs1 = Integer.parseInt(Rs1Token.get().getValue());

                // Get the Rd register
                Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (RdToken.isEmpty()){throw new Exception("Expecting a Register after BRANCH BOP Rs1");}
                int Rd = Integer.parseInt(RdToken.get().getValue());

                // If Rs2 register exists, get int value, otherwise -1
                Optional<Token> Rs2Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                int Rs2 = -1;
                if (Rs2Token.isPresent()){
                    Rs2 = Integer.parseInt(Rs2Token.get().getValue());
                }

                // get immediate value
                Optional<Token> immediate = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                if (immediate.isEmpty()){throw new Exception("Expecting an Immediate at the end of BRANCH statement");}
                int imm = Integer.parseInt(immediate.get().getValue());

                return new BranchNode(BOP, Rd, Rs1, Rs2, imm);
            }
        }
        return null;
    }

    /**
     * Method to run when JUMP operation is indicated
     * @return a JumpNode
     * @throws Exception 
     */
    private JumpNode Jump() throws Exception{
        // Double check that JUMP is the operation
        if (tokenManager.MatchAndRemove(Token.TokenType.JUMP).isPresent()){
            Optional<Token> token = tokenManager.Peek(0);
            if (token.isPresent()){
                // expecting either 1 or 0 registers to follow
                // If Rd register exists, get int value, otherwise -1
                Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                int Rd = -1;
                if (RdToken.isPresent()){
                    Rd = Integer.parseInt(RdToken.get().getValue());
                }

                // get immediate value
                Optional<Token> immediate = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                if (immediate.isEmpty()){throw new Exception("Expecting an Immediate at the end of JUMP statement");}
                int imm = Integer.parseInt(immediate.get().getValue());

                return new JumpNode(Rd, imm);
            }
        }
        return null;
    }

    /**
     * Method to run when CALL operation is indicated
     * @return a CallNode
     * @throws Exception 
     */
    private CallNode Call() throws Exception{
        // Double check that CALL is the operation
        if (tokenManager.MatchAndRemove(Token.TokenType.CALL).isPresent()){
            Optional<Token> token = tokenManager.Peek(0);
            if (token.isPresent()){
                Token.TokenType type = token.get().getType();
                // determine whether the next token is either BOP or not
                if (type == Token.TokenType.REGISTER || type == Token.TokenType.NUMBER){
                    // if number or register, operation is either DestOnly or NoR
                    // If Rd register exists, get int value, otherwise -1
                    Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                    int Rd = -1;
                    if (RdToken.isPresent()){
                        Rd = Integer.parseInt(RdToken.get().getValue());
                    }

                    // get immediate value
                    Optional<Token> immediate = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                    if (immediate.isEmpty()){throw new Exception("Expecting an Immediate at the end of CALL statement");}
                    int imm = Integer.parseInt(immediate.get().getValue());

                    return new CallNode(null, Rd, -1, -1, imm);
                }
                
                // otherwise, next token is BOP and operation is either 2R or 3R
                tokenManager.MatchAndRemove(type);

                CallNode.BOP BOP;
                // check the type of the MOP and store the appropriate operation
                if (type == Token.TokenType.EQUAL){BOP = CallNode.BOP.eq;}
                else if (type == Token.TokenType.UNEQUAL){BOP = CallNode.BOP.neq;}
                else if (type == Token.TokenType.LESS){BOP = CallNode.BOP.le;}
                else if (type == Token.TokenType.greaterOrEqual){BOP = CallNode.BOP.ge;}
                else if (type == Token.TokenType.GREATER){BOP = CallNode.BOP.gt;}
                else if (type == Token.TokenType.lessOrEqual){BOP = CallNode.BOP.le;}
                else{throw new Exception("Invalid BOP in CALL");}

                // expecting either 2 or 3 registers to follow
                // Get the Rs1 register
                Optional<Token> Rs1Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (Rs1Token.isEmpty()){throw new Exception("Expecting a Register after CALL BOP");}
                int Rs1 = Integer.parseInt(Rs1Token.get().getValue());

                // Get the Rd register
                Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (RdToken.isEmpty()){throw new Exception("Expecting a Register after CALL BOP Rs1");}
                int Rd = Integer.parseInt(RdToken.get().getValue());

                // If Rs2 register exists, get int value, otherwise -1
                Optional<Token> Rs2Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                int Rs2 = -1;
                if (Rs2Token.isPresent()){
                    Rs2 = Integer.parseInt(Rs2Token.get().getValue());
                }

                // get immediate value
                Optional<Token> immediate = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                if (immediate.isEmpty()){throw new Exception("Expecting an Immediate at the end of CALL statement");}
                int imm = Integer.parseInt(immediate.get().getValue());

                return new CallNode(BOP, Rd, Rs1, Rs2, imm);
            }
        }
        return null;
    }

    /**
     * Method to run when PUSH operation is indicated
     * @return
     * @throws Exception 
     */
    private PushNode Push() throws Exception{
        // Double check that PUSH is the operation
        if (tokenManager.MatchAndRemove(Token.TokenType.PUSH).isPresent()){
            Optional<Token> token = tokenManager.Peek(0);
            if (token.isPresent()){
                Token.TokenType type = token.get().getType();
                tokenManager.MatchAndRemove(type);
                
                PushNode.MOP MOP;
                // check the type of the MOP and store the appropriate operation
                if (type == Token.TokenType.ADD){MOP = PushNode.MOP.add;}
                else if (type == Token.TokenType.SUB){MOP = PushNode.MOP.sub;}
                else if (type == Token.TokenType.MULT){MOP = PushNode.MOP.mult;}
                else if (type == Token.TokenType.AND){MOP = PushNode.MOP.and;}
                else if (type == Token.TokenType.OR){MOP = PushNode.MOP.or;}
                else if (type == Token.TokenType.XOR){MOP = PushNode.MOP.xor;}
                else if (type == Token.TokenType.NOT){MOP = PushNode.MOP.not;}
                else if (type == Token.TokenType.SHIFT){
                    // if shift, check whether its a left or right shift
                    if (tokenManager.MatchAndRemove(Token.TokenType.LEFT).isPresent()){
                        MOP = PushNode.MOP.leftShift;
                    }
                    else if (tokenManager.MatchAndRemove(Token.TokenType.RIGHT).isPresent()){
                        MOP = PushNode.MOP.rightShift;
                    }
                    else{throw new Exception("Expected 'LEFT' or 'RIGHT' after 'SHIFT'");}
                }
                else{throw new Exception("Invalid MOP in PUSH");}

                // expecting either 1, 2, or 3 registers to follow
                // Get the Rd register
                Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (RdToken.isEmpty()){throw new Exception("Expecting a Register after PUSH MOP");}
                int Rd = Integer.parseInt(RdToken.get().getValue());

                // if the Rs1 register, then either 2R or 3R
                Optional<Token> Rs1Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (Rs1Token.isPresent()){
                    int Rs1 = Integer.parseInt(Rs1Token.get().getValue());

                    // If Rs2 register exists, get int value, otherwise -1
                    Optional<Token> Rs2Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                    int Rs2 = -1;
                    if (Rs2Token.isPresent()){
                        Rs2 = Integer.parseInt(Rs2Token.get().getValue());
                    }

                    return new PushNode(MOP, Rd, Rs1, Rs2, 0);
                }
                else{   // else, DestOnly and get immediate
                    // get immediate value
                    Optional<Token> immediate = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                    if (immediate.isEmpty()){throw new Exception("Expecting an Immediate at the end of PUSH DestOnly statement");}
                    int imm = Integer.parseInt(immediate.get().getValue());

                    return new PushNode(MOP, Rd, -1, -1, imm);
                }
            }
        }
        return null;
    }

    /**
     * Method to run when LOAD operation is indicated
     * @return a LoadNode
     * @throws Exception 
     */
    private LoadNode Load() throws Exception{
        // Double check that LOAD is the operation
        if (tokenManager.MatchAndRemove(Token.TokenType.LOAD).isPresent()){
            Optional<Token> token = tokenManager.Peek(0);
            if (token.isPresent()){
                // expecting either 1, 2, or 3 registers to follow
                // Get the Rd register
                Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (RdToken.isEmpty()){throw new Exception("Expecting a Register after LOAD");}
                int Rd = Integer.parseInt(RdToken.get().getValue());

                // if the Rs1 register, then either 2R or 3R
                Optional<Token> Rs1Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (Rs1Token.isPresent()){
                    int Rs1 = Integer.parseInt(Rs1Token.get().getValue());

                    // If Rs2 register exists, get int value, otherwise -1
                    Optional<Token> Rs2Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                    int Rs2 = -1;
                    if (Rs2Token.isPresent()){ // if present, then 3R
                        Rs2 = Integer.parseInt(Rs2Token.get().getValue());

                        return new LoadNode(Rd, Rs1, Rs2, 0);
                    }
                    else{   // else 2R
                        // get immediate value
                        Optional<Token> immediate = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                        if (immediate.isEmpty()){throw new Exception("Expecting an Immediate at the end of LOAD 2R statement");}
                        int imm = Integer.parseInt(immediate.get().getValue());

                        return new LoadNode(Rd, Rs1, Rs2, imm);
                    }
                }
                else{   // else, DestOnly and get immediate
                    // get immediate value
                    Optional<Token> immediate = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                    if (immediate.isEmpty()){throw new Exception("Expecting an Immediate at the end of LOAD DestOnly statement");}
                    int imm = Integer.parseInt(immediate.get().getValue());

                    return new LoadNode(Rd, -1, -1, imm);
                }
            }
        }
        return null;
    }

    /**
     * Method to run when STORE operation is indicated
     * @return a StoreNode
     * @throws Exception 
     */
    private StoreNode Store() throws Exception{
        // Double check that STORE is the operation
        if (tokenManager.MatchAndRemove(Token.TokenType.STORE).isPresent()){
            Optional<Token> token = tokenManager.Peek(0);
            if (token.isPresent()){
                // expecting either 1, 2, or 3 registers to follow
                // Get the Rd register
                Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (RdToken.isEmpty()){throw new Exception("Expecting a Register after STORE");}
                int Rd = Integer.parseInt(RdToken.get().getValue());

                // if the Rs1 register, then either 2R or 3R
                Optional<Token> Rs1Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (Rs1Token.isPresent()){
                    int Rs1 = Integer.parseInt(Rs1Token.get().getValue());

                    // If Rs2 register exists, get int value, otherwise -1
                    Optional<Token> Rs2Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                    int Rs2 = -1;
                    if (Rs2Token.isPresent()){ // if present, then 3R
                        Rs2 = Integer.parseInt(Rs2Token.get().getValue());

                        return new StoreNode(Rd, Rs1, Rs2, 0);
                    }
                    else{   // else 2R
                        // get immediate value
                        Optional<Token> immediate = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                        if (immediate.isEmpty()){throw new Exception("Expecting an Immediate at the end of STORE 2R statement");}
                        int imm = Integer.parseInt(immediate.get().getValue());

                        return new StoreNode(Rd, Rs1, Rs2, imm);
                    }
                }
                else{   // else, DestOnly and get immediate
                    // get immediate value
                    Optional<Token> immediate = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                    if (immediate.isEmpty()){throw new Exception("Expecting an Immediate at the end of STORE DestOnly statement");}
                    int imm = Integer.parseInt(immediate.get().getValue());

                    return new StoreNode(Rd, -1, -1, imm);
                }
            }
        }
        return null;
    }

    /**
     * Method to run when PEEK operation is indicated
     * @return a PeekNode
     * @throws Exception 
     */
    private PeekNode Peek() throws Exception{
        // Double check that PEEK is the operation
        if (tokenManager.MatchAndRemove(Token.TokenType.PEEK).isPresent()){
            Optional<Token> token = tokenManager.Peek(0);
            if (token.isPresent()){
                // expecting either 2 or 3 registers to follow
                // Get the Rd register
                Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (RdToken.isEmpty()){throw new Exception("Expecting a Register after PEEK");}
                int Rd = Integer.parseInt(RdToken.get().getValue());

                // if the Rs1 register, then either 2R or 3R
                Optional<Token> Rs1Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (Rs1Token.isEmpty()){throw new Exception("Expecting Register after PEEK Rd");}
                int Rs1 = Integer.parseInt(Rs1Token.get().getValue());

                // If Rs2 register exists, get int value, otherwise -1
                Optional<Token> Rs2Token = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                int Rs2 = -1;
                if (Rs2Token.isPresent()){ // if present, then 3R
                    Rs2 = Integer.parseInt(Rs2Token.get().getValue());

                    return new PeekNode(Rd, Rs1, Rs2, 0);
                }
                else{   // else 2R
                    // get immediate value
                    Optional<Token> immediate = tokenManager.MatchAndRemove(Token.TokenType.NUMBER);
                    if (immediate.isEmpty()){throw new Exception("Expecting an Immediate at the end of PEEK 2R statement");}
                    int imm = Integer.parseInt(immediate.get().getValue());

                    return new PeekNode(Rd, Rs1, Rs2, imm);
                }
            }
        }
        return null;
    }

    /**
     * Method to run when POP operation is indicated
     * @return a PopNode
     * @throws Exception 
     */
    private PopNode Pop() throws Exception{
        // Double check that POP is the operation
        if (tokenManager.MatchAndRemove(Token.TokenType.POP).isPresent()){
            Optional<Token> token = tokenManager.Peek(0);
            if (token.isPresent()){
                // expecting either 2 or 3 registers to follow
                // Get the Rd register
                Optional<Token> RdToken = tokenManager.MatchAndRemove(Token.TokenType.REGISTER);
                if (RdToken.isEmpty()){throw new Exception("Expecting a Register after POP");}
                int Rd = Integer.parseInt(RdToken.get().getValue());

                return new PopNode(Rd);
            }
        }
        return null;
    }
}