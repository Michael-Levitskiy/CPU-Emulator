import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CodeHandlerTest {
    /**
     * tests peek(int) method
     * @throws Exception 
     */
    @Test
    void testPeek() throws Exception {
        CodeHandler codeHandler = new CodeHandler("Test.txt");
        
        char x = codeHandler.Peek(0);
        assertEquals('C', x);

        x = codeHandler.Peek(6);
        assertEquals('1', x);
        
        x = codeHandler.Peek(9);
        assertEquals('\r', x);

        x = codeHandler.Peek(41);
        assertEquals(' ', x);

        x = codeHandler.Peek(61);
        assertEquals('R', x);

        x = codeHandler.Peek(115);
        assertEquals('\r', x);

        x = codeHandler.Peek(130);
        assertEquals('1', x);

        x = codeHandler.Peek(137);
        assertEquals('\n', x);
    }
    
    /**
     * tests peekString(int) method
     */
    @Test
    void testPeekString() {
        CodeHandler codeHandler = new CodeHandler("Test.txt");

        String nextChars = codeHandler.PeekString(5);
        assertEquals("COPY ", nextChars);

        codeHandler.Swallow(30);
        nextChars = codeHandler.PeekString(10);
        assertEquals("MATH ADD R", nextChars);

        codeHandler.Swallow(30);
        nextChars = codeHandler.PeekString(4);
        assertEquals(" R1\r", nextChars);

        codeHandler.Swallow(50);
        nextChars = codeHandler.PeekString(13);
        assertEquals("R1 R2\r\nMATH S", nextChars);
    }
    
    /**
     * tests getChar() method
     */
    @Test
    void testGetChar() {
        CodeHandler codeHandler = new CodeHandler("Test.txt");

        char c = codeHandler.GetChar();
        int i = codeHandler.getIndex();
        assertEquals('C', c);
        assertEquals(1, i);

        c = codeHandler.GetChar();
        i = codeHandler.getIndex();
        assertEquals('O', c);
        assertEquals(2, i);

        codeHandler.Swallow(110);
        c = codeHandler.GetChar();
        i = codeHandler.getIndex();
        assertEquals(' ', c);
        assertEquals(113, i);
    }

    /**
     * tests swallow(int) and getIndex() methods
     */
    @Test
    void testSwallow() {
        CodeHandler codeHandler = new CodeHandler("Test.txt");

        int x = codeHandler.getIndex();
        assertEquals(0, x);

        codeHandler.Swallow(10);
        x = codeHandler.getIndex();
        assertEquals(10, x);

        codeHandler.Swallow(5);
        x = codeHandler.getIndex();
        assertEquals(15, x);

        codeHandler.Swallow(90);
        x = codeHandler.getIndex();
        assertEquals(105, x);

        codeHandler.Swallow(0);
        x = codeHandler.getIndex();
        assertEquals(105, x);

        codeHandler.Swallow(1);
        x = codeHandler.getIndex();
        assertEquals(106, x);
    }

    /**
     * tests isDone() method
     */
    @Test
    void testIsDone() {
        CodeHandler codeHandler = new CodeHandler("Test.txt");

        assertFalse(codeHandler.IsDone());

        codeHandler.Swallow(100);
        assertFalse(codeHandler.IsDone());

        codeHandler.Swallow(681);
        assertTrue(codeHandler.IsDone());
    }

    /**
     * tests remainder() method
     */
    @Test
    void testRemainder() {
        CodeHandler codeHandler = new CodeHandler("Test.txt");

        String remainder = codeHandler.Remainder();
        assertEquals("COPY R1 5\r\n" +
                        "MATH ADD R2 R1 R1\r\n" +
                        "MATH ADD R2 R2\r\n" +
                        "MATH ADD R3 R2 R1\r\n" +
                        "\r\n" +
                        "COPY R1 20\r\n" +
                        "COPY R2 2\r\n" +
                        "MATH SHIFT RIGHT R3 R1 R2\r\n" +
                        "MATH SUB R4 R1 R3\r\n" +
                        "\r\n" +
                        "COPY R1 15\r\n" +
                        "COPY R2 3\r\n" +
                        "MATH SHIFT LEFT R3 R1 R2\r\n" +
                        "MATH MULT R3 R2\r\n" +
                        "MATH AND R3 R1\r\n" +
                        "\r\n" +
                        "COPY R2 378\r\n" +
                        "COPY R3 900\r\n" +
                        "MATH NOT R1 R0 R2\r\n" +
                        "MATH XOR R4 R1 R2\r\n" +
                        "MATH OR R4 R3\r\n" +
                        "\r\n" +
                        "COPY R1 1\r\n" +
                        "COPY R2 5\r\n" +
                        "COPY R3 1\r\n" +
                        "BRANCH LE R2 R1 4\r\n" +
                        "MATH MULT R3 R2\r\n" +
                        "MATH SUB R2 R1\r\n" +
                        "JUMP R0 -4\r\n" +
                        "\r\n" +
                        "COPY R1 20\r\n" +
                        "COPY R2 50\r\n" +
                        "COPY R3 90\r\n" +
                        "STORE R3 R1 70\r\n" +
                        "STORE R3 R1 R2\r\n" +
                        "STORE R3 15\r\n" +
                        "LOAD R4 R3 70\r\n" +
                        "LOAD R5 R3 R1\r\n" +
                        "LOAD R3 20\r\n" +
                        "JUMP 13\r\n" +
                        "\r\n" +
                        "COPY R1 1\r\n" +
                        "COPY R2 5\r\n" +
                        "CALL LE R0 R1 13\r\n" +
                        "CALL NEQ R0 R2 R1 11\r\n" +
                        "CALL R2 11\r\n" +
                        "CALL 16\r\n" +
                        "BRANCH EQ R1 R0 R2 1\r\n" +
                        "PUSH SUB R1 R2\r\n" +
                        "PUSH MULT R0 R1 R2\r\n" +
                        "PUSH ADD R1 4\r\n" +
                        "COPY R3 2\r\n" +
                        "PEEK R4 R0 1\r\n" +
                        "PEEK R5 R0 R3\r\n" +
                        "POP R6\r\n" +
                        "HALT\r\n" +
                        "COPY R10 1\r\n" +
                        "MATH ADD R1 R10\r\n" +
                        "RETURN",   remainder);

        codeHandler.Swallow(50);
        remainder = codeHandler.Remainder();
        assertEquals(" ADD R3 R2 R1\r\n" +
                        "\r\n" +
                        "COPY R1 20\r\n" +
                        "COPY R2 2\r\n" +
                        "MATH SHIFT RIGHT R3 R1 R2\r\n" +
                        "MATH SUB R4 R1 R3\r\n" +
                        "\r\n" +
                        "COPY R1 15\r\n" +
                        "COPY R2 3\r\n" +
                        "MATH SHIFT LEFT R3 R1 R2\r\n" +
                        "MATH MULT R3 R2\r\n" +
                        "MATH AND R3 R1\r\n" +
                        "\r\n" +
                        "COPY R2 378\r\n" +
                        "COPY R3 900\r\n" +
                        "MATH NOT R1 R0 R2\r\n" +
                        "MATH XOR R4 R1 R2\r\n" +
                        "MATH OR R4 R3\r\n" +
                        "\r\n" +
                        "COPY R1 1\r\n" +
                        "COPY R2 5\r\n" +
                        "COPY R3 1\r\n" +
                        "BRANCH LE R2 R1 4\r\n" +
                        "MATH MULT R3 R2\r\n" +
                        "MATH SUB R2 R1\r\n" +
                        "JUMP R0 -4\r\n" +
                        "\r\n" +
                        "COPY R1 20\r\n" +
                        "COPY R2 50\r\n" +
                        "COPY R3 90\r\n" +
                        "STORE R3 R1 70\r\n" +
                        "STORE R3 R1 R2\r\n" +
                        "STORE R3 15\r\n" +
                        "LOAD R4 R3 70\r\n" +
                        "LOAD R5 R3 R1\r\n" +
                        "LOAD R3 20\r\n" +
                        "JUMP 13\r\n" +
                        "\r\n" +
                        "COPY R1 1\r\n" +
                        "COPY R2 5\r\n" +
                        "CALL LE R0 R1 13\r\n" +
                        "CALL NEQ R0 R2 R1 11\r\n" +
                        "CALL R2 11\r\n" +
                        "CALL 16\r\n" +
                        "BRANCH EQ R1 R0 R2 1\r\n" +
                        "PUSH SUB R1 R2\r\n" +
                        "PUSH MULT R0 R1 R2\r\n" +
                        "PUSH ADD R1 4\r\n" +
                        "COPY R3 2\r\n" +
                        "PEEK R4 R0 1\r\n" +
                        "PEEK R5 R0 R3\r\n" +
                        "POP R6\r\n" +
                        "HALT\r\n" +
                        "COPY R10 1\r\n" +
                        "MATH ADD R1 R10\r\n" +
                        "RETURN",   remainder);

        codeHandler.Swallow(300);
        remainder = codeHandler.Remainder();
        assertEquals("1 4\r\n" +
                        "MATH MULT R3 R2\r\n" +
                        "MATH SUB R2 R1\r\n" +
                        "JUMP R0 -4\r\n" +
                        "\r\n" +
                        "COPY R1 20\r\n" +
                        "COPY R2 50\r\n" +
                        "COPY R3 90\r\n" +
                        "STORE R3 R1 70\r\n" +
                        "STORE R3 R1 R2\r\n" +
                        "STORE R3 15\r\n" +
                        "LOAD R4 R3 70\r\n" +
                        "LOAD R5 R3 R1\r\n" +
                        "LOAD R3 20\r\n" +
                        "JUMP 13\r\n" +
                        "\r\n" +
                        "COPY R1 1\r\n" +
                        "COPY R2 5\r\n" +
                        "CALL LE R0 R1 13\r\n" +
                        "CALL NEQ R0 R2 R1 11\r\n" +
                        "CALL R2 11\r\n" +
                        "CALL 16\r\n" +
                        "BRANCH EQ R1 R0 R2 1\r\n" +
                        "PUSH SUB R1 R2\r\n" +
                        "PUSH MULT R0 R1 R2\r\n" +
                        "PUSH ADD R1 4\r\n" +
                        "COPY R3 2\r\n" +
                        "PEEK R4 R0 1\r\n" +
                        "PEEK R5 R0 R3\r\n" +
                        "POP R6\r\n" +
                        "HALT\r\n" +
                        "COPY R10 1\r\n" +
                        "MATH ADD R1 R10\r\n" +
                        "RETURN", remainder);

        codeHandler.Swallow(400);
        remainder = codeHandler.Remainder();
        assertEquals(" R10 1\r\n" +
                        "MATH ADD R1 R10\r\n" +
                        "RETURN",   remainder);
    }
}