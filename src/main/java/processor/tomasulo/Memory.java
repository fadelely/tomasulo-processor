package processor.tomasulo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Memory {
	static byte[] addresses = new byte[128];

	int loadWord(int address, String register) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		byte firstByte = Memory.addresses[address];
		byte secondByte = Memory.addresses[address + 1];
		byte thirdByte = Memory.addresses[address + 2];
		byte fourthByte = Memory.addresses[address + 3];
		byteBuffer.put(firstByte);
		byteBuffer.put(secondByte);
		byteBuffer.put(thirdByte);
		byteBuffer.put(fourthByte);
		int word = byteBuffer.getInt(0);
		return word;
	}

	long loadDoubleWord(int address, String register) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		byte firstByte = Memory.addresses[address];
		byte secondByte = Memory.addresses[address + 1];
		byte thirdByte = Memory.addresses[address + 2];
		byte fourthByte = Memory.addresses[address + 3];
		byte fifthByte = Memory.addresses[address + 4];
		byte sixthByte = Memory.addresses[address + 5];
		byte seventhByte = Memory.addresses[address + 6];
		byte eighthByte = Memory.addresses[address + 7];
		byteBuffer.put(firstByte);
		byteBuffer.put(secondByte);
		byteBuffer.put(thirdByte);
		byteBuffer.put(fourthByte);
		byteBuffer.put(fifthByte);
		byteBuffer.put(sixthByte);
		byteBuffer.put(seventhByte);
		byteBuffer.put(eighthByte);
		long doubleWord = byteBuffer.getLong(0);
		return doubleWord;
	}

	void storeWord(int address, int registerValue) {
		// xFF means the LSB are all 1s, and anything after that is 0
		Memory.addresses[address] = (byte) (registerValue & 0xFF);
		Memory.addresses[address + 1] = (byte) ((registerValue >> 8) & 0xFF);
		Memory.addresses[address + 2] = (byte) ((registerValue >> 16) & 0xFF);
		Memory.addresses[address + 3] = (byte) ((registerValue >> 24) & 0xFF);
	}

	void storeDoubleWord(int address, long registerValue) {
		// xFF means the LSB are all 1s, and anything after that is 0
		Memory.addresses[address] = (byte) (registerValue & 0xFF);
		Memory.addresses[address + 1] = (byte) ((registerValue >> 8) & 0xFF);
		Memory.addresses[address + 2] = (byte) ((registerValue >> 16) & 0xFF);
		Memory.addresses[address + 3] = (byte) ((registerValue >> 24) & 0xFF);
		Memory.addresses[address + 4] = (byte) ((registerValue >> 32) & 0xFF);
		Memory.addresses[address + 5] = (byte) ((registerValue >> 40) & 0xFF);
		Memory.addresses[address + 6] = (byte) ((registerValue >> 48) & 0xFF);
		Memory.addresses[address + 7] = (byte) ((registerValue >> 56) & 0xFF);

	}
}
