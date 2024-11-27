package processor.tomasulo;

import java.io.IOException;
import java.util.ArrayList;

import processor.tomasulo.RegisterFile.FloatingRegister;

public class Tomasulo {
	public class LoadBuffer {

		boolean busy;
		int tag;
		int address;

		public LoadBuffer(int tag) {
			this.tag = tag;
			busy = false;
			address = 0;
		}
	}

	public class StoreBuffer {

		boolean busy;
		int tag;
		int V;
		int Q;
		int address;

		public StoreBuffer(int tag) {
			this.tag = tag;
			busy = false;
			address = 0;
			V = 0;
			Q = 0;
		}
	}

	public class ReservationStation {

		public int tag;
		public boolean busy;
		public String opcode; // should be int, but for simplicity sake it is string
		public double vj;
		public double vk;
		public int qj;
		public int qk;
		public int address;

		public ReservationStation(int tag) {
			this.tag = tag;
			busy = false;
			opcode = "";
			vj = 0;
			vk = 0;
			qj = 0;
			qk = 0;
			address = 0;
		}

	}

	public static RegisterFile registerFile = new RegisterFile();
	public static Memory memory = new Memory();
	public static ALU alu = new ALU();

	public static ArrayList<String> instructions = new ArrayList<String>(); // these are all the instructions, not yet
																			// executed :)
	// size should be entered by user
	public static ReservationStation addReservationStations[] = new ReservationStation[4];
	public static ReservationStation multiplyReservationStations[] = new ReservationStation[4];
	public static LoadBuffer loadBuffers[] = new LoadBuffer[4];
	public static StoreBuffer storeBuffers[] = new StoreBuffer[4];

	public static int clockCycle = 1;
	public static boolean stalled = false;

	private void init() {
		int tag = 1;
		for (int i = 0; i < addReservationStations.length; i++)
			addReservationStations[i] = new ReservationStation(tag++);

		for (int i = 0; i < multiplyReservationStations.length; i++)
			multiplyReservationStations[i] = new ReservationStation(tag++);

		for (int i = 0; i < loadBuffers.length; i++)
			loadBuffers[i] = new LoadBuffer(tag++);

		for (int i = 0; i < storeBuffers.length; i++)
			storeBuffers[i] = new StoreBuffer(tag++);
	}

	public void startExecution() throws IOException {
		init();
		ParseText parseText = new ParseText();
		instructions = parseText.parseTextFile();
		for (int i = 0; i < instructions.size(); i++) {
			System.out.println("In clock cycle: " + clockCycle);

			if (!stalled) {
				String regex = "[ ,]+";
				String[] parsedInstruction = instructions.get(i).split(regex);
				String OPCode = parsedInstruction[0];
				if (parseText.isAdditionOperation(OPCode)) {
					// check for empty addition stations, if none are avaliable, stall
					ReservationStation freeReservationStation = null; // THIS IS NOT THE TAG BIT, this is where in the
																		// array is the empty
					// reservation station
					for (ReservationStation addReservationStation : addReservationStations)
						if (addReservationStation.busy == false) {
							freeReservationStation = addReservationStation;
							break;
						}

					if (freeReservationStation == null) {
						System.out.println("Add stations are full, stalling...");
						i--;
						stalled = true;
						continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
					}

					freeReservationStation.busy = true;
					freeReservationStation.opcode = OPCode;
					String F1 = parsedInstruction[1]; // string as this is where we will save our result
					FloatingRegister F2 = RegisterFile.readFloatRegister(parsedInstruction[2]);
					FloatingRegister F3 = RegisterFile.readFloatRegister(parsedInstruction[3]);
					freeReservationStation.qj = F2.Qi; // if its 0, woo, if not, it saves it :)
					freeReservationStation.qk = F3.Qi; // if its 0, woo, if not, it saves it :)
					if(F2.Qi == 0)
						freeReservationStation.vj = F2.value;
					if(F3.Qi == 0)
						freeReservationStation.vk = F3.value;

					switch (OPCode) {
					case "ADD.D":
						ALU.addFloating(F1, freeReservationStation.vj, freeReservationStation.vk, false, freeReservationStation.tag);
						break;
					case "SUB.D":
						ALU.subtractFloating(F1, freeReservationStation.vj, freeReservationStation.vk, false, freeReservationStation.tag);
						break;
					case "ADD.S":
						ALU.addFloating(F1, freeReservationStation.vj, freeReservationStation.vk, true, freeReservationStation.tag);
						break;
					case "SUB.S":
						ALU.subtractFloating(F1, freeReservationStation.vj, freeReservationStation.vk, true, freeReservationStation.tag);
						break;
					}
				}
				
//				case "MUL.D":
//					ALU.multiplyFloating(F1, F2, F3, false);
//					break;
//				case "DIV.D":
//					ALU.divideFloating(F1, F2, F3, false);
//					break;
//				case "MUL.S":
//					ALU.multiplyFloating(F1, F2, F3, true);
//					break;
//				case "DIV.S":
//					ALU.divideFloating(F1, F2, F3, true);
//					break;
//			else if (isMemoryOperation(OPCode)) {
//				// logically, it should be long, since the memory is 64 bits, but a limitation
//				// of java is that arrays can only be addressed max by 2^32 - 1 numbers, or an
//				// int only,
//				String R1 = parsedInstruction[1]; // string as this is where we will save our result
//				int memoryAddress = Integer.parseInt(parsedInstruction[2]);
//				switch (OPCode) {
//				case "L.S":
//					float wordValue = Memory.loadSingle(memoryAddress);
//					// this weird hack is because precision gets fucked during conversion from float
//					// to double techincally gets more precise, but still is not something we wanted
//					double convertedWordValue = Double.valueOf(Float.valueOf(wordValue).toString()).doubleValue();
//					RegisterFile.writeRegister(R1, convertedWordValue);
//					break;
//				case "LW":
//					float integerWordValue = Memory.loadWord(memoryAddress);
//					RegisterFile.writeRegister(R1, integerWordValue);
//					break;
//				case "L.D":
//					double doubleWordValue = Memory.loadDouble(memoryAddress);
//					RegisterFile.writeRegister(R1, doubleWordValue);
//					break;
//				case "LD":
//					double integerDoubleWordValue = Memory.loadDoubleWord(memoryAddress);
//					RegisterFile.writeRegister(R1, integerDoubleWordValue);
//					break;
//				case "SW":
//					int registerValue = (int) RegisterFile.readRegister(R1);
//					Memory.storeWord(memoryAddress, registerValue);
//					break;
//				case "S.S":
//					float floatRegisterValue = (float) RegisterFile.readRegister(R1);
//					Memory.storeSingle(memoryAddress, floatRegisterValue);
//					break;
//				case "SD":
//					long longRegisterValue = (long) RegisterFile.readRegister(R1);
//					Memory.storeDoubleWord(memoryAddress, longRegisterValue);
//					break;
//				case "S.D":
//					double doubleRegisterValue = (double) RegisterFile.readRegister(R1);
//					Memory.storeDouble(memoryAddress, doubleRegisterValue);
//					break;
//
//				}
//
//			} else if (OPCode.equals("ADDI") || OPCode.equals("SUBI")) {
//				String R1 = parsedInstruction[1]; // string as this is where we will save our result
//				long R2 = (long) RegisterFile.readRegister(parsedInstruction[2]);
//				short immediate = Short.valueOf(parsedInstruction[3]);
//				switch (OPCode) {
//				case "ADDI":
//					ALU.addImmediate(R1, R2, immediate);
//					break;
//				case "SUBI":
//					ALU.subtractImmediate(R1, R2, immediate);
//					break;
//				}
//			} else {
//				long R1 = (long) RegisterFile.readRegister(parsedInstruction[1]);
//				switch (OPCode) {
//				// branching currently only handles integers; pray we dont need floating
//				case "BNEQ":
//					long R2 = (long) RegisterFile.readRegister(parsedInstruction[2]);
//					if (R1 != R2) {
//						int instruction = Integer.parseInt(parsedInstruction[3]);
//						i = instruction - 1; // -1 since we will increment at the end of the loop
//
//					}
//					break;
//				case "BEQZ":
//					if (R1 == 0) {
//						int instruction = Integer.parseInt(parsedInstruction[2]);
//						i = instruction - 1; // -1 since we will increment at the end of the loop
//					}
//					break;
//				}
//
//			}
//		}

			}

		}
	}
}