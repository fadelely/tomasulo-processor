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
		case "SUBI":
			return ALU.subtractImmediate(R2, immediate);
		}
		throw new Exception(
				"Add integer operation has been called in the ALU despite it not being a add floating operation");

	}
//
//				    case "MUL.D":
//						ALU.multiplyFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), false,
//								freeReservationStation.getTag());
//						break;
//					case "DIV.D":
//						ALU.divideFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), false,
//								freeReservationStation.getTag());
//						break;
//					case "MUL.S":
//						ALU.multiplyFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), true,
//								freeReservationStation.getTag());
//						break;
//					case "DIV.S":
//						ALU.divideFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), true,
//								freeReservationStation.getTag());
//						break;
//				    case "SW":
//						IntegerRegister integerRegisterValue = RegisterFile.readIntegerRegister(R1);
//						freeStoreBuffer.setQ(integerRegisterValue.Qi);
//						if(freeStoreBuffer.getQ()==0)
//							freeStoreBuffer.setV(integerRegisterValue.value);
//						Memory.storeWord(freeStoreBuffer.getAddress(), (int) freeStoreBuffer.getV());
//						break;
//					case "S.S":
//						FloatingRegister  floatRegister =  RegisterFile.readFloatRegister(R1);
//						freeStoreBuffer.setQ(floatRegister.Qi);
//						if(freeStoreBuffer.getQ()==0)
//							freeStoreBuffer.setV(floatRegister.value);
//						Memory.storeSingle(freeStoreBuffer.getAddress(), (float) freeStoreBuffer.getV());
//						break;
//					case "SD":
//						IntegerRegister longRegisterValue =  RegisterFile.readIntegerRegister(R1);
//						freeStoreBuffer.setQ(longRegisterValue.Qi);
//						if(freeStoreBuffer.getQ()==0)
//							freeStoreBuffer.setV(longRegisterValue.value);
//						Memory.storeDoubleWord(freeStoreBuffer.getAddress(), (long) freeStoreBuffer.getV());
//						break;
//					case "S.D":
//						FloatingRegister doubleRegister =  RegisterFile.readFloatRegister(R1);
//						freeStoreBuffer.setQ(doubleRegister.Qi);
//						if(freeStoreBuffer.getQ()==0)
//							freeStoreBuffer.setV(doubleRegister.value);
//						Memory.storeDouble(freeStoreBuffer.getAddress(), freeStoreBuffer.getV());
//       				    System.out.println(Memory.loadDouble(freeStoreBuffer.getAddress()));
//						break;
//				    case "L.S":
//						float wordValue = Memory.loadSingle(freeLoadBuffer.getAddress());
//						// this weird hack is because precision gets fucked during conversion from float
//						// to double techincally gets more precise, but still is not something we wanted
//						double convertedWordValue = Double.valueOf(Float.valueOf(wordValue).toString()).doubleValue();
//						RegisterFile.writeRegister(R1, convertedWordValue, freeLoadBuffer.getTag());
//						break;
//					case "LW":
//						int integerWordValue = Memory.loadWord(freeLoadBuffer.getAddress());
//						RegisterFile.writeRegister(R1, integerWordValue, freeLoadBuffer.getTag());
//						break;
//					case "L.D":
//						double doubleWordValue = Memory.loadDouble(freeLoadBuffer.getAddress());
//						RegisterFile.writeRegister(R1, doubleWordValue, freeLoadBuffer.getTag());
//						break;
//					case "LD":
//						double integerDoubleWordValue = Memory.loadDoubleWord(freeLoadBuffer.getAddress());
//						RegisterFile.writeRegister(R1, integerDoubleWordValue, freeLoadBuffer.getTag());
//						break;
////				case "BNEQ":
////					long R2 = (long) RegisterFile.readRegister(parsedInstruction[2]);
////					if (R1 != R2) {
////						int instruction = Integer.parseInt(parsedInstruction[3]);
////						i = instruction - 1; // -1 since we will increment at the end of the loop
////
////					}
////					break;
////				case "BEQZ":
////					if (R1 == 0) {
////						int instruction = Integer.parseInt(parsedInstruction[2]);
////						i = instruction - 1; // -1 since we will increment at the end of the loop
////					}
////					break;
//
//
//			}

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

	private static void multiplyFloating(String F1, double F2, double F3, boolean single, int tag)
	{
		if (single)
		{
			System.out.println("Multiplying single values " + F2 + " + " + F3 + " and saving onto " + F1);
			float result = (float) (F2 * F3);
			double convertedResult = Double.valueOf(Float.valueOf(result).toString()).doubleValue();
			Tomasulo.multiplyReservationStations.set(1, new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, convertedResult, tag);
		}
		else
		{
			System.out.println("Multiplying double values " + F2 + " + " + F3 + " and saving onto " + F1);
			double result = F2 * F3;
			Tomasulo.multiplyReservationStations.set(1, new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, result, tag);
		}
	}

	private static void divideFloating(String F1, double F2, double F3, boolean single, int tag)
	{
		if (single)
		{
			System.out.println("Dividing single values " + F2 + " + " + F3 + " and saving onto " + F1);
			float result = (float) (F2 / F3);
			double convertedResult = Double.valueOf(Float.valueOf(result).toString()).doubleValue();
			Tomasulo.multiplyReservationStations.set(1, new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, convertedResult, tag);
		}
		else
		{
			System.out.println("Dividing double values " + F2 + " + " + F3 + " and saving onto " + F1);
			double result = F2 / F3;
			Tomasulo.multiplyReservationStations.set(1, new Tomasulo.ReservationStation(tag));
			RegisterFile.writeRegister(F1, result, tag);
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
