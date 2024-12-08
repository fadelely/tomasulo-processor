package processor.tomasulo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import processor.tomasulo.RegisterFile.FloatingRegister;
import processor.tomasulo.RegisterFile.IntegerRegister;

public class Tomasulo {
	private Consumer<String> updateLog;


	public static int addReservationStationsSize = 4;
	public static int multiplyReservationStationsSize = 4;
	public static int loadBuffersSize = 4;
	public static int storeBuffersSize = 4;



	public void setUpdateLogCallback(Consumer<String> callback) {
		this.updateLog = callback;
	}

	private void logUpdate(String message) {
		if (updateLog != null)
			updateLog.accept(message);

	}

	public class LoadBuffer {

		private final BooleanProperty busy;
		private final IntegerProperty tag;
		private final IntegerProperty address;

		// Constructor
		public LoadBuffer(int tag) {
			this.tag = new SimpleIntegerProperty(tag);
			this.busy = new SimpleBooleanProperty(false);
			this.address = new SimpleIntegerProperty(0);
		}

		// Getter and Setter for busy
		public boolean isBusy() {
			return busy.get();
		}

		public void setBusy(boolean busy) {
			this.busy.set(busy);
		}

		public BooleanProperty busyProperty() {
			return busy;
		}

		// Getter and Setter for tag
		public int getTag() {
			return tag.get();
		}

		public void setTag(int tag) {
			this.tag.set(tag);
		}

		public IntegerProperty tagProperty() {
			return tag;
		}

		// Getter and Setter for address
		public int getAddress() {
			return address.get();
		}

		public void setAddress(int address) {
			this.address.set(address);
		}

		public IntegerProperty addressProperty() {
			return address;
		}
	}

	public class StoreBuffer {

		private final BooleanProperty busy;
		private final IntegerProperty tag;
		private final DoubleProperty V;
		private final IntegerProperty Q;
		private final IntegerProperty address;

		// Constructor
		public StoreBuffer(int tag) {
			this.tag = new SimpleIntegerProperty(tag);
			this.busy = new SimpleBooleanProperty(false);
			this.V = new SimpleDoubleProperty(0);
			this.Q = new SimpleIntegerProperty(0);
			this.address = new SimpleIntegerProperty(0);
		}

		// Getter and Setter for busy
		public BooleanProperty busyProperty() {
			return busy;
		}

		public boolean isBusy() {
			return busy.get();
		}

		public void setBusy(boolean busy) {
			this.busy.set(busy);
		}

		// Getter and Setter for tag
		public IntegerProperty tagProperty() {
			return tag;
		}

		public int getTag() {
			return tag.get();
		}

		public void setTag(int tag) {
			this.tag.set(tag);
		}

		// Getter and Setter for V
		public DoubleProperty VProperty() {
			return V;
		}

		public double getV() {
			return V.get();
		}

		public void setV(double v) {
			this.V.set(v);
		}

		// Getter and Setter for Q
		public IntegerProperty QProperty() {
			return Q;
		}

		public int getQ() {
			return Q.get();
		}

		public void setQ(int q) {
			this.Q.set(q);
		}

		// Getter and Setter for address
		public IntegerProperty addressProperty() {
			return address;
		}

		public int getAddress() {
			return address.get();
		}

		public void setAddress(int address) {
			this.address.set(address);
		}
	}

	public static class ReservationStation {

		private final IntegerProperty tag;
		private final BooleanProperty busy;
		private final StringProperty opcode; // Using StringProperty instead of a plain string.
		private final DoubleProperty vj;
		private final DoubleProperty vk;
		private final IntegerProperty qj;
		private final IntegerProperty qk;
		private final IntegerProperty address;

		// Constructor
		public ReservationStation(int tag) {
			this.tag = new SimpleIntegerProperty(tag);
			this.busy = new SimpleBooleanProperty(false);
			this.opcode = new SimpleStringProperty("");
			this.vj = new SimpleDoubleProperty(0.0);
			this.vk = new SimpleDoubleProperty(0.0);
			this.qj = new SimpleIntegerProperty(0);
			this.qk = new SimpleIntegerProperty(0);
			this.address = new SimpleIntegerProperty(0);
		}

		// Getter and Setter for tag
		public IntegerProperty tagProperty() {
			return tag;
		}

		public int getTag() {
			return tag.get();
		}

		public void setTag(int tag) {
			this.tag.set(tag);
		}

		// Getter and Setter for busy
		public BooleanProperty busyProperty() {
			return busy;
		}

		public boolean isBusy() {
			return busy.get();
		}

		public void setBusy(boolean busy) {
			this.busy.set(busy);
		}

		// Getter and Setter for opcode
		public StringProperty opcodeProperty() {
			return opcode;
		}

		public String getOpcode() {
			return opcode.get();
		}

		public void setOpcode(String opcode) {
			this.opcode.set(opcode);
		}

		// Getter and Setter for vj
		public DoubleProperty vjProperty() {
			return vj;
		}

		public double getVj() {
			return vj.get();
		}

		public void setVj(double vj) {
			this.vj.set(vj);
		}

		// Getter and Setter for vk
		public DoubleProperty vkProperty() {
			return vk;
		}

		public double getVk() {
			return vk.get();
		}

		public void setVk(double vk) {
			this.vk.set(vk);
		}

		// Getter and Setter for qj
		public IntegerProperty qjProperty() {
			return qj;
		}

		public int getQj() {
			return qj.get();
		}

		public void setQj(int qj) {
			this.qj.set(qj);
		}

		// Getter and Setter for qk
		public IntegerProperty qkProperty() {
			return qk;
		}

		public int getQk() {
			return qk.get();
		}

		public void setQk(int qk) {
			this.qk.set(qk);
		}

		// Getter and Setter for address
		public IntegerProperty addressProperty() {
			return address;
		}

		public int getAddress() {
			return address.get();
		}

		public void setAddress(int address) {
			this.address.set(address);
		}
	}


	public static RegisterFile registerFile = new RegisterFile();
	public static Memory memory = new Memory();
	public static ALU alu = new ALU();

	public static ArrayList<String> instructions = new ArrayList<String>(); // these are all the instructions, not yet
	// executed :)
	// size should be entered by user
	public static ObservableList<ReservationStation> addReservationStations = FXCollections.observableArrayList();

	public static ObservableList<ReservationStation> multiplyReservationStations = FXCollections.observableArrayList();


	public static ObservableList<LoadBuffer> loadBuffers = FXCollections.observableArrayList();
	public static ObservableList<StoreBuffer> storeBuffers = FXCollections.observableArrayList();
	public static int clockCycle = 1;
	public static boolean stalled = false;

	public void init() {
		int tag = 1;
		for (int i = 0; i < addReservationStationsSize; i++){
			ReservationStation addReservationStation = new ReservationStation(tag++);
			addReservationStations.add(addReservationStation);
//			System.out.println("Add: "+addReservationStation.tag+"   "+i);

		}


		for (int i = 0; i < multiplyReservationStationsSize; i++){
			ReservationStation multiplyReservationStation = new ReservationStation(tag++);
			multiplyReservationStations.add(multiplyReservationStation);
//			System.out.println("Multiply: "+multiplyReservationStation.tag+"   "+i);
		}


		for (int i = 0; i < loadBuffersSize; i++) {
			LoadBuffer loadBuffer = new LoadBuffer(tag++);
			loadBuffers.add(loadBuffer);
//			System.out.println("Load: "+loadBuffer.tag+"   "+i);
		}
		for (int i = 0; i < storeBuffersSize; i++) {
			StoreBuffer storeBuffer = new StoreBuffer(tag++);
			storeBuffers.add(storeBuffer);
//			System.out.println("Store :" +storeBuffer.tag+"   "+i);
		}
	}

	public void startExecution() throws IOException {
//		init();
		ParseText parseText = new ParseText();
		instructions = parseText.parseTextFile();
		for (int i = 0; i < instructions.size(); i++) {
			logUpdate("In clock cycle: " + clockCycle);
//			System.out.println("In clock cycle: " + clockCycle);

			if (stalled) {
				i--;
				System.out.println("I am stalled");

//				continue;
			}

			String regex = "[ ,]+";
			String[] parsedInstruction = instructions.get(i).split(regex);
			String OPCode = parsedInstruction[0];
			System.out.println("OPCode: " + OPCode);
			if (parseText.isAdditionOperation(OPCode)) {
				// check for empty addition stations, if none are avaliable, stall
				ReservationStation freeReservationStation = null;
				for (ReservationStation addReservationStation : addReservationStations)
					if (!addReservationStation.isBusy()) {
						freeReservationStation = addReservationStation;
						break;
					}

				if (freeReservationStation == null) {
					logUpdate("Stalled due to full reservation station...");
					i--;
					stalled = true;
//					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
				}

				freeReservationStation.setBusy(true);
				freeReservationStation.setOpcode(OPCode);
				String F1 = parsedInstruction[1]; // string as this is where we will save our result
				FloatingRegister F2 = RegisterFile.readFloatRegister(parsedInstruction[2]);
				FloatingRegister F3 = RegisterFile.readFloatRegister(parsedInstruction[3]);
				freeReservationStation.setQj(F2.Qi); // if its 0, woo, if not, it saves it :)
				freeReservationStation.setQk(F3.Qi); // if its 0, woo, if not, it saves it :)
				if (F2.Qi == 0)
					freeReservationStation.setVj(F2.value);
				if (F3.Qi == 0)
					freeReservationStation.setVk(F3.value);

				switch (OPCode) {
					case "ADD.D":
						ALU.addFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), false,
								freeReservationStation.getTag());
						break;
					case "SUB.D":
						ALU.subtractFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), false,
								freeReservationStation.getTag());
						break;
					case "ADD.S":
						ALU.addFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), true,
								freeReservationStation.getTag());
						break;
					case "SUB.S":
						ALU.subtractFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), true,
								freeReservationStation.getTag());
						break;
				}
			} else if (parseText.isMultiplyOperation(OPCode)) {
				ReservationStation freeReservationStation = null;
				for (ReservationStation multiplyReservationStation : multiplyReservationStations)
					if (!multiplyReservationStation.isBusy()) {
						freeReservationStation = multiplyReservationStation;
						break;
					}

				if (freeReservationStation == null) {
					logUpdate("Stalled due to full reservation station...");
					i--;
					stalled = true;
//					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
				}

				freeReservationStation.setBusy(true);
				freeReservationStation.setOpcode(OPCode);
				String F1 = parsedInstruction[1]; // string as this is where we will save our result
				FloatingRegister F2 = RegisterFile.readFloatRegister(parsedInstruction[2]);
				FloatingRegister F3 = RegisterFile.readFloatRegister(parsedInstruction[3]);
				freeReservationStation.setQj(F2.Qi); // if its 0, woo, if not, it saves it :)
				freeReservationStation.setQk(F3.Qi); // if its 0, woo, if not, it saves it :)
				if (F2.Qi == 0)
					freeReservationStation.setVj(F2.value);
				if (F3.Qi == 0)
					freeReservationStation.setVk( F3.value);
				switch (OPCode) {
					case "MUL.D":
						ALU.multiplyFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), false,
								freeReservationStation.getTag());
						break;
					case "DIV.D":
						ALU.divideFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), false,
								freeReservationStation.getTag());
						break;
					case "MUL.S":
						ALU.multiplyFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), true,
								freeReservationStation.getTag());
						break;
					case "DIV.S":
						ALU.divideFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), true,
								freeReservationStation.getTag());
						break;

				}
			} else if (OPCode.equals("ADDI") || OPCode.equals("SUBI")) {
				ReservationStation freeReservationStation = null;
				for (ReservationStation addReservationStation : addReservationStations)
					if (!addReservationStation.isBusy()) {
						freeReservationStation = addReservationStation;
						break;
					}

				if (freeReservationStation == null) {
					logUpdate("Stalled due to full reservation station...");
					i--;
					stalled = true;
//					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
				}

				freeReservationStation.setBusy(true);
				freeReservationStation.setOpcode(OPCode);

				String R1 = parsedInstruction[1]; // string as this is where we will save our result
				IntegerRegister R2 = RegisterFile.readIntegerRegister(parsedInstruction[2]);
				short immediate = Short.valueOf(parsedInstruction[3]);

				freeReservationStation.setQj(R2.Qi); // if its 0, woo, if not, it saves it :)
				freeReservationStation.setQk(0); // since it only reads one register :)
				if (R2.Qi == 0)
					freeReservationStation.setVj(R2.value);
				freeReservationStation.setVk(immediate);

				switch (OPCode) {
					case "ADDI":
						ALU.addImmediate(R1, (long) freeReservationStation.getVj(), (short)freeReservationStation.getVk(), freeReservationStation.getTag());
						break;
					case "SUBI":
						ALU.subtractImmediate(R1, (long) freeReservationStation.getVj(), (short)freeReservationStation.getVk(), freeReservationStation.getTag());
						break;
				}

			}
			else if (parseText.isLoadOperation(OPCode)) {
				// logically, it should be long, since the memory is 64 bits, but a limitation
				// of java is that arrays can only be addressed max by 2^32 - 1 numbers, or an
				// int only,
				LoadBuffer freeLoadBuffer= null;
				for (LoadBuffer loadBuffer : loadBuffers) {
//					System.out.println(loadBuffer.tag + "   " + loadBuffer.busy);
					if (!loadBuffer.isBusy()) {
						freeLoadBuffer = loadBuffer;
						break;
					}
				}

				if (freeLoadBuffer == null) {
					logUpdate("Stalled due to full reservation station...");
					i--;
					stalled = true;
//					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
				}

				String R1 = parsedInstruction[1]; // string as this is where we will save our result
				int memoryAddress = Integer.parseInt(parsedInstruction[2]);

				freeLoadBuffer.setBusy(true);
				freeLoadBuffer.setAddress(memoryAddress);
				switch (OPCode) {
					case "L.S":
						float wordValue = Memory.loadSingle(freeLoadBuffer.getAddress());
						// this weird hack is because precision gets fucked during conversion from float
						// to double techincally gets more precise, but still is not something we wanted
						double convertedWordValue = Double.valueOf(Float.valueOf(wordValue).toString()).doubleValue();
						RegisterFile.writeRegister(R1, convertedWordValue, freeLoadBuffer.getTag());
						break;
					case "LW":
						int integerWordValue = Memory.loadWord(freeLoadBuffer.getAddress());
						RegisterFile.writeRegister(R1, integerWordValue, freeLoadBuffer.getTag());
						break;
					case "L.D":
						double doubleWordValue = Memory.loadDouble(freeLoadBuffer.getAddress());
						RegisterFile.writeRegister(R1, doubleWordValue, freeLoadBuffer.getTag());
						break;
					case "LD":
						double integerDoubleWordValue = Memory.loadDoubleWord(freeLoadBuffer.getAddress());
						RegisterFile.writeRegister(R1, integerDoubleWordValue, freeLoadBuffer.getTag());
						break;
				}
			}
			else if (parseText.isStoreOperation(OPCode)) {
				// logically, it should be long, since the memory is 64 bits, but a limitation
				// of java is that arrays can only be addressed max by 2^32 - 1 numbers, or an
				// int only,
				StoreBuffer freeStoreBuffer = null;
				for (StoreBuffer storeBuffer: storeBuffers)
					if (!storeBuffer.isBusy()) {
						freeStoreBuffer  = storeBuffer;
						break;
					}

				if (freeStoreBuffer  == null) {
					logUpdate("Stalled due to full reservation station...");
					i--;
					stalled = true;
//					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
				}

				String R1 = parsedInstruction[1]; // string as this is where we will save our result
				int memoryAddress = Integer.parseInt(parsedInstruction[2]);

				freeStoreBuffer.setBusy(true);
				freeStoreBuffer.setAddress(memoryAddress);
				switch (OPCode) {
					case "SW":
						IntegerRegister integerRegisterValue = RegisterFile.readIntegerRegister(R1);
						freeStoreBuffer.setQ(integerRegisterValue.Qi);
						if(freeStoreBuffer.getQ()==0)
							freeStoreBuffer.setV(integerRegisterValue.value);
						Memory.storeWord(freeStoreBuffer.getAddress(), (int) freeStoreBuffer.getV());
						break;
					case "S.S":
						FloatingRegister  floatRegister =  RegisterFile.readFloatRegister(R1);
						freeStoreBuffer.setQ(floatRegister.Qi);
						if(freeStoreBuffer.getQ()==0)
							freeStoreBuffer.setV(floatRegister.value);
						Memory.storeSingle(freeStoreBuffer.getAddress(), (float) freeStoreBuffer.getV());
						break;
					case "SD":
						IntegerRegister longRegisterValue =  RegisterFile.readIntegerRegister(R1);
						freeStoreBuffer.setQ(longRegisterValue.Qi);
						if(freeStoreBuffer.getQ()==0)
							freeStoreBuffer.setV(longRegisterValue.value);
						Memory.storeDoubleWord(freeStoreBuffer.getAddress(), (long) freeStoreBuffer.getV());
						break;
					case "S.D":
						FloatingRegister doubleRegister =  RegisterFile.readFloatRegister(R1);
						freeStoreBuffer.setQ(doubleRegister.Qi);
						if(freeStoreBuffer.getQ()==0)
							freeStoreBuffer.setV(doubleRegister.value);
						Memory.storeDouble(freeStoreBuffer.getAddress(), freeStoreBuffer.getV());
//						System.out.println(Memory.loadDouble(freeStoreBuffer.getAddress()));
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
