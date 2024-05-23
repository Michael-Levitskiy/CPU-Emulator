import static org.junit.Assert.assertEquals;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

public class LexerTest {
    /**
     * tests lex(String) method
     * @throws Exception 
     */
    @Test
    void testLex() throws Exception {
        Lexer lexer = new Lexer("src/Test.txt");
        LinkedList<Token> tokens = lexer.lex();

        assertEquals("COPY() 1-0", tokens.get(0).toString());
        assertEquals("REGISTER(1) 1-5", tokens.get(1).toString());
        assertEquals("NUMBER(5) 1-8", tokens.get(2).toString());
        assertEquals("MATH() 4-0", tokens.get(15).toString());
        assertEquals("REGISTER(2) 7-5", tokens.get(27).toString());
        assertEquals("REGISTER(4) 9-9", tokens.get(39).toString());
        assertEquals("REGISTER(3) 9-15", tokens.get(41).toString());
        assertEquals("REGISTER(1) 11-5", tokens.get(45).toString());
        assertEquals("REGISTER(3) 14-10", tokens.get(61).toString());
        assertEquals("COPY() 17-0", tokens.get(70).toString());
        assertEquals("REGISTER(1) 19-9", tokens.get(80).toString());
        assertEquals("ENDOFLINE() 39-10", tokens.get(168).toString());
        assertEquals("UNEQUAL() 45-5", tokens.get(188).toString());
        assertEquals("RETURN() 59-0", tokens.get(252).toString());
    }
}