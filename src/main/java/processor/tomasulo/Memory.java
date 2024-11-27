package processor.tomasulo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Memory {
	static byte[] addresses = new byte[128];

	public static int loadWord(int address) {
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
		System.out.println("Got the integer value " + word + " from memory.");
		return word;
	}

	public static long loadDoubleWord(int address) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);
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
		System.out.println("Got the long value " + doubleWord + " from memory.");
		return doubleWord;
	}

	public static float loadSingle(int address) {
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
		float word = byteBuffer.getFloat(0);
		System.out.println("Got the float value " + word + " from memory.");
		return word;
	}

	public static double loadDouble(int address) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(8);
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
		double doubleWord = byteBuffer.getDouble(0);
		System.out.println("Got the double value " + doubleWord + " from memory.");
		return doubleWord;
	}

	public static void storeWord(int address, int registerValue) {
		// xFF means the LSB are all 1s, and anything after that is 0
		Memory.addresses[address] = (byte) (registerValue & 0xFF);
		Memory.addresses[address + 1] = (byte) ((registerValue >> 8) & 0xFF);
		Memory.addresses[address + 2] = (byte) ((registerValue >> 16) & 0xFF);
		Memory.addresses[address + 3] = (byte) ((registerValue >> 24) & 0xFF);
		System.out.println("Stored " + registerValue + " inside of address " + address);
	}

	public static void storeSingle(int address, float registerValue) {
		// xFF means the LSB are all 1s, and anything after that is 0
		int floatBits = Float.floatToIntBits(registerValue);
		Memory.addresses[address] = (byte) (floatBits & 0xFF);
		Memory.addresses[address + 1] = (byte) ((floatBits >> 8) & 0xFF);
		Memory.addresses[address + 2] = (byte) ((floatBits >> 16) & 0xFF);
		Memory.addresses[address + 3] = (byte) ((floatBits >> 24) & 0xFF);
		System.out.println("Stored " + registerValue + " inside of address " + address);
	}

	public static void storeDoubleWord(int address, long registerValue) {
		// xFF means the LSB are all 1s, and anything after that is 0
		Memory.addresses[address] = (byte) (registerValue & 0xFF);
		Memory.addresses[address + 1] = (byte) ((registerValue >> 8) & 0xFF);
		Memory.addresses[address + 2] = (byte) ((registerValue >> 16) & 0xFF);
		Memory.addresses[address + 3] = (byte) ((registerValue >> 24) & 0xFF);
		Memory.addresses[address + 4] = (byte) ((registerValue >> 32) & 0xFF);
		Memory.addresses[address + 5] = (byte) ((registerValue >> 40) & 0xFF);
		Memory.addresses[address + 6] = (byte) ((registerValue >> 48) & 0xFF);
		Memory.addresses[address + 7] = (byte) ((registerValue >> 56) & 0xFF);
		System.out.println("Stored " + registerValue + " inside of address " + address);
	}

	public static void storeDouble(int address, double registerValue) {
		// xFF means the LSB are all 1s, and anything after that is 0
		long doubleBits = Double.doubleToLongBits(registerValue);
		Memory.addresses[address] = (byte) (doubleBits & 0xFF);
		Memory.addresses[address + 1] = (byte) ((doubleBits >> 8) & 0xFF);
		Memory.addresses[address + 2] = (byte) ((doubleBits >> 16) & 0xFF);
		Memory.addresses[address + 3] = (byte) ((doubleBits >> 24) & 0xFF);
		Memory.addresses[address + 4] = (byte) ((doubleBits >> 32) & 0xFF);
		Memory.addresses[address + 5] = (byte) ((doubleBits >> 40) & 0xFF);
		Memory.addresses[address + 6] = (byte) ((doubleBits >> 48) & 0xFF);
		Memory.addresses[address + 7] = (byte) ((doubleBits >> 56) & 0xFF);
		System.out.println("Stored " + registerValue + " inside of address " + address);

	}
}
