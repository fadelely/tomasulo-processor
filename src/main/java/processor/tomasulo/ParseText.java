package processor.tomasulo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ParseText {
	static RegisterFile RegisterFile = new RegisterFile();
	static Memory memory = new Memory();

	void parseTextFile() throws IOException {
		File instructions = new File("./resources/instructions.txt");
		BufferedReader br = new BufferedReader(new FileReader(instructions));
		String str;
		while ((str = br.readLine()) != null) {
			RegisterFile.floatingRegisters[2] = 5.2;
			RegisterFile.floatingRegisters[3] = 5.2;
			String regex = " ";
			String[] parsedInstruction = str.split(regex);
			String R1 = parsedInstruction[1]; // string as this is where we will save our result
			double R2 = RegisterFile.readRegister(parsedInstruction[2]);
			double R3 = RegisterFile.readRegister(parsedInstruction[3]);
			switch (parsedInstruction[0]) {
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

			case "BNEQ":
				break;
			case "BEQZ":
				break;
			case "ADDI":
				break;
			case "SUBI":
				break;

			case "L.D":
				break;
			case "L.S":
				break;

			case "LD":
				break;
			case "LW":
				break;
			}

		}
		br.close();
	}


	void addFloating(String F1, double F2, double F3, boolean single) {
		if (single) {
			float result = (float) (F2 + F3);
			RegisterFile.writeRegister(F1, result);
		} else {
			double result = F2 + F3;
			RegisterFile.writeRegister(F1, result);
		}
	}

	void subtractFloating(String F1, double F2, double F3, boolean single) {
		if (single) {
			float result = (float) (F2 - F3);
			RegisterFile.writeRegister(F1, result);
		} else {
			double result = F2 - F3;
			RegisterFile.writeRegister(F1, result);
		}
	}

	void multiplyFloating(String F1, double F2, double F3, boolean single) {
		if (single) {
			float result = (float) (F2 * F3);
			RegisterFile.writeRegister(F1, result);
		} else {
			double result = F2 * F3;
			RegisterFile.writeRegister(F1, result);
		}
	}

	void divideFloating(String F1, double F2, double F3, boolean single) {
		if (single) {
			float result = (float) (F2 / F3);
			RegisterFile.writeRegister(F1, result);
		} else {
			double result = F2 / F3;
			RegisterFile.writeRegister(F1, result);
		}
	}

}
