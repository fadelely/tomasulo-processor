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

	public ArrayList<String> parseTextFile() throws IOException {
		File instructionList = new File("./resources/instructions.txt");
		BufferedReader br = new BufferedReader(new FileReader(instructionList));
		String str;
		ArrayList<String> instructions = new ArrayList<String>(); // these are all the instructions, not yet executed :)
		// puts all the instructions in an array, so we can branch easily later
		while ((str = br.readLine()) != null) {
			instructions.add(str);
		}
		br.close();
		return instructions;
	}

	// addition or subtraction really
	boolean isAdditionOperation(String opcode) {
		// why hashset one might ask
		// malakash da3awa
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("ADD.D");
		possibleOperations.add("ADD.S");
		possibleOperations.add("SUB.D");
		possibleOperations.add("SUB.S");
		possibleOperations.add("SUB.S");
		possibleOperations.add("SUB.S");
//		possibleOperations.add("MUL.D");
//		possibleOperations.add("MUL.S");
//		possibleOperations.add("DIV.D");
//		possibleOperations.add("DIV.S");
		return possibleOperations.contains(opcode);

	}

	// division or multiplication
	boolean isMultiplyOperation(String opcode) {
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("MUL.D");
		possibleOperations.add("MUL.S");
		possibleOperations.add("DIV.D");
		possibleOperations.add("DIV.S");
		return possibleOperations.contains(opcode);

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

	boolean isALUOperation(String opcode) {
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("ADD.D");
		possibleOperations.add("ADD.S");
		possibleOperations.add("SUB.D");
		possibleOperations.add("SUB.S");
		possibleOperations.add("MUL.D");
		possibleOperations.add("MUL.S");
		possibleOperations.add("DIV.D");
		possibleOperations.add("DIV.S");
		return possibleOperations.contains(opcode);
	}

}
