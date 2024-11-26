package processor.tomasulo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;

public class ParseText {
	static RegisterFile registerFile = new RegisterFile();
	static Memory memory = new Memory();

	void parseTextFile() throws IOException {
		Memory.storeWord(0, 5000);
		Memory.storeWord(4, 5000);
		File instructions = new File("./resources/instructions.txt");
		BufferedReader br = new BufferedReader(new FileReader(instructions));
		String str;
		while ((str = br.readLine()) != null) {
			String regex = "[ ,]+";
			String[] parsedInstruction = str.split(regex);
			String OPCode = parsedInstruction[0];
			String R1 = parsedInstruction[1]; // string as this is where we will save our result
			if (isALUOperation(OPCode)) {
				double R2 = RegisterFile.readRegister(parsedInstruction[2]);
				double R3 = RegisterFile.readRegister(parsedInstruction[3]);
				switch (OPCode) {
				case "ADD.D":
					addFloating(R1, R2, R3, false);
					break;
				case "SUB.D":
					subtractFloating(R1, R2, R3, false);
					break;
				case "MUL.D":
					multiplyFloating(R1, R2, R3, false);
					break;
				case "DIV.D":
					divideFloating(R1, R2, R3, false);
					break;
				case "ADD.S":
					addFloating(R1, R2, R3, true);
					break;
				case "SUB.S":
					subtractFloating(R1, R2, R3, true);
					break;
				case "MUL.S":
					multiplyFloating(R1, R2, R3, true);
					break;
				case "DIV.S":
					divideFloating(R1, R2, R3, true);
					break;
				}

			} else if (isMemoryOperation(OPCode)) {
				// logically, it should be long, since the memory is 64 bits, but a limitation of java
				// is that arrays can only be addressed max by 2^32 - 1 numbers, or an int only, 
				int memoryAddress = Integer.parseInt(parsedInstruction[2]);
				switch (OPCode) {
				case "L.S":
					float wordValue  = Memory.loadWord(memoryAddress);
					RegisterFile.writeRegister(R1, wordValue );
					break;
				case "LW":
					float integerWordValue = Memory.loadWord(memoryAddress);
					RegisterFile.writeRegister(R1, integerWordValue);
					break;
				case "L.D":
					double doubleWordValue = Memory.loadDoubleWord(memoryAddress);
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
					break;
				case "SD":
					long longRegisterValue = (long) RegisterFile.readRegister(R1);
					Memory.storeDoubleWord(memoryAddress, longRegisterValue);
					break;
				case "S.D":
					break;
					
				}

			} else {
				switch (OPCode) {
				case "BNEQ":
					break;
				case "BEQZ":
					break;
				case "ADDI":
					break;
				case "SUBI":
					break;
				}
			}

		}
		br.close();
		System.out.println(RegisterFile.readRegister("F1"));
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

	void addFloating(String F1, double F2, double F3, boolean single) {
		if (single) {
			System.out.println("Adding single values " + F2 + " + " + F3 + " and saving onto " + F1);
			float result = (float) (F2 + F3);
			RegisterFile.writeRegister(F1, result);
		} else {
			System.out.println("Adding double values " + F2 + " + " + F3 + " and saving onto " + F1);
			double result = F2 + F3;
			RegisterFile.writeRegister(F1, result);
		}
	}

	void subtractFloating(String F1, double F2, double F3, boolean single) {
		if (single) {
			System.out.println("Subtracting single values " + F2 + " + " + F3 + " and saving onto " + F1);
			float result = (float) (F2 - F3);
			RegisterFile.writeRegister(F1, result);
		} else {
			System.out.println("Subtracting double values " + F2 + " + " + F3 + " and saving onto " + F1);
			double result = F2 - F3;
			RegisterFile.writeRegister(F1, result);
		}
	}

	void multiplyFloating(String F1, double F2, double F3, boolean single) {
		if (single) {
			System.out.println("Multiplying single values " + F2 + " + " + F3 + " and saving onto " + F1);
			float result = (float) (F2 * F3);
			RegisterFile.writeRegister(F1, result);
		} else {
			System.out.println("Multiplying double values " + F2 + " + " + F3 + " and saving onto " + F1);
			double result = F2 * F3;
			RegisterFile.writeRegister(F1, result);
		}
	}

	void divideFloating(String F1, double F2, double F3, boolean single) {
		if (single) {
			System.out.println("Dividing single values " + F2 + " + " + F3 + " and saving onto " + F1);
			float result = (float) (F2 / F3);
			RegisterFile.writeRegister(F1, result);
		} else {
			System.out.println("Dividing double values " + F2 + " + " + F3 + " and saving onto " + F1);
			double result = F2 / F3;
			RegisterFile.writeRegister(F1, result);
		}
	}

}
