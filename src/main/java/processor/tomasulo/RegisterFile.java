package processor.tomasulo;

public class RegisterFile {
	static double floatingRegisters[];
	static long integerRegisters[];
	public RegisterFile()
	{
		floatingRegisters = new double[32];
		integerRegisters = new long[32];
	}

	double readRegister(String register) {
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
		case 'R':
			return (long) RegisterFile.integerRegisters[registerNumber];
		case 'F':
			return RegisterFile.floatingRegisters[registerNumber];
		}
		return -2;

	}

	void writeRegister(String register, double value) {
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
		case 'R':
			RegisterFile.integerRegisters[registerNumber] = (long) value;
		case 'F':
			RegisterFile.floatingRegisters[registerNumber] = value;
		}

	}
}
