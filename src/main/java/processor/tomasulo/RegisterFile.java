package processor.tomasulo;

public class RegisterFile {
	static double floatingRegisters[];
	static long integerRegisters[];

	public RegisterFile() {
		floatingRegisters = new double[32];
		integerRegisters = new long[32];
	}

	public static double readRegister(String register) {
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
		case 'R':
			long registerValue = RegisterFile.integerRegisters[registerNumber];
			System.out.println("Reading register " + register + ", value is: " + registerValue);
			return registerValue;
		case 'F':
			double floatingRegisterValue = RegisterFile.floatingRegisters[registerNumber];
			System.out.println("Reading register " + register + ", value is: " + floatingRegisterValue);
			return RegisterFile.floatingRegisters[registerNumber];
		}
		return -2;

	}

	public static void writeRegister(String register, double value) {
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
		case 'R':
			System.out.println("Writing value " + value + " onto register " + register);
			RegisterFile.integerRegisters[registerNumber] = (long) value;
		case 'F':
			System.out.println("Writing value " + value + " onto register " + register);
			RegisterFile.floatingRegisters[registerNumber] = value;
		}

	}
}
