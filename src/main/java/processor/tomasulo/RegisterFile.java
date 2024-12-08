package processor.tomasulo;

public class RegisterFile {

	public class FloatingRegister {
		public int Qi;
		public double value;

		public FloatingRegister() {
			Qi = 0;
			value = 0;
		}

	}

	public class IntegerRegister {
		public int Qi;
		public long value;

		public IntegerRegister() {
			Qi = 0;
			value = 0;
		}

	}

	// since the read register can return either a floating register or integer
	// register, and we don't know which will be
	// returned, we use this to hold both return types and use the one we want
	public class ReturnedRegister {

	}

	static FloatingRegister floatingRegisters[];
	static IntegerRegister integerRegisters[];

	public RegisterFile() {
		floatingRegisters = new FloatingRegister[32];
		for(int i = 0;i<floatingRegisters.length;i++)
			floatingRegisters[i] = new FloatingRegister();

		integerRegisters = new IntegerRegister[32];
		for(int i = 0;i<integerRegisters.length;i++)
			integerRegisters[i] = new IntegerRegister();
	}

	public static IntegerRegister readIntegerRegister(String register) {
		int registerNumber = register.charAt(1) - '0';
		IntegerRegister intReg = RegisterFile.integerRegisters[registerNumber];
		System.out.println("Reading register " + register + ", value is: " + intReg.value + ", and q is " + intReg.Qi);
		return intReg;
	}

	public static FloatingRegister readFloatRegister(String register) {
		int registerNumber = register.charAt(1) - '0';
		FloatingRegister floatReg = RegisterFile.floatingRegisters[registerNumber];
		System.out.println("Reading register " + register + ", value is: " + floatReg.value + ", and q is " + floatReg.Qi);
		return floatReg;

	}

	public static void writeRegister(String register, double value, int tag) {
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
		case 'R':
			IntegerRegister intReg = RegisterFile.integerRegisters[registerNumber];
			if (intReg.Qi == tag) {
				System.out.println("Writing value " + value + " onto register " + register);
				intReg.value = (long) value;
			}
			// else do nothing, someone else will provide the value

		case 'F':
			FloatingRegister floatReg = RegisterFile.floatingRegisters[registerNumber];
			if (floatReg.Qi == tag) {
				System.out.println("Writing value " + value + " onto register " + register);
				floatReg.value = value;
			}
			// else do nothing, someone else will provide the value
		}

	}

	public static void writeTagToRegisterFile(String register, int tag)
	{
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
			case 'R':
				IntegerRegister intReg = RegisterFile.integerRegisters[registerNumber];
				intReg.Qi = tag;

			case 'F':
				FloatingRegister floatReg = RegisterFile.floatingRegisters[registerNumber];
				floatReg.Qi = tag;
		}

	}
}
