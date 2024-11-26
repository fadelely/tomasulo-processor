package processor.tomasulo;

public class RegisterFile {
	double floatingRegisters[];
	long integerRegisters[];
	public RegisterFile()
	{
		floatingRegisters = new double[32];
		integerRegisters = new long[32];
	}
}
