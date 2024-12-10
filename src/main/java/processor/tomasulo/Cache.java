package processor.tomasulo;

import java.util.Arrays;

public class Cache
{
	
	public static int[] blocks;
	public int blockSize;
	public Cache(int cacheSize, int blockSize)
	{
		blocks = new int[cacheSize/blockSize];
		Arrays.fill(blocks, -1);  
		this.blockSize = blockSize;
	}
	
	// We do not have an acutual cache, we simulate it by storing only the index and tag bits
	// and use it to check if its in the cache or not; if it is, continue normally, if not, write into
	// the cache by changing the tags bit for that index and increase the execution time for that instruction
	public boolean checkCache(int memoryAddress)
	{
        int blockOffsetBits = (int)(Math.log(blockSize) / Math.log(2));
        int indexBits = (int)(Math.log(blocks.length) / Math.log(2));
        int indexMask = (1 << indexBits) - 1; 
		int index = (memoryAddress >> (blockOffsetBits) ) & indexMask;
		int tag = memoryAddress >> (blockOffsetBits + indexBits);
		if(blocks[index] == tag)
		{
			return true;
		}
		return false;
		
	}
	
	public void writeCache(int memoryAddress)
	{
        int blockOffsetBits = (int)(Math.log(blockSize) / Math.log(2));
        int indexBits = (int)(Math.log(blocks.length) / Math.log(2));
        int indexMask = (1 << indexBits) - 1; 
		int index = (memoryAddress >> (blockOffsetBits) ) & indexMask;
		int tag = (int) Math.floor(memoryAddress / blocks.length);
		blocks[index] = tag;

	}
}
