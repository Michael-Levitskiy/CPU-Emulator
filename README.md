# CPU-Emulator
This program uses Java code to demonstrate how different parts of a CPU operate and communicate with each other to execute given instructions

## Purpose:
- Learn and understand how a CPU operates
- Practice implementing an Assembler to convert Assembly code into Binary instructions
- Practice writing Unit Tests

## How the Project Works:
- Main exists within the Execute.java file and 2 command line arguments are expected upon execution
  1. The name of the file that contains the assembly code to execute
  2. The name of the file to write the binary instructions to
- These two command line arguments are passed into the Assembler class (Assembler.java)
  - The Assembler calls the Lexer (Lexer.java) and Parser (Parser.java) classes to form an Abstract Syntax Tree (AST)
  - Each Node of the AST is an assembly instruction (or a single line in assembly)
  - Each Node contains an Overridden 'toString()' method that produces the correct 32 bit instruction
  - The Assembler writes to the binary file by calling 'toString()' and writing the result
- After our binary file is filled, the Execute class will read through the binary file and load the instructions into the Main Memory class (MainMemory.java)
- Then the Execute class initializes a Processor variable (Processor.java) and calls the method 'run()' on that variable
  - The 'run()' method in the Processor works by performing a loop of fetching the instruction, decoding the instruction, executing the instruction, and storing the result
  - When performing operations, there are Bit (Bit.java) and Word (Word.java) classes that resemble binary bits and words in which the operations are performed on. The ALU class (ALU.java) uses different Word and Bit operations to complete the math operation given in the instruction
- There are 2 Caches implemented that the Processor uses
  1. L2 Cache (L2Cache.java) : When fetching data, the processor will fetch from the L2 Cache which contains 4 different 8 Word blocks. If the data is in this cache, we return it, but if it's not in this cache, we fetch a new block from Main Memory and return the data.
  2. Instruction Cache (InstructionCache.java) : When fetching instructions, the processor will fetch from the Instruction Cache which contains an 8 Word block. If the instruction is in this cache, we return it, but if it's not, we fetch a new block from the L2 Cache and return the next instruction.
- Upon completion of the 'run()' method, printed to the terminal is the data of the total clock cycles, instruction cache hits and misses, and L2 cache hits and misses.

## Notable Documents
- SIA32.pdf : This document contains the format of the binary instructions, the instruction definitions for the OpCode, and the bit patterns for the math and boolean operations
- AssemblyFormat.pdf : This document contains the format for each assembly instruction for each of the possible OpCode instructions found in the SIA32.pdf document
- CacheReport.pdf : Initially, this project did not contain an Instruction Cache or an L2Cache. This document explains the value of Caches by showing my analysis of the number of clock cycles for 3 different programs to run without any Cache, with the Instruction Cache, and with both the Instruction and L2 Caches.