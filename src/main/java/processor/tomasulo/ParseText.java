package processor.tomasulo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ParseText {
	static RegisterFile registerFile = new RegisterFile();
	static Memory memory = new Memory();
	static ALU alu = new ALU();

	void parseTextFile() throws IOException {
		Memory.storeSingle(0, 2.56f);
		Memory.storeSingle(4, -2.48f);
		File instructionList = new File("./resources/instructions.txt");
		BufferedReader br = new BufferedReader(new FileReader(instructionList));
		String str;
		// puts all the instructions in an array, so we can branch easily later
		ArrayList<String> instructions = new ArrayList<String>();
		while ((str = br.readLine()) != null) {
			instructions.add(str);
		}
		br.close();
		
		for (int i = 0 ;i< instructions.size() ; i++) {
			String regex = "[ ,]+";
			String[] parsedInstruction = instructions.get(i).split(regex);
			String OPCode = parsedInstruction[0];
			if (isALUOperation(OPCode)) {
				String F1 = parsedInstruction[1]; // string as this is where we will save our result
				double F2 = RegisterFile.readRegister(parsedInstruction[2]);
				double F3 = RegisterFile.readRegister(parsedInstruction[3]);
				switch (OPCode) {
				case "ADD.D":
					ALU.addFloating(F1, F2, F3, false);
					break;
				case "SUB.D":
					ALU.subtractFloating(F1, F2, F3, false);
					break;
				case "MUL.D":
					ALU.multiplyFloating(F1, F2, F3, false);
					break;
				case "DIV.D":
					ALU.divideFloating(F1, F2, F3, false);
					break;
				case "ADD.S":
					ALU.addFloating(F1, F2, F3, true);
					break;
				case "SUB.S":
					ALU.subtractFloating(F1, F2, F3, true);
					break;
				case "MUL.S":
					ALU.multiplyFloating(F1, F2, F3, true);
					break;
				case "DIV.S":
					ALU.divideFloating(F1, F2, F3, true);
					break;
				}

			} else if (isMemoryOperation(OPCode)) {
				// logically, it should be long, since the memory is 64 bits, but a limitation
				// of java
				// is that arrays can only be addressed max by 2^32 - 1 numbers, or an int only,
				String R1 = parsedInstruction[1]; // string as this is where we will save our result
				int memoryAddress = Integer.parseInt(parsedInstruction[2]);
				switch (OPCode) {
				case "L.S":
					float wordValue = Memory.loadSingle(memoryAddress);
					// this weird hack is because precision gets fucked during conversion from float
					// to double
					// techincally gets more precise, but still is not something we wanted
					double convertedWordValue = Double.valueOf(Float.valueOf(wordValue).toString()).doubleValue();
					RegisterFile.writeRegister(R1, convertedWordValue);
					break;
				case "LW":
					float integerWordValue = Memory.loadWord(memoryAddress);
					RegisterFile.writeRegister(R1, integerWordValue);
					break;
				case "L.D":
					double doubleWordValue = Memory.loadDouble(memoryAddress);
					RegisterFile.writeRegister(R1, doubleWordValue);
					break;
				case "LD":
					double integerDoubleWordValue = Memory.loadDoubleWord(memoryAddress);
					RegisterFile.writeRegister(R1, integerDoubleWordValue);
					break;
				case "SW":
					int registerValue = (int) RegisterFile.readRegister(R1);
					Memory.storeWord(memoryAddress, registerValue);
					break;
				case "S.S":
					float floatRegisterValue = (float) RegisterFile.readRegister(R1);
					Memory.storeSingle(memoryAddress, floatRegisterValue);
					break;
				case "SD":
					long longRegisterValue = (long) RegisterFile.readRegister(R1);
					Memory.storeDoubleWord(memoryAddress, longRegisterValue);
					break;
				case "S.D":
					double doubleRegisterValue = (double) RegisterFile.readRegister(R1);
					Memory.storeDouble(memoryAddress, doubleRegisterValue);
					break;

				}

			} else if (OPCode.equals("ADDI") || OPCode.equals("SUBI")) {
				String R1 = parsedInstruction[1]; // string as this is where we will save our result
				long R2 = (long) RegisterFile.readRegister(parsedInstruction[2]);
				short immediate = Short.valueOf(parsedInstruction[3]);
				switch (OPCode) {
				case "ADDI":
					ALU.addImmediate(R1, R2, immediate);
					break;
				case "SUBI":
					ALU.subtractImmediate(R1, R2, immediate);
					break;
				}
			} else {
				long R1 = (long) RegisterFile.readRegister(parsedInstruction[1]);
				switch (OPCode) {
				// branching currently only handles integers; pray we dont need floating
				case "BNEQ":
				long R2 = (long) RegisterFile.readRegister(parsedInstruction[2]);
					if(R1 != R2)
					{
						int instruction =Integer.parseInt(parsedInstruction[3]) ;
						i = instruction - 1; // -1 since we will increment at the end of the loop
						
					}
					break;
				case "BEQZ":
					if(R1 == 0)
					{
						int instruction =Integer.parseInt(parsedInstruction[2]) ;
						i = instruction - 1; // -1 since we will increment at the end of the loop
					}
					break;
				}

			}
		}
	}

	boolean isMemoryOperation(String opcode) {
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("LD");
		possibleOperations.add("LW");
		possibleOperations.add("L.D");
		possibleOperations.add("L.S");
		possibleOperations.add("SD");
		possibleOperations.add("SW");
		possibleOperations.add("S.D");
		possibleOperations.add("S.S");
		return possibleOperations.contains(opcode);

	}

	boolean isALUOperation(String operation) {
		// why hashset one might ask
		// malakash da3awa
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("ADD.D");
		possibleOperations.add("ADD.S");
		possibleOperations.add("SUB.D");
		possibleOperations.add("SUB.S");
		possibleOperations.add("MUL.D");
		possibleOperations.add("MUL.S");
		possibleOperations.add("DIV.D");
		possibleOperations.add("DIV.S");
		return possibleOperations.contains(operation);
	}

}
