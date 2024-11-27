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

	static FloatingRegister floatingRegisters[];
	static IntegerRegister integerRegisters[];

	public RegisterFile() {
		floatingRegisters = new FloatingRegister[32];
		for (FloatingRegister reg : floatingRegisters)
			reg = new FloatingRegister();

		integerRegisters = new IntegerRegister[32];
		for (IntegerRegister reg : integerRegisters)
			reg = new IntegerRegister();
	}

	public static double readRegister(String register) {
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
		case 'R':
			IntegerRegister intReg = RegisterFile.integerRegisters[registerNumber];
			if (intReg.Qi == 0) {
				long registerValue = intReg.value;
				System.out.println("Reading register " + register + ", value is: " + registerValue);
				return registerValue;
			} else {
				// return tag
			}
		case 'F':
			FloatingRegister floatReg = RegisterFile.floatingRegisters[registerNumber];
			if (floatReg.Qi == 0) {
				double floatingRegisterValue = floatReg.value;
				System.out.println("Reading register " + register + ", value is: " + floatingRegisterValue);
				return floatingRegisterValue;
			} else {
				// return tag
			}
		}
		return -1;
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
}
