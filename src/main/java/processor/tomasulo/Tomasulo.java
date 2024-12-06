package processor.tomasulo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import processor.tomasulo.RegisterFile.FloatingRegister;
import processor.tomasulo.RegisterFile.IntegerRegister;

public class Tomasulo {
	private Consumer<String> updateLog;


	public void setUpdateLogCallback(Consumer<String> callback) {
		this.updateLog = callback;
	}

	private void logUpdate(String message) {
		if (updateLog != null)
			updateLog.accept(message);

	}

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
		double V;
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

	public static class ReservationStation {

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
			logUpdate("In clock cycle: " + clockCycle);
			System.out.println("In clock cycle: " + clockCycle);

			if (stalled) {
				i--;
				continue;
			}

			String regex = "[ ,]+";
			String[] parsedInstruction = instructions.get(i).split(regex);
			String OPCode = parsedInstruction[0];
			if (parseText.isAdditionOperation(OPCode)) {
				// check for empty addition stations, if none are avaliable, stall
				ReservationStation freeReservationStation = null;
				for (ReservationStation addReservationStation : addReservationStations)
					if (addReservationStation.busy == false) {
						freeReservationStation = addReservationStation;
						break;
					}

				if (freeReservationStation == null) {
					logUpdate("Stalled due to full reservation station...");
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
				if (F2.Qi == 0)
					freeReservationStation.vj = F2.value;
				if (F3.Qi == 0)
					freeReservationStation.vk = F3.value;

				switch (OPCode) {
				case "ADD.D":
					ALU.addFloating(F1, freeReservationStation.vj, freeReservationStation.vk, false,
							freeReservationStation.tag);
					break;
				case "SUB.D":
					ALU.subtractFloating(F1, freeReservationStation.vj, freeReservationStation.vk, false,
							freeReservationStation.tag);
					break;
				case "ADD.S":
					ALU.addFloating(F1, freeReservationStation.vj, freeReservationStation.vk, true,
							freeReservationStation.tag);
					break;
				case "SUB.S":
					ALU.subtractFloating(F1, freeReservationStation.vj, freeReservationStation.vk, true,
							freeReservationStation.tag);
					break;
				}
			} else if (parseText.isMultiplyOperation(OPCode)) {
				ReservationStation freeReservationStation = null;
				for (ReservationStation multiplyReservationStation : multiplyReservationStations)
					if (multiplyReservationStation.busy == false) {
						freeReservationStation = multiplyReservationStation;
						break;
					}

				if (freeReservationStation == null) {
					logUpdate("Stalled due to full reservation station...");
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
				if (F2.Qi == 0)
					freeReservationStation.vj = F2.value;
				if (F3.Qi == 0)
					freeReservationStation.vk = F3.value;
				switch (OPCode) {
				case "MUL.D":
					ALU.multiplyFloating(F1, freeReservationStation.vj, freeReservationStation.vk, false,
							freeReservationStation.tag);
					break;
				case "DIV.D":
					ALU.divideFloating(F1, freeReservationStation.vj, freeReservationStation.vk, false,
							freeReservationStation.tag);
					break;
				case "MUL.S":
					ALU.multiplyFloating(F1, freeReservationStation.vj, freeReservationStation.vk, true,
							freeReservationStation.tag);
					break;
				case "DIV.S":
					ALU.divideFloating(F1, freeReservationStation.vj, freeReservationStation.vk, true,
							freeReservationStation.tag);
					break;

				}
			} else if (OPCode.equals("ADDI") || OPCode.equals("SUBI")) {
				ReservationStation freeReservationStation = null;
				for (ReservationStation addReservationStation : addReservationStations)
					if (addReservationStation.busy == false) {
						freeReservationStation = addReservationStation;
						break;
					}

				if (freeReservationStation == null) {
					logUpdate("Stalled due to full reservation station...");
					i--;
					stalled = true;
					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
				}

				freeReservationStation.busy = true;
				freeReservationStation.opcode = OPCode;

				String R1 = parsedInstruction[1]; // string as this is where we will save our result
				IntegerRegister R2 = RegisterFile.readIntegerRegister(parsedInstruction[2]);
				short immediate = Short.valueOf(parsedInstruction[3]);

				freeReservationStation.qj = R2.Qi; // if its 0, woo, if not, it saves it :)
				freeReservationStation.qk = 0; // since it only reads one register :)
				if (R2.Qi == 0)
					freeReservationStation.vj = R2.value;
				freeReservationStation.vk = immediate;

				switch (OPCode) {
				case "ADDI":
					ALU.addImmediate(R1, (long) freeReservationStation.vj, (short)freeReservationStation.vk, freeReservationStation.tag);
					break;
				case "SUBI":
					ALU.subtractImmediate(R1, (long) freeReservationStation.vj, (short)freeReservationStation.vk, freeReservationStation.tag);
					break;
				}

			}
			else if (parseText.isLoadOperation(OPCode)) {
				// logically, it should be long, since the memory is 64 bits, but a limitation
				// of java is that arrays can only be addressed max by 2^32 - 1 numbers, or an
				// int only,
				LoadBuffer freeLoadBuffer= null;
				for (LoadBuffer loadBuffer : loadBuffers)
					if (loadBuffer.busy == false) {
						freeLoadBuffer = loadBuffer;
						break;
					}

				if (freeLoadBuffer == null) {
					logUpdate("Stalled due to full reservation station...");
					i--;
					stalled = true;
					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
				}

				String R1 = parsedInstruction[1]; // string as this is where we will save our result
				int memoryAddress = Integer.parseInt(parsedInstruction[2]);

				freeLoadBuffer.busy = true;
				freeLoadBuffer.address =  memoryAddress;
				switch (OPCode) {
				case "L.S":
					float wordValue = Memory.loadSingle(freeLoadBuffer.address);
					// this weird hack is because precision gets fucked during conversion from float
					// to double techincally gets more precise, but still is not something we wanted
					double convertedWordValue = Double.valueOf(Float.valueOf(wordValue).toString()).doubleValue();
					RegisterFile.writeRegister(R1, convertedWordValue, freeLoadBuffer.tag);
					break;
				case "LW":
					int integerWordValue = Memory.loadWord(freeLoadBuffer.address);
					RegisterFile.writeRegister(R1, integerWordValue, freeLoadBuffer.tag);
					break;
				case "L.D":
					double doubleWordValue = Memory.loadDouble(freeLoadBuffer.address);
					RegisterFile.writeRegister(R1, doubleWordValue, freeLoadBuffer.tag);
					break;
				case "LD":
					double integerDoubleWordValue = Memory.loadDoubleWord(freeLoadBuffer.address);
					RegisterFile.writeRegister(R1, integerDoubleWordValue, freeLoadBuffer.tag);
					break;
				}
			}
			else if (parseText.isStoreOperation(OPCode)) {
				// logically, it should be long, since the memory is 64 bits, but a limitation
				// of java is that arrays can only be addressed max by 2^32 - 1 numbers, or an
				// int only,
				StoreBuffer freeStoreBuffer = null;
				for (StoreBuffer storeBuffer: storeBuffers)
					if (storeBuffer.busy == false) {
						freeStoreBuffer  = storeBuffer;
						break;
					}

				if (freeStoreBuffer  == null) {
					logUpdate("Stalled due to full reservation station...");
					i--;
					stalled = true;
					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
				}

				String R1 = parsedInstruction[1]; // string as this is where we will save our result
				int memoryAddress = Integer.parseInt(parsedInstruction[2]);

				freeStoreBuffer.busy = true;
				freeStoreBuffer.address =  memoryAddress;
				switch (OPCode) {
				case "SW":
					IntegerRegister integerRegisterValue = RegisterFile.readIntegerRegister(R1);
					freeStoreBuffer.Q = integerRegisterValue.Qi;
					if(freeStoreBuffer.Q==0)
						freeStoreBuffer.V = integerRegisterValue.value;
					Memory.storeWord(freeStoreBuffer.address, (int) freeStoreBuffer.V);
					break;
				case "S.S":
					FloatingRegister  floatRegister =  RegisterFile.readFloatRegister(R1);
					freeStoreBuffer.Q = floatRegister.Qi;
					if(freeStoreBuffer.Q==0)
						freeStoreBuffer.V = floatRegister.value;
					Memory.storeSingle(freeStoreBuffer.address, (float) freeStoreBuffer.V);
					break;
				case "SD":
					IntegerRegister longRegisterValue =  RegisterFile.readIntegerRegister(R1);
					freeStoreBuffer.Q = longRegisterValue.Qi;
					if(freeStoreBuffer.Q==0)
						freeStoreBuffer.V = longRegisterValue.value;
					Memory.storeDoubleWord(freeStoreBuffer.address, (long) freeStoreBuffer.V);
					break;
				case "S.D":
					FloatingRegister doubleRegister =  RegisterFile.readFloatRegister(R1);
					freeStoreBuffer.Q = doubleRegister.Qi;
					if(freeStoreBuffer.Q==0)
						freeStoreBuffer.V = doubleRegister.value;
					Memory.storeDouble(freeStoreBuffer.address, freeStoreBuffer.V);
					break;
			}
			}

//
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
		clockCycle++;
		}

	}
}
