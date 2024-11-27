package processor.tomasulo;

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
