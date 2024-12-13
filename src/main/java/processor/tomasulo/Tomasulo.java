package processor.tomasulo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import processor.tomasulo.RegisterFile.FloatingRegister;
import processor.tomasulo.RegisterFile.IntegerRegister;

public class Tomasulo
{
	public class LoadBuffer
	{

		private final IntegerProperty issueTime; // tracks when did it enter the reservation station; used to
		// determine priority when two instructions are writing at the same time
		private final StringProperty opcode;

		private final BooleanProperty busy;
		private final IntegerProperty tag;
		private final IntegerProperty address;

		private final IntegerProperty QAddress;
		private final IntegerProperty executionTime;

		public boolean firstExecution;
		public int fillingCache;

		// Constructor
		public LoadBuffer(int tag)
		{
			this.issueTime = new SimpleIntegerProperty(-1);
			this.opcode = new SimpleStringProperty("");
			this.tag = new SimpleIntegerProperty(tag);
			this.busy = new SimpleBooleanProperty(false);
			this.address = new SimpleIntegerProperty(0);
			this.executionTime = new SimpleIntegerProperty(0);
			firstExecution = true;
			fillingCache = 0;
			this.QAddress = new SimpleIntegerProperty(0);
		}

		// Getter and Setter for busy
		public boolean isBusy()
		{
			return busy.get();
		}

		public void setBusy(boolean busy)
		{
			this.busy.set(busy);
		}

		public BooleanProperty busyProperty()
		{
			return busy;
		}

		// Getter and Setter for tag
		public int getTag()
		{
			return tag.get();
		}

		public void setTag(int tag)
		{
			this.tag.set(tag);
		}

		public IntegerProperty tagProperty()
		{
			return tag;
		}

		// Getter and Setter for address
		public int getAddress()
		{
			return address.get();
		}

		public void setAddress(int address)
		{
			this.address.set(address);
		}

		public IntegerProperty QAddressProperty()
		{
			return QAddress;
		}

		public int getQAddress()
		{
			return QAddress.get();
		}

		public void setQAddress(int q)
		{
			this.QAddress.set(q);
		}

		public IntegerProperty addressProperty()
		{
			return address;
		}

		public IntegerProperty executionTimeProperty()
		{
			return executionTime;
		}

		public int getExecutionTime()
		{
			return executionTime.get();
		}

		public void setExecutionTime(int executionTime)
		{
			this.executionTime.set(executionTime);
		}

		public IntegerProperty issueTimeProperty()
		{
			return issueTime;
		}

		public int getIssueTime()
		{
			return issueTime.get();
		}

		public void setIssueTime(int issueTime)
		{
			this.issueTime.set(issueTime);
		}

		public StringProperty opcodeProperty()
		{
			return opcode;
		}

		public String getOpcode()
		{
			return opcode.get();
		}

		public void setOpcode(String opcode)
		{
			this.opcode.set(opcode);
		}
	}

	public class StoreBuffer
	{

		private final IntegerProperty issueTime; // tracks when did it enter the reservation station; used to
		// determine
		// priority
		// when two instructions are writing at the same time
		private final BooleanProperty busy;
		private final IntegerProperty tag;
		private final DoubleProperty V;
		private final IntegerProperty Q;
		private final IntegerProperty QAddress;
		private final IntegerProperty address;
		private final StringProperty opcode;

		private final IntegerProperty executionTime;
		public boolean firstExecution;
		public int fillingCache;
		public long vLong;

		// Constructor
		public StoreBuffer(int tag)
		{
			this.issueTime = new SimpleIntegerProperty(-1);
			this.opcode = new SimpleStringProperty("");
			this.tag = new SimpleIntegerProperty(tag);
			this.busy = new SimpleBooleanProperty(false);
			this.V = new SimpleDoubleProperty(0);
			this.Q = new SimpleIntegerProperty(0);
			this.address = new SimpleIntegerProperty(0);
			this.executionTime = new SimpleIntegerProperty(0);
			this.QAddress = new SimpleIntegerProperty(0);
			this.fillingCache = 0;
			this.firstExecution = true;
			this.vLong = 0;
		}

		// Getter and Setter for busy
		public BooleanProperty busyProperty()
		{
			return busy;
		}

		public boolean isBusy()
		{
			return busy.get();
		}

		public void setBusy(boolean busy)
		{
			this.busy.set(busy);
		}

		// Getter and Setter for tag
		public IntegerProperty tagProperty()
		{
			return tag;
		}

		public int getTag()
		{
			return tag.get();
		}

		public void setTag(int tag)
		{
			this.tag.set(tag);
		}

		// Getter and Setter for V
		public DoubleProperty VProperty()
		{
			return V;
		}

		public double getV()
		{
			return V.get();
		}

		public void setV(double v)
		{
			this.V.set(v);
		}

		// Getter and Setter for Q
		public IntegerProperty QProperty()
		{
			return Q;
		}

		public int getQ()
		{
			return Q.get();
		}

		public void setQ(int q)
		{
			this.Q.set(q);
		}

		public IntegerProperty QAddressProperty()
		{
			return QAddress;
		}

		public int getQAddress()
		{
			return QAddress.get();
		}

		public void setQAddress(int q)
		{
			this.QAddress.set(q);
		}

		// Getter and Setter for address
		public IntegerProperty addressProperty()
		{
			return address;
		}

		public int getAddress()
		{
			return address.get();
		}

		public void setAddress(int address)
		{
			this.address.set(address);
		}

		public IntegerProperty executionTimeProperty()
		{
			return executionTime;
		}

		public int getExecutionTime()
		{
			return executionTime.get();
		}

		public void setExecutionTime(int executionTime)
		{
			this.executionTime.set(executionTime);
		}

		public StringProperty opcodeProperty()
		{
			return opcode;
		}

		public String getOpcode()
		{
			return opcode.get();
		}

		public void setOpcode(String opcode)
		{
			this.opcode.set(opcode);
		}

		public IntegerProperty issueTimeProperty()
		{
			return issueTime;
		}

		public int getIssueTime()
		{
			return issueTime.get();
		}

		public void setIssueTime(int issueTime)
		{
			this.issueTime.set(issueTime);
		}
	}

	public static class IntegerReservationStation
	{
		private int tag;
		private final IntegerProperty issueTime; // tracks when did it enter the reservation station; used to
		// determine priority when two instructions are writing at the same
		// time
		private final BooleanProperty busy;
		private final StringProperty opcode; // Using StringProperty instead of a plain string.
		private final LongProperty vj;
		private final LongProperty vk;
		private final IntegerProperty qj;
		private final IntegerProperty qk;
		private final IntegerProperty address;
		private final IntegerProperty executionTime;

		// Constructor
		public IntegerReservationStation(int tag)
		{
			this.tag = tag;
			this.issueTime = new SimpleIntegerProperty(-1);
			this.busy = new SimpleBooleanProperty(false);
			this.opcode = new SimpleStringProperty("");
			this.vj = new SimpleLongProperty(0);
			this.vk = new SimpleLongProperty(0);
			this.qj = new SimpleIntegerProperty(0);
			this.qk = new SimpleIntegerProperty(0);
			this.address = new SimpleIntegerProperty(0);
			this.executionTime = new SimpleIntegerProperty(0);
		}

		public int getTag()
		{
			return tag;
		}

		public void setTag(int tag)
		{
			this.tag = tag;
		}

		// Getter and Setter for busy
		public BooleanProperty busyProperty()
		{
			return busy;
		}

		public boolean isBusy()
		{
			return busy.get();
		}

		public void setBusy(boolean busy)
		{
			this.busy.set(busy);
		}

		// Getter and Setter for opcode
		public StringProperty opcodeProperty()
		{
			return opcode;
		}

		public String getOpcode()
		{
			return opcode.get();
		}

		public void setOpcode(String opcode)
		{
			this.opcode.set(opcode);
		}

		// Getter and Setter for vj
		public LongProperty vjProperty()
		{
			return vj;
		}

		public long getVj()
		{
			return vj.get();
		}

		public void setVj(long vj)
		{
			this.vj.set(vj);
		}

		// Getter and Setter for vk
		public LongProperty vkProperty()
		{
			return vk;
		}

		public long getVk()
		{
			return vk.get();
		}

		public void setVk(long vk)
		{
			this.vk.set(vk);
		}

		// Getter and Setter for qj
		public IntegerProperty qjProperty()
		{
			return qj;
		}

		public int getQj()
		{
			return qj.get();
		}

		public void setQj(int qj)
		{
			this.qj.set(qj);
		}

		// Getter and Setter for qk
		public IntegerProperty qkProperty()
		{
			return qk;
		}

		public int getQk()
		{
			return qk.get();
		}

		public void setQk(int qk)
		{
			this.qk.set(qk);
		}

		// Getter and Setter for address
		public IntegerProperty addressProperty()
		{
			return address;
		}

		public int getAddress()
		{
			return address.get();
		}

		public void setAddress(int address)
		{
			this.address.set(address);
		}

		public IntegerProperty executionTimeProperty()
		{
			return executionTime;
		}

		public int getExecutionTime()
		{
			return executionTime.get();
		}

		public void setExecutionTime(int executionTime)
		{
			this.executionTime.set(executionTime);
		}

		public IntegerProperty issueTimeProperty()
		{
			return issueTime;
		}

		public int getIssueTime()
		{
			return issueTime.get();
		}

		public void setIssueTime(int issueTime)
		{
			this.issueTime.set(issueTime);
		}
	}

	public static class ReservationStation
	{
		private int tag;
		private final IntegerProperty issueTime; // tracks when did it enter the reservation station; used to
		// determine priority when two instructions are writing at the same
		// time
		private final BooleanProperty busy;
		private final StringProperty opcode; // Using StringProperty instead of a plain string.
		private final DoubleProperty vj;
		private final DoubleProperty vk;
		private final IntegerProperty qj;
		private final IntegerProperty qk;
		private final IntegerProperty address;

		private final IntegerProperty executionTime;

		// Constructor
		public ReservationStation(int tag)
		{
			this.tag = tag;
			this.issueTime = new SimpleIntegerProperty(-1);
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

		public int getTag()
		{
			return tag;
		}

		public void setTag(int tag)
		{
			this.tag = tag;
		}

		// Getter and Setter for busy
		public BooleanProperty busyProperty()
		{
			return busy;
		}

		public boolean isBusy()
		{
			return busy.get();
		}

		public void setBusy(boolean busy)
		{
			this.busy.set(busy);
		}

		// Getter and Setter for opcode
		public StringProperty opcodeProperty()
		{
			return opcode;
		}

		public String getOpcode()
		{
			return opcode.get();
		}

		public void setOpcode(String opcode)
		{
			this.opcode.set(opcode);
		}

		// Getter and Setter for vj
		public DoubleProperty vjProperty()
		{
			return vj;
		}

		public double getVj()
		{
			return vj.get();
		}

		public void setVj(double vj)
		{
			this.vj.set(vj);
		}

		// Getter and Setter for vk
		public DoubleProperty vkProperty()
		{
			return vk;
		}

		public double getVk()
		{
			return vk.get();
		}

		public void setVk(double vk)
		{
			this.vk.set(vk);
		}

		// Getter and Setter for qj
		public IntegerProperty qjProperty()
		{
			return qj;
		}

		public int getQj()
		{
			return qj.get();
		}

		public void setQj(int qj)
		{
			this.qj.set(qj);
		}

		// Getter and Setter for qk
		public IntegerProperty qkProperty()
		{
			return qk;
		}

		public int getQk()
		{
			return qk.get();
		}

		public void setQk(int qk)
		{
			this.qk.set(qk);
		}

		// Getter and Setter for address
		public IntegerProperty addressProperty()
		{
			return address;
		}

		public int getAddress()
		{
			return address.get();
		}

		public void setAddress(int address)
		{
			this.address.set(address);
		}

		public IntegerProperty executionTimeProperty()
		{
			return executionTime;
		}

		public int getExecutionTime()
		{
			return executionTime.get();
		}

		public void setExecutionTime(int executionTime)
		{
			this.executionTime.set(executionTime);
		}

		public IntegerProperty issueTimeProperty()
		{
			return issueTime;
		}

		public int getIssueTime()
		{
			return issueTime.get();
		}

		public void setIssueTime(int issueTime)
		{
			this.issueTime.set(issueTime);
		}
	}

	public static class BranchStation
	{

		private final BooleanProperty busy;
		private final StringProperty opcode; // Using StringProperty instead of a plain string.
		private final DoubleProperty vj;
		private final DoubleProperty vk;
		private final IntegerProperty qj;
		private final IntegerProperty qk;
		private final IntegerProperty address;

		private final IntegerProperty executionTime;

		// Constructor
		public BranchStation()
		{
			this.busy = new SimpleBooleanProperty(false);
			this.opcode = new SimpleStringProperty("");
			this.vj = new SimpleDoubleProperty(0.0);
			this.vk = new SimpleDoubleProperty(0.0);
			this.qj = new SimpleIntegerProperty(0);
			this.qk = new SimpleIntegerProperty(0);
			this.address = new SimpleIntegerProperty(0);
			this.executionTime = new SimpleIntegerProperty(0);
		}

		// Getter and Setter for busy
		public BooleanProperty busyProperty()
		{
			return busy;
		}

		public boolean isBusy()
		{
			return busy.get();
		}

		public void setBusy(boolean busy)
		{
			this.busy.set(busy);
		}

		// Getter and Setter for opcode
		public StringProperty opcodeProperty()
		{
			return opcode;
		}

		public String getOpcode()
		{
			return opcode.get();
		}

		public void setOpcode(String opcode)
		{
			this.opcode.set(opcode);
		}

		// Getter and Setter for vj
		public DoubleProperty vjProperty()
		{
			return vj;
		}

		public double getVj()
		{
			return vj.get();
		}

		public void setVj(double vj)
		{
			this.vj.set(vj);
		}

		// Getter and Setter for vk
		public DoubleProperty vkProperty()
		{
			return vk;
		}

		public double getVk()
		{
			return vk.get();
		}

		public void setVk(double vk)
		{
			this.vk.set(vk);
		}

		// Getter and Setter for qj
		public IntegerProperty qjProperty()
		{
			return qj;
		}

		public int getQj()
		{
			return qj.get();
		}

		public void setQj(int qj)
		{
			this.qj.set(qj);
		}

		// Getter and Setter for qk
		public IntegerProperty qkProperty()
		{
			return qk;
		}

		public int getQk()
		{
			return qk.get();
		}

		public void setQk(int qk)
		{
			this.qk.set(qk);
		}

		// Getter and Setter for address
		public IntegerProperty addressProperty()
		{
			return address;
		}

		public int getAddress()
		{
			return address.get();
		}

		public void setAddress(int address)
		{
			this.address.set(address);
		}

		public IntegerProperty executionTimeProperty()
		{
			return executionTime;
		}

		public int getExecutionTime()
		{
			return executionTime.get();
		}

		public void setExecutionTime(int executionTime)
		{
			this.executionTime.set(executionTime);
		}

	}

	private Consumer<String> updateLog;

	public static int addReservationStationsSize = 4;
	public static int multiplyReservationStationsSize = 4;
	public static int loadBuffersSize = 4;
	public static int storeBuffersSize = 4;
	public static int immediateReservationStationSize = 2;

	public static int blockSize = 8;
	public static int cacheSize = 128;

	public static int LoadBufferExecutionTime = 2;

	public static int StoreBufferExecutionTime = 1;

	public static int AddReservationStationExecutionTime = 2;

	public static int AddImmReservationStationExecutionTime = 1;

	public static int SubReservationStationExecutionTime = 2;

	public static int SubImmReservationStationExecutionTime = 1;

	public static int MultiplyReservationStationExecutionTime = 4;

	public static int DivideReservationStationExecutionTime = 6;

	public static int BranchReservationStationExecutionTime = 3;

	public static RegisterFile registerFile = new RegisterFile();
	public static Memory memory = new Memory();
	public static Cache cache;
	public static ALU alu = new ALU();

	public static ArrayList<Instructon> instructions = new ArrayList<>(); // these are all the
																			// instructions, not yet
																			// executed :)
																			// size should be entered by user
	public static ObservableList<ReservationStation> addReservationStations = FXCollections.observableArrayList();

	public static ObservableList<ReservationStation> multiplyReservationStations = FXCollections.observableArrayList();

	public static ObservableList<IntegerReservationStation> integerReservationStations = FXCollections
			.observableArrayList();

	public static ObservableList<LoadBuffer> loadBuffers = FXCollections.observableArrayList();
	public static ObservableList<StoreBuffer> storeBuffers = FXCollections.observableArrayList();

	public static ObservableList<BranchStation> branchStation = FXCollections.observableArrayList();

	public int currentInstructionIndex = 0;

	private boolean alreadyDecremented = false;

	private boolean fullAddStations = false;
	private boolean fullMultiplyStations = false;
	private boolean fullLoadBuffers = false;
	private boolean fullStoreBuffers = false;
	private boolean fullImmediateStations = false;
	private boolean isBranching = false;
	private boolean stalled = false; // Stalled state
	private IntegerProperty clockCycle = new SimpleIntegerProperty(1); // Clock cycle as IntegerProperty

	ParseText parseText = new ParseText();

	public void setRegisterFile(RegisterFile registerFile)
	{
		Tomasulo.registerFile = registerFile;
	}

	public RegisterFile getRegisterFile()
	{
		return Tomasulo.registerFile;
	}

	// Getter for clock cycle
	public IntegerProperty clockCycleProperty()
	{
		return clockCycle;
	}

	public int getClockCycle()
	{
		return clockCycle.get();
	}

	public void setClockCycle(int clockCycle)
	{
		this.clockCycle.set(clockCycle);
	}

	public static ArrayList<Instructon> getInstructions()
	{

		return instructions;
	}

	public void incrementClockCycle()
	{
		setClockCycle(getClockCycle() + 1); // Increment the clock cycle
	}

	public void setUpdateLogCallback(Consumer<String> callback)
	{
		this.updateLog = callback;
	}

	private void logUpdate(String message)
	{
		if (updateLog != null) updateLog.accept(message + "\n");

	}

	public void setupInstructions() throws IOException
	{
		Tomasulo.instructions = parseText.parseTextFile();
	}

	public String getTagString(int tag)
	{
		// Check the tag's range and return the corresponding string
		if (tag == 0) return "0";

		if (tag <= addReservationStationsSize)
		{
			// Tag falls within the "Add" Reservation Stations range
			return "A" + tag;
		}
		else if (tag <= addReservationStationsSize + multiplyReservationStationsSize)
		{
			// Tag falls within the "Multiply" Reservation Stations range
			return "M" + (tag - addReservationStationsSize); // Adjust the tag for the M range
		}
		else if (tag <= addReservationStationsSize + multiplyReservationStationsSize + loadBuffersSize)
		{
			// Tag falls within the "Load" Buffers range
			return "L" + (tag - addReservationStationsSize - multiplyReservationStationsSize); // Adjust the tag for the L range
		}
		else if (tag <= addReservationStationsSize + multiplyReservationStationsSize + loadBuffersSize
				+ storeBuffersSize)
		{
			// Tag falls within the "Store" Buffers range
			return "S" + (tag - addReservationStationsSize - multiplyReservationStationsSize - loadBuffersSize); // Adjust the tag for the S range
		}
		else if (tag <= addReservationStationsSize + multiplyReservationStationsSize + loadBuffersSize
				+ storeBuffersSize + immediateReservationStationSize)
		{
			// Tag falls within the "Immediate" Reservation Stations range
			return "I" + (tag - addReservationStationsSize - multiplyReservationStationsSize - loadBuffersSize
					- storeBuffersSize); // Adjust the tag for the I range
		}
		else
		{
			// If the tag is out of bounds (greater than all ranges)
			return "invalid tag";
		}
	}

	public void init() throws IOException
	{
		int tag = 1;
		for (int i = 0; i < addReservationStationsSize; i++)
		{
			ReservationStation addReservationStation = new ReservationStation(tag++);
			addReservationStations.add(addReservationStation);

		}

		for (int i = 0; i < multiplyReservationStationsSize; i++)
		{
			ReservationStation multiplyReservationStation = new ReservationStation(tag++);
			multiplyReservationStations.add(multiplyReservationStation);
		}

		for (int i = 0; i < loadBuffersSize; i++)
		{
			LoadBuffer loadBuffer = new LoadBuffer(tag++);
			loadBuffers.add(loadBuffer);
		}

		for (int i = 0; i < storeBuffersSize; i++)
		{
			StoreBuffer storeBuffer = new StoreBuffer(tag++);
			storeBuffers.add(storeBuffer);
		}

		for (int i = 0; i < immediateReservationStationSize; i++)
		{
			IntegerReservationStation immediateReservationStation = new IntegerReservationStation(tag++);
			integerReservationStations.add(immediateReservationStation);
		}

		BranchStation newBranchStation = new BranchStation();
		branchStation.add(newBranchStation);
		cache = new Cache(cacheSize, blockSize);
	}

	public void executeCycle() throws IOException
	{
		Instructon instruction = new Instructon("", false);

		System.out.println("In clock cycle: " + getClockCycle());

		// Fetch the next instruction if not stalled and there are remaining instructions
		if (!stalled && currentInstructionIndex < instructions.size())
		{
			instruction = instructions.get(currentInstructionIndex);
			currentInstructionIndex++; // Move to the next instruction
			alreadyDecremented = false; // Reset the flag when moving to the next instruction
		}

		try
		{
			executeAndWrite();

			// Issue the instruction if it's valid and not stalled
			if (!instruction.getInstruction().equals(""))
			{
				issue(instruction);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		// Update the stalled flag based on system state
		stalled = fullAddStations || fullLoadBuffers || fullMultiplyStations || fullStoreBuffers
				|| fullImmediateStations || isBranching;

		// Decrement currentInstructionIndex only once if stalled
		if (stalled && !alreadyDecremented)
		{
			currentInstructionIndex--;
			alreadyDecremented = true; // Mark that decrement has been applied
		}

		incrementClockCycle();
	}

	public void issue(Instructon instruction) throws IOException
	{
		if (stalled) return;

		logUpdate("Attemping to issue instruction: " + instruction.getInstruction());
		String regex = "[ ,]+";
		String[] parsedInstruction = instruction.getInstruction().split(regex);
		for (Instructon instructionChange : instructions)
		{
			instructionChange.setCurrent(false); // Assuming the current is a String property, set it to "false"
		}

		instruction.setCurrent(true);
		String OPCode = parsedInstruction[0];

		if (parseText.isFloatOperation(OPCode))
		{
			issueFloatAdd(OPCode, parsedInstruction);
		}
		else if (parseText.isIntegerOperation((OPCode)))
		{
			issueImmediate(OPCode, parsedInstruction);
		}
		else if (parseText.isMultiplyOrDivideOperation(OPCode))
		{
			issueMultiply(OPCode, parsedInstruction);
		}
		else if (parseText.isLoadOperation(OPCode))
		{
			issueLoad(OPCode, parsedInstruction);
		}
		else if (parseText.isStoreOperation(OPCode))
		{
			issueStore(OPCode, parsedInstruction);
		}
		else if (parseText.isBranchOperation(OPCode))
		{
			issueBranch(OPCode, parsedInstruction);
		}

	}

	private void issueFloatAdd(String OPCode, String[] parsedInstruction)
	{
		// check for empty addition stations, if none are avaliable, stall
		ReservationStation freeReservationStation = null;
		for (ReservationStation addReservationStation : addReservationStations)
			if (!addReservationStation.isBusy())
			{
				freeReservationStation = addReservationStation;
				break;
			}

		if (freeReservationStation == null)
		{
			logUpdate("Stalled due to full reservation station...");
			fullAddStations = true;
			return;
		}

		freeReservationStation.setBusy(true);
		freeReservationStation.setOpcode(OPCode);
		if (parseText.isFloatAdditionOperation(OPCode))
			freeReservationStation.setExecutionTime(AddReservationStationExecutionTime);
		else if (parseText.isFloatSubtractionOperation(OPCode))
			freeReservationStation.setExecutionTime(SubReservationStationExecutionTime);

		freeReservationStation.setIssueTime(getClockCycle());
		String F1 = parsedInstruction[1]; // string as this is where we will save our result
		FloatingRegister F2 = RegisterFile.readFloatRegister(parsedInstruction[2]);
		FloatingRegister F3 = RegisterFile.readFloatRegister(parsedInstruction[3]);
		freeReservationStation.setQj(F2.getQi()); // if its 0, woo, if not, it saves it :)
		freeReservationStation.setQk(F3.getQi()); // if its 0, woo, if not, it saves it :)
		if (F2.getQi() == 0) freeReservationStation.setVj(F2.getValue());
		if (F3.getQi() == 0) freeReservationStation.setVk(F3.getValue());
		RegisterFile.writeTagToRegisterFile(F1, freeReservationStation.getTag());

	}

	private void issueImmediate(String OPCode, String[] parsedInstruction)
	{
		// check for empty addition stations, if none are avaliable, stall
		IntegerReservationStation freeReservationStation = null;
		for (IntegerReservationStation immediateReservationStation : integerReservationStations)
			if (!immediateReservationStation.isBusy())
			{
				freeReservationStation = immediateReservationStation;
				break;
			}

		if (freeReservationStation == null)
		{
			logUpdate("Stalled due to full reservation station...");
			fullImmediateStations = true;
			return;
		}

		freeReservationStation.setBusy(true);
		freeReservationStation.setOpcode(OPCode);
		freeReservationStation.setIssueTime(getClockCycle());

		if (parseText.isIntegerAdditionOperation(OPCode))
			freeReservationStation.setExecutionTime(AddImmReservationStationExecutionTime);
		else if (parseText.isIntegerSubtractionOperation(OPCode))
			freeReservationStation.setExecutionTime(SubImmReservationStationExecutionTime);

		String R1 = parsedInstruction[1]; // string as this is where we will save our result
		IntegerRegister R2 = RegisterFile.readIntegerRegister(parsedInstruction[2]);
		short immediate = Short.valueOf(parsedInstruction[3]);

		freeReservationStation.setQj(R2.getQi()); // if its 0, woo, if not, it saves it :)
		freeReservationStation.setQk(0); // since it only reads one register :)
		if (R2.getQi() == 0) freeReservationStation.setVj(R2.getValue());
		freeReservationStation.setVk(immediate);
		RegisterFile.writeTagToRegisterFile(R1, freeReservationStation.getTag());

	}

	private void issueMultiply(String OPCode, String[] parsedInstruction)
	{
		ReservationStation freeReservationStation = null;
		for (ReservationStation multiplyReservationStation : multiplyReservationStations)
			if (!multiplyReservationStation.isBusy())
			{
				freeReservationStation = multiplyReservationStation;
				break;
			}

		if (freeReservationStation == null)
		{
			logUpdate("Stalled due to full reservation station...");
			fullMultiplyStations = true;
			stalled = true;
			return;
		}

		freeReservationStation.setBusy(true);
		freeReservationStation.setOpcode(OPCode);
		if (parseText.isMultiplyOperation(OPCode))
			freeReservationStation.setExecutionTime(MultiplyReservationStationExecutionTime);
		if (parseText.isDivideOperation(OPCode))
			freeReservationStation.setExecutionTime(DivideReservationStationExecutionTime);

		freeReservationStation.setIssueTime(getClockCycle());
		String F1 = parsedInstruction[1]; // string as this is where we will save our result
		FloatingRegister F2 = RegisterFile.readFloatRegister(parsedInstruction[2]);
		FloatingRegister F3 = RegisterFile.readFloatRegister(parsedInstruction[3]);
		freeReservationStation.setQj(F2.getQi()); // if its 0, woo, if not, it saves it :)
		freeReservationStation.setQk(F3.getQi()); // if its 0, woo, if not, it saves it :)
		if (F2.getQi() == 0) freeReservationStation.setVj(F2.getValue());
		if (F3.getQi() == 0) freeReservationStation.setVk(F3.getValue());
		RegisterFile.writeTagToRegisterFile(F1, freeReservationStation.getTag());

	}

	private void issueLoad(String OPCode, String[] parsedInstruction)
	{ /*
		 * logically, it should be long, since the memory is 64 bits, but a limitation of java
		 * is that arrays can only be addressed max by 2^32 - 1 numbers, or an int only
		 */
		LoadBuffer freeLoadBuffer = null;
		for (LoadBuffer loadBuffer : loadBuffers)
		{
			if (!loadBuffer.isBusy())
			{
				freeLoadBuffer = loadBuffer;
				break;
			}
		}

		if (freeLoadBuffer == null)
		{
			logUpdate("Stalled due to full reservation station...");
			fullLoadBuffers = true;
			return;
		}

		String R1 = parsedInstruction[1]; // can be integer or floating register
		if (parsedInstruction[2].contains("R"))
		{
			IntegerRegister R2 = RegisterFile.readIntegerRegister(parsedInstruction[2]);
			freeLoadBuffer.setQAddress(R2.getQi());
			freeLoadBuffer.setAddress((int) R2.getValue());
		}
		else
		{
			freeLoadBuffer.setAddress(Integer.parseInt(parsedInstruction[2]));
		}

		freeLoadBuffer.setBusy(true);
		freeLoadBuffer.setExecutionTime(LoadBufferExecutionTime);
		freeLoadBuffer.setOpcode(OPCode);
		freeLoadBuffer.setIssueTime(getClockCycle());
		RegisterFile.writeTagToRegisterFile(R1, freeLoadBuffer.getTag());

	}

	private void issueStore(String OPCode, String[] parsedInstruction)
	{
		/*
		 * logically, it should be long, since the memory is 64 bits, but a limitation of java
		 * is that arrays can only be addressed max by 2^32 - 1 numbers, or an int only
		 */
		StoreBuffer freeStoreBuffer = null;
		for (StoreBuffer storeBuffer : storeBuffers)
			if (!storeBuffer.isBusy())
			{
				freeStoreBuffer = storeBuffer;
				break;
			}

		if (freeStoreBuffer == null)
		{
			logUpdate("Stalled due to full reservation station...");
			fullStoreBuffers = true;
			return;
		}

		if (parsedInstruction[2].contains("R"))
		{
			IntegerRegister R2 = RegisterFile.readIntegerRegister(parsedInstruction[2]);
			freeStoreBuffer.setQAddress(R2.getQi());
			freeStoreBuffer.setAddress((int) R2.getValue());
		}
		else
		{
			freeStoreBuffer.setAddress(Integer.parseInt(parsedInstruction[2]));
		}

		if (parseText.isIntegerStoreOperation(OPCode))
		{
			IntegerRegister R1 = RegisterFile.readIntegerRegister(parsedInstruction[1]);
			freeStoreBuffer.setQ(R1.getQi()); // if its 0, woo, if not, it saves it :)
			if (R1.getQi() == 0) {
				freeStoreBuffer.setV(R1.getValue());
				freeStoreBuffer.vLong = R1.getValue();
			}
		}
		// don't need the if since the else will always be true, but it is
		// left for readibility's sake
		else if (parseText.isFloatStoreOperation(OPCode))
		{
			FloatingRegister F1 = RegisterFile.readFloatRegister(parsedInstruction[1]);
			freeStoreBuffer.setQ(F1.getQi()); // if its 0, woo, if not, it saves it :)
			if (F1.getQi() == 0) freeStoreBuffer.setV(F1.getValue());
		}

		freeStoreBuffer.setBusy(true);
		freeStoreBuffer.setExecutionTime(StoreBufferExecutionTime);
		freeStoreBuffer.setOpcode(OPCode);
		freeStoreBuffer.setIssueTime(getClockCycle());
	}

	private void issueBranch(String OPCode, String[] parsedInstruction)
	{
		isBranching = true;
		BranchStation freeBranchStation = branchStation.get(0);
		freeBranchStation.setBusy(true);
		freeBranchStation.setOpcode(OPCode);
		freeBranchStation.setExecutionTime(BranchReservationStationExecutionTime);
		IntegerRegister R2 = RegisterFile.readIntegerRegister(parsedInstruction[1]);
		IntegerRegister R3 = RegisterFile.readIntegerRegister(parsedInstruction[2]);
		String loopAddress = parsedInstruction[3];
		freeBranchStation.setQj(R2.getQi()); // if its 0, woo, if not, it saves it :)
		freeBranchStation.setQk(R3.getQi()); // if its 0, woo, if not, it saves it :)
		freeBranchStation.setAddress(Integer.parseInt(loopAddress));
		if (R2.getQi() == 0) freeBranchStation.setVj(R2.getValue());
		if (R3.getQi() == 0) freeBranchStation.setVk(R3.getValue());
	}

	/*
	 * Method mainly does two things:
	 * 1) Decrement the execution time by 1 if it has all its operands
	 *    (i.e., Qj and Qi are 0).
	 * 2) If the execution time is 0, compute and publish the result.
	 * 
	 * Priority:
	 * Whenever two instructions publish at the same time, we need to prioritize one over the other.
	 * The priority of an instruction is determined by the issue time.
	 * The earlier the instruction came to the station, the higher its priority.
	 * If two instructions have the same priority, then we determine their
	 * priority by the opcode:
	 * - Load has the highest priority,
	 * - Then floating multiplication and division,
	 * - Then floating addition and subtraction,
	 * - Then immediate (integer) addition and subtraction.
	 * If both instructions have the same opcode (3ashan 5awal), then we
	 * choose the one with the lower tag number (so M1 has a higher priority
	 * than M2, for example).
	 */
	private void executeAndWrite() throws Exception
	{
		int lowestIssueTime = Integer.MAX_VALUE;
		int theStrongestOneAfterGojoOfCourse = -1;// The tag of the one that will publish (based on
													// priority above)
		for (LoadBuffer loadBuffer : loadBuffers)
		{
			if (loadBuffer.isBusy() && loadBuffer.getQAddress() == 0)
			{
				if (loadBuffer.getExecutionTime() > 0)
				{
					if (loadBuffer.firstExecution)
					{
						loadBuffer.firstExecution = false;
						if (!cache.checkCache(loadBuffer.getAddress()))
						{
							logUpdate("Cache miss! Fetching cache...");
							loadBuffer.setExecutionTime(loadBuffer.getExecutionTime() + 10);
							loadBuffer.fillingCache = 10;
							cache.writeCache(loadBuffer.getAddress());
						}
						else
						{
							logUpdate("Cache hit!");
							loadBuffer.setExecutionTime(loadBuffer.getExecutionTime() - 1);
						}
					}
					else
					{
						loadBuffer.setExecutionTime(loadBuffer.getExecutionTime() - 1);
						loadBuffer.fillingCache--;
						if (loadBuffer.fillingCache == 0) cache.filledCache(loadBuffer.getAddress());

					}

				}
				else if (loadBuffer.getExecutionTime() == 0 && lowestIssueTime > loadBuffer.getIssueTime())
				{
					lowestIssueTime = loadBuffer.getIssueTime();
					theStrongestOneAfterGojoOfCourse = loadBuffer.getTag();
				}

			}
		}

		for (ReservationStation multiplicationStation : multiplyReservationStations)
		{
			if (multiplicationStation.isBusy())
			{
				if (multiplicationStation.getExecutionTime() > 0 && multiplicationStation.getQj() == 0
						&& multiplicationStation.getQk() == 0)
					multiplicationStation.setExecutionTime(multiplicationStation.getExecutionTime() - 1);
				else if (multiplicationStation.getExecutionTime() == 0
						&& lowestIssueTime > multiplicationStation.getIssueTime())
				{
					lowestIssueTime = multiplicationStation.getIssueTime();
					theStrongestOneAfterGojoOfCourse = multiplicationStation.getTag();
				}
			}
		}

		for (ReservationStation additionStation : addReservationStations)
		{
			if (additionStation.isBusy())
			{
				if (additionStation.getExecutionTime() > 0 && additionStation.getQj() == 0
						&& additionStation.getQk() == 0)
					additionStation.setExecutionTime(additionStation.getExecutionTime() - 1);
				else if (additionStation.getExecutionTime() == 0 && lowestIssueTime > additionStation.getIssueTime())
				{
					lowestIssueTime = additionStation.getIssueTime();
					theStrongestOneAfterGojoOfCourse = additionStation.getTag();

				}
			}
		}

		for (IntegerReservationStation immediateReservationStation : integerReservationStations)
		{
			if (immediateReservationStation.isBusy())
			{
				if (immediateReservationStation.getExecutionTime() > 0 && immediateReservationStation.getQj() == 0)
					immediateReservationStation.setExecutionTime(immediateReservationStation.getExecutionTime() - 1);
				else if (immediateReservationStation.getExecutionTime() == 0
						&& lowestIssueTime > immediateReservationStation.getIssueTime())
				{
					lowestIssueTime = immediateReservationStation.getIssueTime();
					theStrongestOneAfterGojoOfCourse = immediateReservationStation.getTag();
				}
			}

		}
		executeStore();
		executeBranch();

		if (theStrongestOneAfterGojoOfCourse != -1) publish(theStrongestOneAfterGojoOfCourse);
	}

	private void executeStore()
	{
		for (StoreBuffer storeBuffer : storeBuffers)
		{
			if (storeBuffer.isBusy() && storeBuffer.getQAddress() == 0 && storeBuffer.getQ() == 0)
			{

				if (storeBuffer.firstExecution)
				{
					storeBuffer.firstExecution = false;
					if (!cache.checkCache(storeBuffer.getAddress()))
					{
						logUpdate("Cache miss! Fetching cache...");
						storeBuffer.setExecutionTime(storeBuffer.getExecutionTime() + 10);
						storeBuffer.fillingCache = 10;
						cache.writeCache(storeBuffer.getAddress());
					}
					else
					{
						logUpdate("Cache hit!");
						storeBuffer.setExecutionTime(storeBuffer.getExecutionTime() - 1);
					}
				}
				else if(storeBuffer.getExecutionTime() > 0)
				{
					storeBuffer.setExecutionTime(storeBuffer.getExecutionTime() - 1);
					storeBuffer.fillingCache--;
					if (storeBuffer.fillingCache == 0) cache.filledCache(storeBuffer.getAddress());
				}
				// since store never writes to the bus, it can finish execution once is result is ready, no  matter
				// who is publishing on the bus
				else if (storeBuffer.getExecutionTime() == 0)
				{
					switch (storeBuffer.getOpcode())
					{
					case "SW":
						Memory.storeWord(storeBuffer.getAddress(), (int) storeBuffer.getV());
						break;
					case "SD":
						Memory.storeDoubleWord(storeBuffer.getAddress(), storeBuffer.vLong);
						break;
					case "S.S":
						Memory.storeSingle(storeBuffer.getAddress(), (float) storeBuffer.getV());
						break;
					case "S.D":
						Memory.storeDouble(storeBuffer.getAddress(), storeBuffer.getV());
						break;
					}
					logUpdate("Store buffer " + getTagString(storeBuffer.getTag()) + " is publishing!");
					fullStoreBuffers = false;
					storeBuffer.firstExecution = true;
					storeBuffer.setBusy(false);
					storeBuffer.setV(0);
					storeBuffer.setQ(0);
					storeBuffer.setAddress(0);
					storeBuffer.vLong = 0;
				}

			}
		}
	}

	private void executeBranch()
	{
		BranchStation branchingStation = branchStation.get(0);
		if (branchingStation.isBusy())
		{
			if (branchingStation.getExecutionTime() > 0 && branchingStation.getQj() == 0
					&& branchingStation.getQk() == 0)
				branchingStation.setExecutionTime(branchingStation.getExecutionTime() - 1);
			else if (branchingStation.getExecutionTime() == 0)
			{
				isBranching = false;
				branchingStation.setBusy(false);
				if (branchingStation.getOpcode().equals("BNE"))
				{
					if (branchingStation.getVj() != branchingStation.getVk())
					{
						int instructionNumber = branchingStation.getAddress() / 4;
						currentInstructionIndex = instructionNumber;

						logUpdate(
								"Branch successful, branching to instruction address " + branchingStation.getAddress());
					}
					else
					{
						logUpdate("Not branching...");
						currentInstructionIndex++;
					}
				}
				else if (branchingStation.getOpcode().equals("BEQ"))
				{
					if (branchingStation.getVj() == branchingStation.getVk())
					{
						int instructionNumber = branchingStation.getAddress() / 4;
						currentInstructionIndex = instructionNumber;

						logUpdate(
								"Branch successful, branching to instruction address " + branchingStation.getAddress());

					}
					else
					{
						logUpdate("Not branching...");
						currentInstructionIndex++;
					}

				}
				branchingStation.setAddress(0);
				branchingStation.setOpcode("");
				branchingStation.setQj(0);
				branchingStation.setQk(0);
				branchingStation.setVj(0);
				branchingStation.setVk(0);
			}
		}
	}

	private void publish(int tag) throws Exception
	{
		// if it is in the add reservation stations
		if (tag <= addReservationStationsSize)
		{
			ReservationStation publishingStation = getReservationStationWithTag(tag);
			if (publishingStation == null)
				throw new Exception("For some reason, one of the add reservation stations is not intialized");

			fullAddStations = false; // whether it was full or not, a space has opened up :D
			logUpdate("Reservation station " + getTagString(publishingStation.getTag()) + " is publishing!");
			// ALU will figure out whether its single/double, and if its subtraction or addition
			double result = ALU.addFloatOperation(publishingStation.getOpcode(), publishingStation.getVj(),
					publishingStation.getVk());
			publishFloatResult(tag, result);

			publishingStation.setBusy(false);
			publishingStation.setOpcode("");
			publishingStation.setVk(0);
			publishingStation.setVj(0);
			publishingStation.setQj(0);
			publishingStation.setQk(0);

		}
		// if it is in the multiplication reservation stations, and so on...
		else if (tag <= addReservationStationsSize + multiplyReservationStationsSize)
		{
			ReservationStation publishingStation = getReservationStationWithTag(tag);
			if (publishingStation == null) throw new Exception(
					"For some reason, one of the multiplication reservation stations is not intialized");
			logUpdate("Reservation station " + getTagString(publishingStation.getTag()) + " is publishing!");
			// ALU will figure out whether its single/double, and if its multiplication or division 
			double result = ALU.multiplyFloatOperation(publishingStation.getOpcode(), publishingStation.getVj(),
					publishingStation.getVk());
			fullMultiplyStations = false; // whether it was full or not, a space has opened up :D
			publishFloatResult(tag, result);
			publishingStation.setBusy(false);
			publishingStation.setOpcode("");
			publishingStation.setVk(0);
			publishingStation.setVj(0);
			publishingStation.setQj(0);
			publishingStation.setQk(0);

		}
		else if (tag <= addReservationStationsSize + multiplyReservationStationsSize + loadBuffersSize)
		{
			LoadBuffer publishingBuffer = getLoadBufferWithTag(tag);
			if (publishingBuffer == null)
				throw new Exception("For some reason, one of the load buffers is not intialized");
			fullLoadBuffers = false;
			logUpdate("Load buffer " + getTagString(publishingBuffer.getTag()) + " is publishing!");
			switch (publishingBuffer.getOpcode())
			{
			case "LW":
				int word = Memory.loadWord(publishingBuffer.getAddress());
				publishIntegerResult(publishingBuffer.getTag(), word);

				break;
			case "LD":
				long doubleWord = Memory.loadDoubleWord(publishingBuffer.getAddress());
				publishIntegerResult(publishingBuffer.getTag(), doubleWord);
				break;
			case "L.S":
				float wordValue = Memory.loadSingle(publishingBuffer.getAddress());
				// this weird hack is because precision gets fucked during conversion from float
				// to double; technically gets more precise, but still is not something we wanted
				double convertedWordValue = Double.valueOf(Float.valueOf(wordValue).toString()).doubleValue();
				publishFloatResult(tag, convertedWordValue);
				break;
			case "L.D":
				double doubleWordValue = Memory.loadDouble(publishingBuffer.getAddress());
				publishFloatResult(tag, doubleWordValue);
				break;
			}
			publishingBuffer.firstExecution = true;
			publishingBuffer.setBusy(false);
			publishingBuffer.setAddress(0);
		}
		else if (tag <= addReservationStationsSize + multiplyReservationStationsSize + loadBuffersSize
				+ storeBuffersSize + immediateReservationStationSize)
		{

			IntegerReservationStation publishingStation = getIntegerReservationStationWithTag(tag);
			if (publishingStation == null) throw new Exception(
					"For some reason, one of the immediate addition reservation stations is not intialized");
			fullImmediateStations = false;
			logUpdate("Reservation station " + getTagString(publishingStation.getTag()) + " is publishing!");
			long result = ALU.addIntegerOperation(publishingStation.getOpcode(), publishingStation.getVj(),
					(short) publishingStation.getVk());
			publishIntegerResult(tag, result);
			publishingStation.setBusy(false);
			publishingStation.setOpcode("");
			publishingStation.setVk(0);
			publishingStation.setVj(0);
			publishingStation.setQj(0);
			publishingStation.setQk(0);

		}
	}

	// publish by asking every single reservation station and the registers if it
	// needs this result 
	// mafrood el 3aks, el reservation station heya el tshoof law 3ayza 7aga mein el bus, bas
	// ana mesh 3ayz a3mel listeners, w el enen nafs el result
	public void publishFloatResult(int tag, double result)
	{

		for (ReservationStation multiplicationStation : multiplyReservationStations)
		{
			if (multiplicationStation.isBusy())
			{
				if (multiplicationStation.getQj() == tag)
				{
					multiplicationStation.setVj(result);
					multiplicationStation.setQj(0);
				}

				if (multiplicationStation.getQk() == tag)
				{
					multiplicationStation.setVk(result);
					multiplicationStation.setQk(0);
				}
			}
		}

		for (ReservationStation additionStation : addReservationStations)
		{
			if (additionStation.isBusy())
			{
				if (additionStation.getQj() == tag)
				{
					additionStation.setVj(result);
					additionStation.setQj(0);
				}

				if (additionStation.getQk() == tag)
				{
					additionStation.setVk(result);
					additionStation.setQk(0);
				}
			}
		}

		for (StoreBuffer storeBuffer : storeBuffers)
		{
			if (storeBuffer.isBusy() && storeBuffer.getQ() == tag)
			{
				storeBuffer.setQ(0);
				storeBuffer.setV(result);
				//				storeBuffer.setBusy(false);
			}
		}
		// should never be called since it only uses registers, but eh
		for (IntegerReservationStation immediateReservationStation : integerReservationStations)
		{
			if (immediateReservationStation.isBusy())
			{
				if (immediateReservationStation.getQj() == tag)
				{
					immediateReservationStation.setVj((long) result);
					immediateReservationStation.setQj(0);
				}
			}
		}

		for (FloatingRegister register : RegisterFile.floatingRegisters)
		{
			if (register.getQi() == tag)
			{
				register.setQi(0);
				register.setValue(result);
			}
		}
		for (IntegerRegister register : RegisterFile.integerRegisters)
		{
			if (register.getQi() == tag)
			{
				register.setQi(0);
				register.setValue((long) result);
			}
		}

	}

	public void publishIntegerResult(int tag, long result)
	{

		if (branchStation.get(0).isBusy())
		{
			if (branchStation.get(0).getQj() == tag)
			{
				branchStation.get(0).setVj(result);
				branchStation.get(0).setQj(0);
			}
			if (branchStation.get(0).getQk() == tag)
			{
				branchStation.get(0).setVk(result);
				branchStation.get(0).setQk(0);
			}
		}
		for (ReservationStation multiplicationStation : multiplyReservationStations)
		{
			if (multiplicationStation.isBusy())
			{
				if (multiplicationStation.getQj() == tag)
				{
					multiplicationStation.setVj(result);
					multiplicationStation.setQj(0);
				}

				if (multiplicationStation.getQk() == tag)
				{
					multiplicationStation.setVk(result);
					multiplicationStation.setQk(0);
				}
			}
		}

		for (ReservationStation additionStation : addReservationStations)
		{
			if (additionStation.isBusy())
			{
				if (additionStation.getQj() == tag)
				{
					additionStation.setVj(result);
					additionStation.setQj(0);
				}

				if (additionStation.getQk() == tag)
				{
					additionStation.setVk(result);
					additionStation.setQk(0);
				}
			}
		}

		for (StoreBuffer storeBuffer : storeBuffers)
		{
			if (storeBuffer.isBusy() && storeBuffer.getQ() == tag)
			{
				storeBuffer.setQ(0);
				storeBuffer.setV(result);
				storeBuffer.vLong = result;
			}
			if (storeBuffer.isBusy() && storeBuffer.getQAddress() == tag)
			{
				storeBuffer.setQAddress(0);
				storeBuffer.setAddress((int) result);
			}
		}
		for (LoadBuffer loadBuffer : loadBuffers)
		{
			if (loadBuffer.isBusy() && loadBuffer.getQAddress() == tag)
			{
				loadBuffer.setQAddress(0);
				loadBuffer.setAddress((int) result);
			}
		}

		for (IntegerRegister register : RegisterFile.integerRegisters)
		{
			if (register.getQi() == tag)
			{
				register.setQi(0);
				register.setValue(result);
			}
		}

		for (IntegerReservationStation immediateReservationStation : integerReservationStations)
		{
			if (immediateReservationStation.isBusy())
			{
				if (immediateReservationStation.getQj() == tag)
				{
					immediateReservationStation.setVj(result);
					immediateReservationStation.setQj(0);
				}
			}
		}

	}

	private ReservationStation getReservationStationWithTag(int tag)
	{
		for (ReservationStation additionStation : addReservationStations)
			if (additionStation.getTag() == tag) return additionStation;

		for (ReservationStation multiplicationStation : multiplyReservationStations)
			if (multiplicationStation.getTag() == tag) return multiplicationStation;

		return null;
	}

	private IntegerReservationStation getIntegerReservationStationWithTag(int tag)
	{
		for (IntegerReservationStation immediateReservationStation : integerReservationStations)
			if (immediateReservationStation.getTag() == tag) return immediateReservationStation;

		return null;
	}

	private LoadBuffer getLoadBufferWithTag(int tag)
	{
		for (LoadBuffer loadBuffer : loadBuffers)
			if (loadBuffer.getTag() == tag) return loadBuffer;

		return null;
	}

}
