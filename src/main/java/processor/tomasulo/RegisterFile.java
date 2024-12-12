package processor.tomasulo;

import javafx.beans.property.*;

public class RegisterFile {

	public class FloatingRegister {
		private final IntegerProperty qi;
		private final DoubleProperty value;
		private final StringProperty registerName; // Register name (F0, F1, F2, ...)

		public FloatingRegister(int index) {
			this.qi = new SimpleIntegerProperty(0);
			this.value = new SimpleDoubleProperty(0.0);
			this.registerName = new SimpleStringProperty("F" + index); // Set the register name
		}

		// Getters
		public int getQi() {
			return qi.get();
		}

		public double getValue() {
			return value.get();
		}

		public String getRegisterName() {
			return registerName.get();
		}

		// Setters
		public void setQi(int qi) {
			this.qi.set(qi);
		}

		public void setValue(double value) {
			this.value.set(value);
		}

		public void setRegisterName(String name) {
			this.registerName.set(name);
		}

		// Properties
		public IntegerProperty qiProperty() {
			return qi;
		}

		public DoubleProperty valueProperty() {
			return value;
		}

		public StringProperty registerNameProperty() {
			return registerName;
		}
	}

	public class IntegerRegister {
		private final IntegerProperty qi;
		private final LongProperty value;
		private final StringProperty registerName; // Register name (R0, R1, R2, ...)

		public IntegerRegister(int index) {
			this.qi = new SimpleIntegerProperty(0);
			this.value = new SimpleLongProperty(0);
			this.registerName = new SimpleStringProperty("R" + index); // Set the register name
		}

		// Getters
		public int getQi() {
			return qi.get();
		}

		public long getValue() {
			return value.get();
		}

		public String getRegisterName() {
			return registerName.get();
		}

		// Setters
		public void setQi(int qi) {
			this.qi.set(qi);
		}

		public void setValue(long value) {
			this.value.set(value);
		}

		public void setRegisterName(String name) {
			this.registerName.set(name);
		}

		// Properties
		public IntegerProperty qiProperty() {
			return qi;
		}

		public LongProperty valueProperty() {
			return value;
		}

		public StringProperty registerNameProperty() {
			return registerName;
		}
	}

	public static FloatingRegister[] floatingRegisters;
	public static IntegerRegister[] integerRegisters;

	public RegisterFile() {
		floatingRegisters = new FloatingRegister[32];
		for (int i = 0; i < floatingRegisters.length; i++) {
			floatingRegisters[i] = new FloatingRegister(i); // Pass index for register name
		}

		integerRegisters = new IntegerRegister[32];
		for (int i = 0; i < integerRegisters.length; i++) {
			integerRegisters[i] = new IntegerRegister(i); // Pass index for register name
		}
	}
	public RegisterFile(FloatingRegister floatingRegisters[], IntegerRegister integerRegisters[]) {

		for(int i = 0; i < floatingRegisters.length; i++) {
			RegisterFile.floatingRegisters[i] = floatingRegisters[i];
		}
		for(int i = 0; i < integerRegisters.length; i++) {
			RegisterFile.integerRegisters[i] = integerRegisters[i];
		}

	}

	public static IntegerRegister readIntegerRegister(String register) {
		int registerNumber = register.charAt(1) - '0';
		IntegerRegister intReg = RegisterFile.integerRegisters[registerNumber];
		System.out.println("Reading register " + register + ", value is: " + intReg.getValue() + ", and q is " + intReg.getQi());
		return intReg;
	}

	public static FloatingRegister readFloatRegister(String register) {
		int registerNumber = register.charAt(1) - '0';
		FloatingRegister floatReg = RegisterFile.floatingRegisters[registerNumber];
		System.out.println("Reading register " + register + ", value is: " + floatReg.getValue() + ", and q is " + floatReg.getQi());
		return floatReg;
	}

	public static void writeRegister(String register, double value, int tag) {
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
			case 'R':
				IntegerRegister intReg = RegisterFile.integerRegisters[registerNumber];
				if (intReg.getQi() == tag) {
					System.out.println("Writing value " + value + " onto register " + register);
					intReg.setValue((long) value);
				}
				break;
			case 'F':
				FloatingRegister floatReg = RegisterFile.floatingRegisters[registerNumber];
				if (floatReg.getQi() == tag) {
					System.out.println("Writing value " + value + " onto register " + register);
					floatReg.setValue(value);
				}
				break;
		}
	}

	public static void writeTagToRegisterFile(String register, int tag) {
		int registerNumber = register.charAt(1) - '0';
		switch (register.charAt(0)) {
			case 'R':
				IntegerRegister intReg = RegisterFile.integerRegisters[registerNumber];
				intReg.setQi(tag);
				break;
			case 'F':
				FloatingRegister floatReg = RegisterFile.floatingRegisters[registerNumber];
				floatReg.setQi(tag);
				break;
		}
	}
}
