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


		private int issueTime; // tracks when did it enter the reservation station; used to determine priority when two instructions are writing at the same time
		private final BooleanProperty busy;
		private final IntegerProperty tag;
		private final IntegerProperty address;

		private final IntegerProperty executionTime;

		// Constructor
		public LoadBuffer(int tag) {
			this.issueTime = 0;
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

		public int getIssueTime() {
			return issueTime;
		}

		public void setIssueTime(int issueTime) {
			this.issueTime = issueTime;
		}
	}

	public class StoreBuffer {

		private int issueTime; // tracks when did it enter the reservation station; used to determine priority when two instructions are writing at the same time
		private final BooleanProperty busy;
		private final IntegerProperty tag;
		private final DoubleProperty V;
		private final IntegerProperty Q;
		private final IntegerProperty address;

		private final IntegerProperty executionTime;


		// Constructor
		public StoreBuffer(int tag) {
			this.issueTime = 0;
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

		public int getIssueTime() {
			return issueTime;
		}

		public void setIssueTime(int issueTime) {
			this.issueTime = issueTime;
		}
	}

	public static class ReservationStation {

		private int tag;
		private int issueTime; // tracks when did it enter the reservation station; used to determine priority when two instructions are writing at the same time
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
			this.tag = 0;
			this.issueTime = 0;
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
//		public IntegerProperty tagProperty() {
//			return tag;
//		}

		public int getTag() {
			return tag;
		}

		public void setTag(int tag) {
			this.tag = tag;
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


		public int getIssueTime() {
			return issueTime;
		}

		public void setIssueTime(int issueTime) {
			this.issueTime = issueTime;
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
		instructions = parseText.parseTextFile();
		Iterator<String> instructionIterator = instructions.iterator();
		String instruction = "";
		while (true) {
			System.out.println("In clock cycle: " + clockCycle);
			instruction = "";
			if (!stalled && instructionIterator.hasNext())
				instruction = instructionIterator.next();
			
			executeAndWrite();
			if(!instruction.equals(""))
				issue(instruction);
			clockCycle++;
		}
	}

	public void issue(String instruction) throws IOException {
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
			RegisterFile.writeTagToRegisterFile(F1, freeReservationStation.getTag());


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
			RegisterFile.writeTagToRegisterFile(F1, freeReservationStation.getTag());

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
			RegisterFile.writeTagToRegisterFile(R1, freeReservationStation.getTag());

		} else if (parseText.isLoadOperation(OPCode)) {
			// logically, it should be long, since the memory is 64 bits, but a limitation
			// of java is that arrays can only be addressed max by 2^32 - 1 numbers, or an
			// int only,
			LoadBuffer freeLoadBuffer = null;
			for (LoadBuffer loadBuffer : loadBuffers) {
				if (!loadBuffer.isBusy()) {
					freeLoadBuffer = loadBuffer;
					break;
				}
			}

			if (freeLoadBuffer == null) {
				logUpdate("Stalled due to full reservation station...");
				stalled = true;
				return ;
			}

			String R1 = parsedInstruction[1]; // can be integer or floating register
			int memoryAddress = Integer.parseInt(parsedInstruction[2]);

			freeLoadBuffer.setBusy(true);
			freeLoadBuffer.setAddress(memoryAddress);
			freeLoadBuffer.setExecutionTime(LoadBufferExecutionTime);
			RegisterFile.writeTagToRegisterFile(R1, freeLoadBuffer.getTag());

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
			}

			int memoryAddress = Integer.parseInt(parsedInstruction[2]);
			if(parseText.isIntegerStoreOperation(OPCode))
			{
				IntegerRegister R1 = RegisterFile.readIntegerRegister(parsedInstruction[1]);

				freeStoreBuffer.setQ(R1.Qi); // if its 0, woo, if not, it saves it :)
				freeStoreBuffer.setQ(0); // since it only reads one register :)
				if (R1.Qi == 0)
					freeStoreBuffer.setV(R1.value);
			}
			// don't need the if since the else will always be true, but it is left for readibility's sake
			else if(parseText.isFloatStoreOperation(OPCode)) 
			{
				FloatingRegister F1 = RegisterFile.readFloatRegister(parsedInstruction[1]);

				freeStoreBuffer.setQ(F1.Qi); // if its 0, woo, if not, it saves it :)
				freeStoreBuffer.setQ(0); // since it only reads one register :)
				if (F1.Qi == 0)
					freeStoreBuffer.setV(F1.value);
			}

			freeStoreBuffer.setBusy(true);
			freeStoreBuffer.setAddress(memoryAddress);
			freeStoreBuffer.setExecutionTime(StoreBufferExecutionTime);
		}

		clockCycle++;
}

	// method mainly does two things: 1) decrement the execution time by 1 if it has all its operands (ie Qj and Qi are 0)
    //								  2) if the execution time is 0, compute and publish the result depending on it priority
	// the priority of a instruction is determined by the issue time, the earlier the instruction came to the station, the higher
	// its priority. If two instructions have the same priority, then we determine its priority by the opcode, where load has 
	// the highest priority, then the multiplication, then the addition, then finally the division. If both instructions are the
	// same opcode (3ashan 5awal), then we choose the one with the lower tag number (so M1 has a higher priority than M2 for example)
	private void executeAndWrite() throws Exception {
		int lowestIssueTime = Integer.MAX_VALUE;
		int theStrongestOneAfterGojoOfCourse = -1;// The tag of the one that will publish (based on priority above)
		for(LoadBuffer loadBuffer: loadBuffers)
		{
			if(loadBuffer.isBusy())
			{
				if(loadBuffer.getExecutionTime() > 0)
					loadBuffer.setExecutionTime(loadBuffer.getExecutionTime()-1);
				else if (loadBuffer.getExecutionTime()==0 && lowestIssueTime > loadBuffer.getIssueTime())
				{
					lowestIssueTime = loadBuffer.getIssueTime();
					theStrongestOneAfterGojoOfCourse = loadBuffer.getTag();
				}

			}
		}

		for(ReservationStation multiplicationStation: multiplyReservationStations)
		{
			if(multiplicationStation.isBusy())
			{
				if(multiplicationStation.getExecutionTime() > 0 && multiplicationStation.getQj() == 0 && multiplicationStation.getQk() == 0)
					multiplicationStation.setExecutionTime(multiplicationStation.getExecutionTime()-1);
				else if (multiplicationStation.getExecutionTime()==0 && lowestIssueTime > multiplicationStation.getIssueTime())
				{
					lowestIssueTime = multiplicationStation.getIssueTime();
					theStrongestOneAfterGojoOfCourse = multiplicationStation.getTag();
				}
			}
		}

		for(ReservationStation additionStation: addReservationStations)
		{
			if(additionStation.isBusy())
			{
				if(additionStation.getExecutionTime()> 0 && additionStation.getQj()== 0 && additionStation.getQk() == 0)
					additionStation.setExecutionTime(additionStation.getExecutionTime()-1);
				else if (additionStation.getExecutionTime()== 0 && lowestIssueTime > additionStation.getIssueTime())
				{
					lowestIssueTime = additionStation.getIssueTime();
					theStrongestOneAfterGojoOfCourse = additionStation.getTag();
				}
			}
		}

		for(StoreBuffer storeBuffer: storeBuffers)
		{
			if(storeBuffer.isBusy())
			{
				if(storeBuffer.getExecutionTime() > 0 && storeBuffer.getQ() == 0)
					storeBuffer.setExecutionTime(storeBuffer.getExecutionTime()-1);
				else if(storeBuffer.getExecutionTime()== 0 && lowestIssueTime > storeBuffer.getIssueTime())
				{
					lowestIssueTime = storeBuffer.getIssueTime();
					theStrongestOneAfterGojoOfCourse = storeBuffer.getTag();
				}

			}
		}
		
		// the one who will publish
		if(theStrongestOneAfterGojoOfCourse != -1)
		{
			// if it is in the add reservation stations
			if(theStrongestOneAfterGojoOfCourse <= addReservationStationsSize)
			{
				ReservationStation publishingReservationStation = getReservationStationWithTag(theStrongestOneAfterGojoOfCourse);
				if(publishingReservationStation == null)
					throw new Exception("For some reason, one of the add reservation stations is not intialized");
				publishingReservationStation.setBusy(false);
				// compute result and publish ... 

				
			}
			
			// if it is in the multiplcation reservation stations, and so on...
		}
	}

	public ReservationStation getReservationStationWithTag(int tag)
	{
		for(ReservationStation additionStation: addReservationStations)
			if(additionStation.getTag() == tag)
				return additionStation;

		for(ReservationStation multiplicationStation: multiplyReservationStations)
			if(multiplicationStation.getTag() == tag)
				return multiplicationStation;

		return null;
	}





}




