package processor.tomasulo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import processor.tomasulo.RegisterFile.FloatingRegister;
import processor.tomasulo.RegisterFile.IntegerRegister;

public class Tomasulo {
	private Consumer<String> updateLog;

	public static int addReservationStationsSize = 4;
	public static int multiplyReservationStationsSize = 4;
	public static int loadBuffersSize = 4;
	public static int storeBuffersSize = 4;

	public static int LoadBufferExecutionTime = 1;

	public static int StoreBufferExecutionTime = 1;

	public static int AddReservationStationExecutionTime = 2;

	public static int MultiplyReservationStationExecutionTime = 4;

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

	private int currentInstructionIndex = 0; // Index of the instruction being executed
	private int remainingClockCycles = 0;   // Clock cycles remaining for the current instruction
	private boolean stalled = false;        // Stalled state
	private int clockCycle = 1;             // Current clock cycle
	private String currentOPCode = "";      // OPCode of the instruction being executed

	ParseText parseText = new ParseText();


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

		private final IntegerProperty executionTime;

		// Constructor
		public LoadBuffer(int tag) {
			this.tag = new SimpleIntegerProperty(tag);
			this.busy = new SimpleBooleanProperty(false);
			this.address = new SimpleIntegerProperty(0);
			this.executionTime = new SimpleIntegerProperty(0);
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

		public IntegerProperty executionTimeProperty() {
			return executionTime;
		}

		public int getExecutionTime() {
			return executionTime.get();
		}

		public void setExecutionTime(int executionTime) {
			this.executionTime.set(executionTime);
		}
	}

	public class StoreBuffer {

		private final BooleanProperty busy;
		private final IntegerProperty tag;
		private final DoubleProperty V;
		private final IntegerProperty Q;
		private final IntegerProperty address;

		private final IntegerProperty executionTime;


		// Constructor
		public StoreBuffer(int tag) {
			this.tag = new SimpleIntegerProperty(tag);
			this.busy = new SimpleBooleanProperty(false);
			this.V = new SimpleDoubleProperty(0);
			this.Q = new SimpleIntegerProperty(0);
			this.address = new SimpleIntegerProperty(0);
			this.executionTime = new SimpleIntegerProperty(0);
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

		public IntegerProperty executionTimeProperty() {
			return executionTime;
		}

		public int getExecutionTime() {
			return executionTime.get();
		}

		public void setExecutionTime(int executionTime) {
			this.executionTime.set(executionTime);
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

		private final IntegerProperty executionTime;

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
			this.executionTime = new SimpleIntegerProperty(0);
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

		public IntegerProperty executionTimeProperty() {
			return executionTime;
		}

		public int getExecutionTime() {
			return executionTime.get();
		}

		public void setExecutionTime(int executionTime) {
			this.executionTime.set(executionTime);
		}
	}

	public void init() {
		int tag = 1;
		for (int i = 0; i < addReservationStationsSize; i++) {
			ReservationStation addReservationStation = new ReservationStation(tag++);
			addReservationStations.add(addReservationStation);
//			System.out.println("Add: "+addReservationStation.tag+"   "+i);

		}


		for (int i = 0; i < multiplyReservationStationsSize; i++) {
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
		instructions = parseText.parseTextFile();
		Iterator<String> instructionIterator = instructions.iterator();
		String instruction = "";
		while (true) {
			System.out.println("In clock cycle: " + clockCycle);
			if (!stalled)
				instruction = instructionIterator.next();

			execute();
			issue(instruction);
			clockCycle++;
		}
	}


	public void issue(String instruction) throws IOException {
//		init();
		if (stalled)
			return;

		String regex = "[ ,]+";
		String[] parsedInstruction = instruction.split(regex);
		String OPCode = parsedInstruction[0];

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

				stalled = true;
				return ;
//					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
			}

			freeReservationStation.setBusy(true);
			freeReservationStation.setOpcode(OPCode);
			freeReservationStation.setExecutionTime(AddReservationStationExecutionTime);
			String F1 = parsedInstruction[1]; // string as this is where we will save our result
			FloatingRegister F2 = RegisterFile.readFloatRegister(parsedInstruction[2]);
			FloatingRegister F3 = RegisterFile.readFloatRegister(parsedInstruction[3]);
			freeReservationStation.setQj(F2.Qi); // if its 0, woo, if not, it saves it :)
			freeReservationStation.setQk(F3.Qi); // if its 0, woo, if not, it saves it :)
			if (F2.Qi == 0)
				freeReservationStation.setVj(F2.value);
			if (F3.Qi == 0)
				freeReservationStation.setVk(F3.value);


		} else if (parseText.isMultiplyOperation(OPCode)) {
			ReservationStation freeReservationStation = null;
			for (ReservationStation multiplyReservationStation : multiplyReservationStations)
				if (!multiplyReservationStation.isBusy()) {
					freeReservationStation = multiplyReservationStation;
					break;
				}

			if (freeReservationStation == null) {
				logUpdate("Stalled due to full reservation station...");
				stalled = true;
				return ;
//					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
			}

			freeReservationStation.setBusy(true);
			freeReservationStation.setOpcode(OPCode);
			freeReservationStation.setExecutionTime(MultiplyReservationStationExecutionTime);
			String F1 = parsedInstruction[1]; // string as this is where we will save our result
			FloatingRegister F2 = RegisterFile.readFloatRegister(parsedInstruction[2]);
			FloatingRegister F3 = RegisterFile.readFloatRegister(parsedInstruction[3]);
			freeReservationStation.setQj(F2.Qi); // if its 0, woo, if not, it saves it :)
			freeReservationStation.setQk(F3.Qi); // if its 0, woo, if not, it saves it :)
			if (F2.Qi == 0)
				freeReservationStation.setVj(F2.value);
			if (F3.Qi == 0)
				freeReservationStation.setVk(F3.value);

		} else if (OPCode.equals("ADDI") || OPCode.equals("SUBI")) {
			ReservationStation freeReservationStation = null;
			for (ReservationStation addReservationStation : addReservationStations)
				if (!addReservationStation.isBusy()) {
					freeReservationStation = addReservationStation;
					break;
				}

			if (freeReservationStation == null) {
				logUpdate("Stalled due to full reservation station...");
				stalled = true;
				return;
//					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
			}

			freeReservationStation.setBusy(true);
			freeReservationStation.setOpcode(OPCode);
			freeReservationStation.setExecutionTime(AddReservationStationExecutionTime);

			String R1 = parsedInstruction[1]; // string as this is where we will save our result
			IntegerRegister R2 = RegisterFile.readIntegerRegister(parsedInstruction[2]);
			short immediate = Short.valueOf(parsedInstruction[3]);

			freeReservationStation.setQj(R2.Qi); // if its 0, woo, if not, it saves it :)
			freeReservationStation.setQk(0); // since it only reads one register :)
			if (R2.Qi == 0)
				freeReservationStation.setVj(R2.value);
			freeReservationStation.setVk(immediate);

		} else if (parseText.isLoadOperation(OPCode)) {
			// logically, it should be long, since the memory is 64 bits, but a limitation
			// of java is that arrays can only be addressed max by 2^32 - 1 numbers, or an
			// int only,
			LoadBuffer freeLoadBuffer = null;
			for (LoadBuffer loadBuffer : loadBuffers) {
//					System.out.println(loadBuffer.tag + "   " + loadBuffer.busy);
				if (!loadBuffer.isBusy()) {
					freeLoadBuffer = loadBuffer;
					break;
				}
			}

			if (freeLoadBuffer == null) {
				logUpdate("Stalled due to full reservation station...");
				stalled = true;
				return ;
//					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
			}

			String R1 = parsedInstruction[1]; // string as this is where we will save our result
			int memoryAddress = Integer.parseInt(parsedInstruction[2]);

			freeLoadBuffer.setBusy(true);
			freeLoadBuffer.setAddress(memoryAddress);
			freeLoadBuffer.setExecutionTime(LoadBufferExecutionTime);
		} else if (parseText.isStoreOperation(OPCode)) {
			// logically, it should be long, since the memory is 64 bits, but a limitation
			// of java is that arrays can only be addressed max by 2^32 - 1 numbers, or an
			// int only,
			StoreBuffer freeStoreBuffer = null;
			for (StoreBuffer storeBuffer : storeBuffers)
				if (!storeBuffer.isBusy()) {
					freeStoreBuffer = storeBuffer;
					break;
				}

			if (freeStoreBuffer == null) {
				logUpdate("Stalled due to full reservation station...");
				stalled = true;
				return;
//					continue; // ma3rafash el continue deeh hatshta8ala wala la2, pray
			}

			String R1 = parsedInstruction[1]; // string as this is where we will save our result
			int memoryAddress = Integer.parseInt(parsedInstruction[2]);

			freeStoreBuffer.setBusy(true);
			freeStoreBuffer.setAddress(memoryAddress);
			freeStoreBuffer.setExecutionTime(StoreBufferExecutionTime);
		}

		clockCycle++;


}

	private void execute() {
		for(ReservationStation multiplicationStation: multiplyReservationStations)
		{
			if(multiplicationStation.isBusy())
			{
				if(multiplicationStation.getExecutionTime() > 0 && multiplicationStation.getQj() == 0 && multiplicationStation.getQk() == 0)
					multiplicationStation.setExecutionTime(multiplicationStation.getExecutionTime()-1);
				else if (multiplicationStation.getExecutionTime()==0)
				{

				}
			}
		}
		for(ReservationStation additionStation: addReservationStations)
		{
			if(additionStation.isBusy())
			{
				if(additionStation.getExecutionTime()> 0 && additionStation.getQj()== 0 && additionStation.getQk() == 0)
					additionStation.setExecutionTime(additionStation.getExecutionTime()-1);
				else if (additionStation.getExecutionTime()== 0)
				{
				}
			}
		}
		for(LoadBuffer loadBuffer: loadBuffers)
		{
			if(loadBuffer.isBusy())
			{
				if(loadBuffer.getExecutionTime() > 0)
					loadBuffer.setExecutionTime(loadBuffer.getExecutionTime()-1);
				else if (loadBuffer.getExecutionTime()==0)
				{
				}

			}
		}
		for(StoreBuffer storeBuffer: storeBuffers)
		{
			if(storeBuffer.isBusy())
			{
				if(storeBuffer.getExecutionTime() > 0 && storeBuffer.getQ() == 0)
					storeBuffer.setExecutionTime(storeBuffer.getExecutionTime()-1);
				else if(storeBuffer.getExecutionTime()== 0)
				{

				}

			}
		}
//		long R1 = (long) RegisterFile.readRegister(parsedInstruction[1]);
//
//		switch(OPCode)
//				{
//				    case "ADD.D":
//						ALU.addFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), false,
//								freeReservationStation.getTag());
//						break;
//					case "SUB.D":
//						ALU.subtractFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), false,
//								freeReservationStation.getTag());
//						break;
//					case "ADD.S":
//						ALU.addFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), true,
//								freeReservationStation.getTag());
//						break;
//					case "SUB.S":
//						ALU.subtractFloating(F1, freeReservationStation.getVj(), freeReservationStation.getVk(), true,
//								freeReservationStation.getTag());
//						break;
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
//				    case "ADDI":
//						ALU.addImmediate(R1, (long) freeReservationStation.getVj(), (short)freeReservationStation.getVk(), freeReservationStation.getTag());
//						break;
//					case "SUBI":
//						ALU.subtractImmediate(R1, (long) freeReservationStation.getVj(), (short)freeReservationStation.getVk(), freeReservationStation.getTag());
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
		}





}




