package processor.tomasulo;

public class ALU {
	/*
	 *  Notice that in every single ALU operation whenever it is single, we do a weird conversion 
	 *  This is because if we leave it as is, the value gets fucked midconversion (or more precise to be exact)
	 *  ex: instead of getting a result of 0.08, we get 0.0799999982...
	 *  We do the weird hack for the converted result, but this might result in incorrect calulcations. Just means we
	 *  need to test more :)
	 */
	public static void addOperation()
	{
		
	}
	public static void addFloating(String F1, double F2, double F3, boolean single, int tag) {
		if (single) {
			System.out.println("Adding single values " + F2 + " + " + F3 + " and saving onto " + F1);
			float result = (float) (F2 + F3);
			double convertedResult=	Double.valueOf(Float.valueOf(result).toString()).doubleValue();
			Tomasulo.addReservationStations.set(1,new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, convertedResult, tag);
		} else {
			System.out.println("Adding double values " + F2 + " + " + F3 + " and saving onto " + F1);
			double result = F2 + F3;
			Tomasulo.addReservationStations.set(1,new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, result, tag);
		}
	}

	public static void subtractFloating(String F1, double F2, double F3, boolean single, int tag) {
		if (single) {
			System.out.println("Subtracting single values " + F2 + " + " + F3 + " and saving onto " + F1);
			float result = (float) (F2 - F3);
			double convertedResult=	Double.valueOf(Float.valueOf(result).toString()).doubleValue();
			Tomasulo.addReservationStations.set(1,new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, convertedResult, tag);
		} else {
			System.out.println("Subtracting double values " + F2 + " + " + F3 + " and saving onto " + F1);
			double result = F2 - F3;
			Tomasulo.addReservationStations.set(1,new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, result, tag);
		}
	}

	public static void multiplyFloating(String F1, double F2, double F3, boolean single, int tag) {
		if (single) {
			System.out.println("Multiplying single values " + F2 + " + " + F3 + " and saving onto " + F1);
			float result = (float) (F2 * F3);
			double convertedResult=	Double.valueOf(Float.valueOf(result).toString()).doubleValue();
			Tomasulo.multiplyReservationStations.set(1,new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, convertedResult, tag);
		} else {
			System.out.println("Multiplying double values " + F2 + " + " + F3 + " and saving onto " + F1);
			double result = F2 * F3;
			Tomasulo.multiplyReservationStations.set(1,new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, result, tag);
		}
	}

	public static void divideFloating(String F1, double F2, double F3, boolean single, int tag) {
		if (single) {
			System.out.println("Dividing single values " + F2 + " + " + F3 + " and saving onto " + F1);
			float result = (float) (F2 / F3);
			double convertedResult=	Double.valueOf(Float.valueOf(result).toString()).doubleValue();
			Tomasulo.multiplyReservationStations.set(1,new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, convertedResult, tag);
		} else {
			System.out.println("Dividing double values " + F2 + " + " + F3 + " and saving onto " + F1);
			double result = F2 / F3;
			Tomasulo.multiplyReservationStations.set(1,new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, result, tag);
		}
	}

	public static void addImmediate(String R1, long R2, short immediate, int tag)
	{
		System.out.println("Adding immediate " + immediate + " onto the value " + R2);
		long result = R2 + immediate;
		RegisterFile.writeRegister(R1, result, tag);
	}

	public static void subtractImmediate(String R1, long R2, short immediate, int tag)
	{
		System.out.println("Subtracting immediate " + immediate + " onto the value " + R2);
		long result = R2 - immediate;
		RegisterFile.writeRegister(R1, result, tag);
	}
}
