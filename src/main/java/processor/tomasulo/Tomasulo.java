package processor.tomasulo;

import java.io.IOException;
import java.util.ArrayList;

public class Tomasulo {
public class LoadBuffer {

	boolean busy;
	int tag;
	int address;
	public LoadBuffer(int tag)
	{
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
	public StoreBuffer(int tag)
	{
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
	public ReservationStation(int tag) 
	{
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

	private void init() {
		int tag = 1;
		for (int i= 0 ;i<  addReservationStations.length;i++)
			addReservationStations[i] = new ReservationStation(tag++);

		for (int i= 0 ;i<  multiplyReservationStations.length;i++)
			multiplyReservationStations[i] = new ReservationStation(tag++);

		for (int i= 0 ;i<  loadBuffers.length;i++)
			loadBuffers[i] = new LoadBuffer(tag++);

		for (int i= 0 ;i<  storeBuffers.length;i++)
			storeBuffers[i] = new StoreBuffer(tag++);
	}

	public void startExecution() throws IOException {
		init();
		ParseText parseText = new ParseText();
		instructions = parseText.parseTextFile();
	}
}
