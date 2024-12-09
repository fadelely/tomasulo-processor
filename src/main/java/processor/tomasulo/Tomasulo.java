package processor.tomasulo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.function.Consumer;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import processor.tomasulo.RegisterFile.FloatingRegister;
import processor.tomasulo.RegisterFile.IntegerRegister;

public class Tomasulo
{
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

	public static ArrayList<String> instructions = new ArrayList<String>(); // these are all the
																			// instructions, not yet
																			// executed :)
																			// size should be entered by user
	public static ObservableList<ReservationStation> addReservationStations = FXCollections.observableArrayList();

	public static ObservableList<ReservationStation> multiplyReservationStations = FXCollections.observableArrayList();

	public static ObservableList<LoadBuffer> loadBuffers = FXCollections.observableArrayList();
	public static ObservableList<StoreBuffer> storeBuffers = FXCollections.observableArrayList();

	private int currentInstructionIndex = 0; // Index of the instruction being executed
	private int remainingClockCycles = 0; // Clock cycles remaining for the current instruction
	private boolean stalled = false; // Stalled state
	private int clockCycle = 1; // Current clock cycle
	private String currentOPCode = ""; // OPCode of the instruction being executed

	ParseText parseText = new ParseText();

	public void setUpdateLogCallback(Consumer<String> callback)
	{
		this.updateLog = callback;
	}

	private void logUpdate(String message)
	{
		if (updateLog != null) updateLog.accept(message);

	}

	public class LoadBuffer
	{

		private final IntegerProperty issueTime; // tracks when did it enter the reservation station; used to
								// determine priority when two instructions are writing at the same time
		private final StringProperty opcode;

		private final BooleanProperty busy;
		private final IntegerProperty tag;
		private final IntegerProperty address;

		private final IntegerProperty executionTime;

		// Constructor
		public LoadBuffer(int tag)
		{
			this.issueTime = new SimpleIntegerProperty(-1);
			this.opcode = new SimpleStringProperty("");
			this.tag = new SimpleIntegerProperty(tag);
			this.busy = new SimpleBooleanProperty(false);
			this.address = new SimpleIntegerProperty(0);
			this.executionTime = new SimpleIntegerProperty(0);
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
		private final IntegerProperty address;
		private final StringProperty opcode;

		private final IntegerProperty executionTime;

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
			this.tag = 0;
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

	public void init()
	{
		int tag = 1;
		for (int i = 0; i < addReservationStationsSize; i++)
		{
			ReservationStation addReservationStation = new ReservationStation(tag++);
			addReservationStations.add(addReservationStation);
			//			System.out.println("Add: "+addReservationStation.tag+"   "+i);

		}

		for (int i = 0; i < multiplyReservationStationsSize; i++)
		{
			ReservationStation multiplyReservationStation = new ReservationStation(tag++);
			multiplyReservationStations.add(multiplyReservationStation);
			//			System.out.println("Multiply: "+multiplyReservationStation.tag+"   "+i);
		}

		for (int i = 0; i < loadBuffersSize; i++)
		{
			LoadBuffer loadBuffer = new LoadBuffer(tag++);
			loadBuffers.add(loadBuffer);
			//			System.out.println("Load: "+loadBuffer.tag+"   "+i);
		}
		for (int i = 0; i < storeBuffersSize; i++)
		{
			StoreBuffer storeBuffer = new StoreBuffer(tag++);
			storeBuffers.add(storeBuffer);
			//			System.out.println("Store :" +storeBuffer.tag+"   "+i);
		}
	}

	public void startExecution() throws IOException
	{
		instructions = parseText.parseTextFile();
		Iterator<String> instructionIterator = instructions.iterator();
		String instruction = "";
		while (true)
		{
			System.out.println("In clock cycle: " + clockCycle);
			instruction = "";
			if (!stalled && instructionIterator.hasNext()) instruction = instructionIterator.next();

			try
			{
				executeAndWrite();
				if (!instruction.equals("")) issue(instruction);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			clockCycle++;
		}
	}

	public void issue(String instruction) throws IOException
	{
		if (stalled) return;

		String regex = "[ ,]+";
		String[] parsedInstruction = instruction.split(regex);
		String OPCode = parsedInstruction[0];

		if (parseText.isFloatAdditionOperation(OPCode))
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
				stalled = true;
				return;
			}

			freeReservationStation.setBusy(true);
			freeReservationStation.setOpcode(OPCode);
			freeReservationStation.setExecutionTime(AddReservationStationExecutionTime);
			freeReservationStation.setIssueTime(clockCycle);
			String F1 = parsedInstruction[1]; // string as this is where we will save our result
			FloatingRegister F2 = RegisterFile.readFloatRegister(parsedInstruction[2]);
			FloatingRegister F3 = RegisterFile.readFloatRegister(parsedInstruction[3]);
			freeReservationStation.setQj(F2.Qi); // if its 0, woo, if not, it saves it :)
			freeReservationStation.setQk(F3.Qi); // if its 0, woo, if not, it saves it :)
			if (F2.Qi == 0) freeReservationStation.setVj(F2.value);
			if (F3.Qi == 0) freeReservationStation.setVk(F3.value);
			RegisterFile.writeTagToRegisterFile(F1, freeReservationStation.getTag());

		}
		else if (parseText.isMultiplyOperation(OPCode))
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
				stalled = true;
				return;
			}

			freeReservationStation.setBusy(true);
			freeReservationStation.setOpcode(OPCode);
			freeReservationStation.setExecutionTime(MultiplyReservationStationExecutionTime);
			freeReservationStation.setIssueTime(clockCycle);
			String F1 = parsedInstruction[1]; // string as this is where we will save our result
			FloatingRegister F2 = RegisterFile.readFloatRegister(parsedInstruction[2]);
			FloatingRegister F3 = RegisterFile.readFloatRegister(parsedInstruction[3]);
			freeReservationStation.setQj(F2.Qi); // if its 0, woo, if not, it saves it :)
			freeReservationStation.setQk(F3.Qi); // if its 0, woo, if not, it saves it :)
			if (F2.Qi == 0) freeReservationStation.setVj(F2.value);
			if (F3.Qi == 0) freeReservationStation.setVk(F3.value);
			RegisterFile.writeTagToRegisterFile(F1, freeReservationStation.getTag());

		}
		else if (parseText.isIntegerAdditionOperation(OPCode))
		{
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
				stalled = true;
				return;
			}

			freeReservationStation.setBusy(true);
			freeReservationStation.setOpcode(OPCode);
			freeReservationStation.setExecutionTime(AddReservationStationExecutionTime);
			freeReservationStation.setIssueTime(clockCycle);

			String R1 = parsedInstruction[1]; // string as this is where we will save our result
			IntegerRegister R2 = RegisterFile.readIntegerRegister(parsedInstruction[2]);
			short immediate = Short.valueOf(parsedInstruction[3]);

			freeReservationStation.setQj(R2.Qi); // if its 0, woo, if not, it saves it :)
			freeReservationStation.setQk(0); // since it only reads one register :)
			if (R2.Qi == 0) freeReservationStation.setVj(R2.value);
			freeReservationStation.setVk(immediate);
			RegisterFile.writeTagToRegisterFile(R1, freeReservationStation.getTag());

		}
		else if (parseText.isLoadOperation(OPCode))
		{
			/*
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
				stalled = true;
				return;
			}

			String R1 = parsedInstruction[1]; // can be integer or floating register
			int memoryAddress = Integer.parseInt(parsedInstruction[2]);

			freeLoadBuffer.setBusy(true);
			freeLoadBuffer.setAddress(memoryAddress);
			freeLoadBuffer.setExecutionTime(LoadBufferExecutionTime);
			freeLoadBuffer.setOpcode(OPCode);
			freeLoadBuffer.setIssueTime(clockCycle);
			RegisterFile.writeTagToRegisterFile(R1, freeLoadBuffer.getTag());

		}
		else if (parseText.isStoreOperation(OPCode))
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
				stalled = true;
				return;
			}

			int memoryAddress = Integer.parseInt(parsedInstruction[2]);

			if (parseText.isIntegerStoreOperation(OPCode))
			{
				IntegerRegister R1 = RegisterFile.readIntegerRegister(parsedInstruction[1]);
				freeStoreBuffer.setQ(R1.Qi); // if its 0, woo, if not, it saves it :)
				if (R1.Qi == 0) freeStoreBuffer.setV(R1.value);
			}
			// don't need the if since the else will always be true, but it is
			// left for readibility's sake
			else if (parseText.isFloatStoreOperation(OPCode))
			{
				FloatingRegister F1 = RegisterFile.readFloatRegister(parsedInstruction[1]);
				freeStoreBuffer.setQ(F1.Qi); // if its 0, woo, if not, it saves it :)
				if (F1.Qi == 0) freeStoreBuffer.setV(F1.value);
			}

			freeStoreBuffer.setBusy(true);
			freeStoreBuffer.setAddress(memoryAddress);
			freeStoreBuffer.setExecutionTime(StoreBufferExecutionTime);
			freeStoreBuffer.setOpcode(OPCode);
			freeStoreBuffer.setIssueTime(clockCycle);
		}

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
	 * - Then multiplication,
	 * - Then addition,
	 * - Then finally division.
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
			if (loadBuffer.isBusy())
			{
				if (loadBuffer.getExecutionTime() > 0)
					loadBuffer.setExecutionTime(loadBuffer.getExecutionTime() - 1);
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

		for (StoreBuffer storeBuffer : storeBuffers)
		{
			if (storeBuffer.isBusy())
			{
				if (storeBuffer.getExecutionTime() > 0 && storeBuffer.getQ() == 0)
					storeBuffer.setExecutionTime(storeBuffer.getExecutionTime() - 1);
				// since store never writes to the bus, it can finish execution once is result is ready, no  matter
				// who is publishing on the bus
				// can multiple people publish on bus?
				else if (storeBuffer.getExecutionTime() == 0)
				{
					switch (storeBuffer.getOpcode())
					{
					case "SW":
						Memory.storeWord(storeBuffer.getAddress(), (int) storeBuffer.getV());
						break;
					case "SD":
						Memory.storeDoubleWord(storeBuffer.getAddress(), (long) storeBuffer.getV());
						break;
					case "S.S":
						Memory.storeSingle(storeBuffer.getAddress(), (float) storeBuffer.getV());
						break;
					case "S.D":
						Memory.storeDouble(storeBuffer.getAddress(), storeBuffer.getV());
						break;
					}
				}

			}
		}

		if (theStrongestOneAfterGojoOfCourse != -1) publish(theStrongestOneAfterGojoOfCourse);
	}

	private void publish(int tag) throws Exception
	{
		// if it is in the add reservation stations
		if (tag <= addReservationStationsSize)
		{
			ReservationStation publishingStation = getReservationStationWithTag(tag);
			if (publishingStation == null)
				throw new Exception("For some reason, one of the add reservation stations is not intialized");
			publishingStation.setBusy(false);
			if (parseText.isFloatAdditionOperation(publishingStation.getOpcode()))
			{
				logUpdate("Reservation station " + publishingStation.tag + " is publishing!");
				// ALU will figure out whether its single/double, and if its subtraction or addition
				double result = ALU.addFloatOperation(publishingStation.getOpcode(), publishingStation.getVj(),
						publishingStation.getVk());
				publishFloatResult(tag, result);
			}

			else if (parseText.isIntegerAdditionOperation(publishingStation.getOpcode()))
			{
				logUpdate("Reservation station " + publishingStation.getTag() + " is publishing!");
				long result = ALU.addIntegerOperation(publishingStation.getOpcode(), publishingStation.getQj(),
						(short) publishingStation.getQk());
				publishIntegerResult(tag, result);
			}

		}
		// if it is in the multiplication reservation stations, and so on...
		else if (tag <= addReservationStationsSize + multiplyReservationStationsSize)
		{
			ReservationStation publishingStation = getReservationStationWithTag(tag);
			if (publishingStation == null) throw new Exception(
					"For some reason, one of the multiplication reservation stations is not intialized");
			publishingStation.setBusy(false);
			logUpdate("Reservation station " + publishingStation.getTag() + " is publishing!");
			// ALU will figure out whether its single/double, and if its multiplication or division 
			double result = ALU.addFloatOperation(publishingStation.getOpcode(), publishingStation.getVj(),
					publishingStation.getVk());
			publishFloatResult(tag, result);

		}
		else if (tag <= addReservationStationsSize + multiplyReservationStationsSize + loadBuffersSize)
		{
			LoadBuffer publishingBuffer = getLoadBufferWithTag(tag);
			if (publishingBuffer == null)
				throw new Exception("For some reason, one of the load buffers is not intialized");
			publishingBuffer.setBusy(false);
			logUpdate("Load buffer " + publishingBuffer.getTag() + " is publishing!");
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
				// to double; techincally gets more precise, but still is not something we wanted
				double convertedWordValue = Double.valueOf(Float.valueOf(wordValue).toString()).doubleValue();
				publishFloatResult(tag, convertedWordValue);
				break;
			case "L.D":
				double doubleWordValue = Memory.loadDouble(publishingBuffer.getAddress());
				publishFloatResult(tag, doubleWordValue);
				break;
			}

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
			}
		}

		for (FloatingRegister register : RegisterFile.floatingRegisters)
		{
			if (register.Qi == tag)
			{
				register.Qi = 0;
				register.value = result;
			}
		}

	}

	public void publishIntegerResult(int tag, long result)
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
			}
		}

		for (IntegerRegister register : RegisterFile.integerRegisters)
		{
			if (register.Qi == tag)
			{
				register.Qi = 0;
				register.value = result;
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

	private LoadBuffer getLoadBufferWithTag(int tag)
	{
		for (LoadBuffer loadBuffer : loadBuffers)
			if (loadBuffer.getTag() == tag) return loadBuffer;

		return null;
	}

}
