package processor.tomasulo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ParseText
{

	public ArrayList<Instructon> parseTextFile() throws IOException
	{
		File instructionList = new File("./resources/instructions.txt");
		BufferedReader br = new BufferedReader(new FileReader(instructionList));
		String str;
		ArrayList<Instructon> instructions = new ArrayList<>(); // these are all the instructions, not yet executed :)
		// puts all the instructions in an array, so we can branch easily later
		while ((str = br.readLine()) != null)
		{
			instructions.add(new Instructon(str, false));
		}
		br.close();

		return instructions;
	}

	// addition or subtraction really
	boolean isFloatOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("ADD.D");
		possibleOperations.add("ADD.S");
		possibleOperations.add("SUB.D");
		possibleOperations.add("SUB.S");
		return possibleOperations.contains(opcode);

	}
	boolean isFloatAdditionOperation(String opcode)
	{
		// why hashset one might ask
		// malakash da3awa
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("ADD.D");
		possibleOperations.add("ADD.S");
		return possibleOperations.contains(opcode);

	}
	boolean isFloatSubtractionOperation(String opcode)
	{
		// why hashset one might ask
		// malakash da3awa
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("SUB.D");
		possibleOperations.add("SUB.S");
		return possibleOperations.contains(opcode);

	}

	boolean isIntegerOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("DADDI");
		possibleOperations.add("DSUBI");
		return possibleOperations.contains(opcode);
	}

	boolean isIntegerAdditionOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("DADDI");
		return possibleOperations.contains(opcode);

	}
	boolean isIntegerSubtractionOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("DSUBI");
		return possibleOperations.contains(opcode);

	}

	// division or multiplication
	boolean isMultiplyOrDivideOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("MUL.D");
		possibleOperations.add("MUL.S");
		possibleOperations.add("DIV.D");
		possibleOperations.add("DIV.S");
		return possibleOperations.contains(opcode);
	}

	boolean isMultiplyOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("MUL.D");
		possibleOperations.add("MUL.S");
		return possibleOperations.contains(opcode);

	}
	boolean isDivideOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("DIV.D");
		possibleOperations.add("DIV.S");
		return possibleOperations.contains(opcode);

	}

	boolean isLoadOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("LD");
		possibleOperations.add("LW");
		possibleOperations.add("L.D");
		possibleOperations.add("L.S");
		return possibleOperations.contains(opcode);

	}

	boolean isStoreOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("SD");
		possibleOperations.add("SW");
		possibleOperations.add("S.D");
		possibleOperations.add("S.S");
		return possibleOperations.contains(opcode);

	}

	boolean isFloatStoreOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("S.D");
		possibleOperations.add("S.S");
		return possibleOperations.contains(opcode);

	}

	boolean isIntegerStoreOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("SD");
		possibleOperations.add("SW");
		return possibleOperations.contains(opcode);

	}
	
	boolean isBranchOperation(String opcode)
	{
		Set<String> possibleOperations = new HashSet<String>();
		possibleOperations.add("BEQ");
		possibleOperations.add("BNE");
		return possibleOperations.contains(opcode);
	}

	boolean isALUOperation(String opcode)
	{
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
