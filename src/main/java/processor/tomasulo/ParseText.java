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
		RegisterFile.integerRegisters [2]= -923274;
		storeWord(0, "R2", true);
		loadWord(0, "R3", true);
		System.out.println("R3 is " + readRegister("R3"));
		for(int i = 0;i< 5;i++)
			System.out.println("Memory " + i + " is " + Memory.addresses[i]);

		File instructions = new File("./resources/instructions.txt");
		BufferedReader br = new BufferedReader(new FileReader(instructions));
		String str;
		while ((str = br.readLine()) != null) {
			RegisterFile.floatingRegisters[2] = 5.2;
			RegisterFile.floatingRegisters[3] = 5.2;
			String regex = " ";
			String[] parsedInstruction = str.split(regex);
			int R1 = (int) readRegister(parsedInstruction[1]);
			double R2 = readRegister(parsedInstruction[2]);
			double R3 = readRegister(parsedInstruction[3]);
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
	}

	double readRegister(String register) {
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
		case 'R':
			return RegisterFile.integerRegisters[registerNumber];
		case 'F':
			return RegisterFile.floatingRegisters[registerNumber];
		}
		return -1;

	}
	void writeRegister(String register, double value)
	{
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
		case 'R':
			RegisterFile.integerRegisters[registerNumber] = (int)value;
		case 'F':
			RegisterFile.floatingRegisters[registerNumber] = value;
		}
		
	}

	void addFloating(int R1, double R2, double R3, boolean single) {
		RegisterFile.floatingRegisters[R1] = R2 + R3;
	}

	void subtractFloating(int R1, double R2, double R3, boolean single) {
		RegisterFile.floatingRegisters[R1] = R2 - R3;
	}

	void multiplyFloating(int R1, double R2, double R3, boolean single) {
		RegisterFile.floatingRegisters[R1] = R2 * R3;
	}

	void divideFloating(int R1, double R2, double R3, boolean single) {
		RegisterFile.floatingRegisters[R1] = R2 / R3;
	}

	void loadWord(int address, String register, boolean single) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		byte firstByte = Memory.addresses[address];
		byte secondByte = Memory.addresses[address + 1] ;
		byte thirdByte = Memory.addresses[address + 2];
		byte fourthByte = Memory.addresses[address + 3];
		byteBuffer.put(firstByte);
		byteBuffer.put(secondByte);
		byteBuffer.put(thirdByte);
		byteBuffer.put(fourthByte);
		int word = byteBuffer.getInt(0);

		writeRegister(register, word);
		if (!single) {
			byte fifthByte = Memory.addresses[address + 4];
			byte sixthByte = Memory.addresses[address + 5];
			byte seventhByte = Memory.addresses[address + 6];
			byte eighthByte = Memory.addresses[address + 7];
		}
	}
	
	void storeWord(int address, String register, boolean single)
	{
		int registerValue = (int)readRegister(register);
		// xFF means the LSB are all 1s, and anything after that is 0
		Memory.addresses[address] = (byte) (registerValue  & 0xFF);
		Memory.addresses[address + 1] = (byte) ((registerValue >> 8) & 0xFF);
		Memory.addresses[address + 2] = (byte) ((registerValue >> 16) & 0xFF);
		Memory.addresses[address + 3] = (byte) ((registerValue >> 24) & 0xFF);
	}

}
