import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Assembler {
    
    /**
     * Constructor that performs the operations to assemble the assembly into binary
     * @param assemblyFileName (String)  : name of the file with assembly
     * @param binaryFileName (String) : name of the file to be filled with binary
     * @throws Exception
     */
    public Assembler(String assemblyFileName, String binaryFileName) throws Exception {
        // lex and parse the assembly source file
        Lexer lexer = new Lexer(assemblyFileName);
        Parser parser = new Parser(lexer.lex());
        StatementsNode nodes = parser.parse();

        LinkedList<StatementNode> nodesList = nodes.getStatements();

        // write to the binary file by calling toString on all of the nodes in the list
        try{
            File outputFile = new File(binaryFileName);
            FileWriter fw = new FileWriter(outputFile);

            for (StatementNode s : nodesList){
                fw.write(s.toString() + "\n");
            }

            fw.close();
        }
        catch(IOException e){
            System.err.println("There was an error writing to a file");
        }
    }
}