package processor.tomasulo;

public class ALU
{
	/*
	 * Notice that in every single ALU operation whenever it is single, we do a weird conversion
	 * This is because if we leave it as is, the value gets fucked midconversion (or more precise to
	 * be exact) ex: instead of getting a result of 0.08, we get 0.0799999982... We do the weird
	 * hack for the converted result, but this might result in incorrect calulcations. Just means we
	 * need to test more :)
	 */
	public static double addFloatOperation(String OPCode, double F2, double F3) throws Exception
	{
		switch (OPCode)
		{
			case "ADD.D":
				return addFloating(F2, F3, false);
			case "SUB.D":
				return subtractFloating(F2, F3, false);
			case "ADD.S":
				return addFloating(F2, F3, true);
			case "SUB.S":
				return subtractFloating(F2, F3, false);
		}
		throw new Exception(
				"Add floating operation has been called in the ALU despite it not being a add floating operation");
	}

	public static long addIntegerOperation(String OPCode, long R2, short immediate) throws Exception
	{
		switch (OPCode)
		{
			case "ADDI":
				return ALU.addImmediate(R2, immediate);
			case "DADDI":
				return ALU.addImmediate(R2, immediate);
			case "SUBI":
				return ALU.subtractImmediate(R2, immediate);
			case "DSUBI":
				return ALU.subtractImmediate(R2, immediate);
		}
		throw new Exception(
				"Add integer operation has been called in the ALU despite it not being a add integer operation");

	}

	public static double multiplyFloatOperation(String OPCode, double F2, double F3) throws Exception
	{
		switch (OPCode)
		{
			case "MUL.D":
				return ALU.multiplyFloating(F2, F3, false);
			case "DIV.D":
				return ALU.divideFloating(F2, F3, false);
			case "MUL.S":
				return ALU.multiplyFloating(F2, F3, true);
			case "DIV.S":
				return ALU.divideFloating(F2, F3, true);
		}
		throw new Exception(
				"Multiply floating operation has been called in the ALU despite it not being a multiply floating operation");
	}

	private static double addFloating(double F2, double F3, boolean single)
	{
		if (single)
		{
			System.out.println("Adding single values " + F2 + " + " + F3);
			float result = (float) (F2 + F3);
			double convertedResult = Double.valueOf(Float.valueOf(result).toString()).doubleValue();
			return convertedResult;
		}
		else
		{
			System.out.println("Adding double values " + F2 + " + " + F3);
			double result = F2 + F3;
			return result;
		}
	}

	private static double subtractFloating(double F2, double F3, boolean single)
	{
		if (single)
		{
			System.out.println("Subtracting single values " + F2 + " + " + F3);
			float result = (float) (F2 - F3);
			double convertedResult = Double.valueOf(Float.valueOf(result).toString()).doubleValue();
			return convertedResult;
		}
		else
		{
			System.out.println("Subtracting double values " + F2 + " + " + F3);
			double result = F2 - F3;
			return result;
		}
	}

	private static double multiplyFloating(double F2, double F3, boolean single)
	{
		if (single)
		{
			System.out.println("Multiplying single values " + F2 + " + " + F3);
			float result = (float) (F2 * F3);
			double convertedResult = Double.valueOf(Float.valueOf(result).toString()).doubleValue();
			return convertedResult;
		}
		else
		{
			System.out.println("Multiplying double values " + F2 + " + " + F3);
			double result = F2 * F3;
			return result;
		}
	}

	private static double divideFloating(double F2, double F3, boolean single)
	{
		if (single)
		{
			System.out.println("Dividing single values " + F2 + " + " + F3);
			float result = (float) (F2 / F3);
			double convertedResult = Double.valueOf(Float.valueOf(result).toString()).doubleValue();
			return convertedResult;
		}
		else
		{
			System.out.println("Dividing double values " + F2 + " + " + F3);
			double result = F2 / F3;
			return result;
		}
	}

	private static long addImmediate(long R2, short immediate)
	{
		System.out.println("Adding immediate " + immediate + " onto the value " + R2);
		return R2 + (long) immediate;
	}

	private static long subtractImmediate(long R2, short immediate)
	{
		System.out.println("Subtracting immediate " + immediate + " onto the value " + R2);
		return R2 - (long) immediate;
	}
}
