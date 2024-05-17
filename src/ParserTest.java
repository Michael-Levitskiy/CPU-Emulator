import static org.junit.Assert.assertEquals;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;
import ASTNodes.*;

public class ParserTest {
    @Test
    void testParse() throws Exception {
        Lexer lexer = new Lexer("Test.txt");
        Parser parser = new Parser(lexer.lex());
        StatementsNode nodes = parser.parse();

        LinkedList<StatementNode> nodesList = nodes.getStatements();

        assertEquals("00000000000000010100000000100001", nodesList.get(0).toString());
        assertEquals("00000000000010000111100001000010", nodesList.get(1).toString());
        assertEquals("00000000000000001011100001000011", nodesList.get(2).toString());
        assertEquals("00000000000100000111100001100010", nodesList.get(3).toString());
        assertEquals("00000000000001010000000000100001", nodesList.get(4).toString());
        assertEquals("00000000000000001000000001000001", nodesList.get(5).toString());
        assertEquals("00000000000010001011010001100010", nodesList.get(6).toString());
        assertEquals("00000000000010001111110010000010", nodesList.get(7).toString());
        assertEquals("00000000000000111100000000100001", nodesList.get(8).toString());
        assertEquals("00000000000000001100000001000001", nodesList.get(9).toString());
        assertEquals("00000000000010001011000001100010", nodesList.get(10).toString());
        assertEquals("00000000000000001001110001100011", nodesList.get(11).toString());
        assertEquals("00000000000000000110000001100011", nodesList.get(12).toString());
        assertEquals("00000000010111101000000001000001", nodesList.get(13).toString());
        assertEquals("00000000111000010000000001100001", nodesList.get(14).toString());
        assertEquals("00000000000000001010110000100010", nodesList.get(15).toString());
        assertEquals("00000000000010001010100010000010", nodesList.get(16).toString());
        assertEquals("00000000000000001110010010000011", nodesList.get(17).toString());
        assertEquals("00000000000000000100000000100001", nodesList.get(18).toString());
        assertEquals("00000000000000010100000001000001", nodesList.get(19).toString());
        assertEquals("00000000000000000100000001100001", nodesList.get(20).toString());
        assertEquals("00000000001000001001010000100111", nodesList.get(21).toString());
        assertEquals("00000000000000001001110001100011", nodesList.get(22).toString());
        assertEquals("00000000000000000111110001000011", nodesList.get(23).toString());
        assertEquals("11111111111111110000000000000101", nodesList.get(24).toString());
        assertEquals("00000000000001010000000000100001", nodesList.get(25).toString());
        assertEquals("00000000000011001000000001000001", nodesList.get(26).toString());
        assertEquals("00000000000101101000000001100001", nodesList.get(27).toString());
        assertEquals("00000010001100000100000001110111", nodesList.get(28).toString());
        assertEquals("00000000000010001000000001110110", nodesList.get(29).toString());
        assertEquals("00000000000000111100000001110101", nodesList.get(30).toString());
        assertEquals("00000010001100001100000010010011", nodesList.get(31).toString());
        assertEquals("00000000000110000100000010110010", nodesList.get(32).toString());
        assertEquals("00000000000001010000000001110001", nodesList.get(33).toString());
        assertEquals("00000000000000000000000110100100", nodesList.get(34).toString());
        assertEquals("00000000000000000100000000100001", nodesList.get(35).toString());
        assertEquals("00000000000000010100000001000001", nodesList.get(36).toString());
        assertEquals("00000000011010000001010000101011", nodesList.get(37).toString());
        assertEquals("00001011000000000100010001001010", nodesList.get(38).toString());
        assertEquals("00000000000000101100000001001001", nodesList.get(39).toString());
        assertEquals("00000000000000000000001000001000", nodesList.get(40).toString());
        assertEquals("00000001000010001000000000000110", nodesList.get(41).toString());
        assertEquals("00000000000000001011110000101111", nodesList.get(42).toString());
        assertEquals("00000000000010001001110000001110", nodesList.get(43).toString());
        assertEquals("00000000000000010011100000101101", nodesList.get(44).toString());
        assertEquals("00000000000000001000000001100001", nodesList.get(45).toString());
        assertEquals("00000000000010000000000010011011", nodesList.get(46).toString());
        assertEquals("00000000000000001100000010111010", nodesList.get(47).toString());
        assertEquals("00000000000000000000000011011001", nodesList.get(48).toString());
        assertEquals("00000000000000000000000000000000", nodesList.get(49).toString());
        assertEquals("00000000000000000100000101000001", nodesList.get(50).toString());
        assertEquals("00000000000000101011100000100011", nodesList.get(51).toString());
        assertEquals("00000000000000000000000000010000", nodesList.get(52).toString());
    }
}