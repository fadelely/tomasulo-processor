package processor.tomasulo;

public class LoadBuffer {

	boolean busy;
	int address;
	public LoadBuffer()
	{
		busy = false;
		address = 0;
	}
}
