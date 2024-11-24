package processor.tomasulo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ParseText 
{
	static RegisterFile RegisterFile = new RegisterFile();
	void parseTextFile() throws IOException
	{
		File instructions = new File("./resources/instructions.txt");
		BufferedReader br = new BufferedReader(new FileReader(instructions));
		String str;
		 while ((str = br.readLine()) != null)
		 {
			 RegisterFile.floatingRegisters[2] =5.2; 
			 RegisterFile.floatingRegisters[3] =5.2; 
			 String regex = " ";
			 String[] parsedInstruction = str.split(regex);
			 int R1 = (int) readRegister(parsedInstruction[1]);
			 double R2 = readRegister(parsedInstruction[2]);
			 double R3 = readRegister(parsedInstruction[3]);
			 switch(parsedInstruction[0])
			 {
			 case "ADD.D":
				addFloating(R1,R2,R3,false);
				break;
			 case "SUB.D":
				subtractFloating(R1,R2,R3, false); 
				break;
			 case "MUL.D":
				multiplyFloating(R1,R2,R3, false); 
				break;
			 case "DIV.D":
				divideFloating(R1,R2,R3, false); 
				break;
			 case "ADD.S":
				addFloating(R1,R2,R3, true); 
				break;
			 case "SUB.S":
				subtractFloating(R1,R2,R3, true); 
				break;
			 case "MUL.S":
				multiplyFloating(R1,R2,R3, true); 
				break;
			 case "DIV.S":
				divideFloating(R1,R2,R3, true); 
				break;
				 
			 case "BNEQ":
				 break;
			 case "BEQZ":
				 break;
			 case "ADDI":
				 break;
			 case"SUBI":
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
	
	double readRegister(String register)
	{
		int registerNumber = register.charAt(1) - '0';
		switch(register.charAt(0))
		{
		case 'R':
			return RegisterFile.integerRegisters[registerNumber];
		case 'F':
			return RegisterFile.floatingRegisters[registerNumber];
		}
		return -1;
			
	}
	void addFloating(int R1, double R2, double R3, boolean single)
	{
		RegisterFile.floatingRegisters[R1] = R2+R3; 
	}
	void subtractFloating(int R1, double R2, double R3, boolean single)
	{
		RegisterFile.floatingRegisters[R1] = R2-R3; 
	}
	void multiplyFloating(int R1, double R2, double R3, boolean single)
	{
		RegisterFile.floatingRegisters[R1] = R2*R3; 
	}
	void divideFloating(int R1, double R2, double R3, boolean single)
	{
		RegisterFile.floatingRegisters[R1] = R2/R3; 
	}

}
