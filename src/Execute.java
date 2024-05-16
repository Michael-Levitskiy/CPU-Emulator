import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Execute {
    public static void main(String[] args) throws Exception {
        // Ensure that there are 2 arguments provided
        if(args.length != 2){
            System.err.println("Provide 2 arguments (assembly source file name and binary output file name)");
            System.exit(0);
        }

        new Assembler(args[0], args[1]);                        // initialize assembler to create binary file
        ArrayList<String> instructions = new ArrayList<>();     // initialize ArrayList to contain binary instructions
        
        // read binary file and add each line to the ArrayList
        try{
            File binaryFile = new File(args[1]);
            Scanner scanner = new Scanner(binaryFile);
            while(scanner.hasNextLine()){
                String instruction = scanner.nextLine();
                instructions.add(instruction);
            }
            scanner.close();
        }
        catch(IOException e){
            System.err.println("There was an error reading from the output file");
        }
        // convert ArrayList<String> to String[] and load into MainMemory
        String[] data = instructions.toArray(new String[instructions.size()]);
        MainMemory.load(data);

        // Initialize the processor and run the instructions
        Processor processor = new Processor();
        processor.run();
    }
}