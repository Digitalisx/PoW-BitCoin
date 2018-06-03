package Homework2;

import java.util.ArrayList;
import java.util.List;

public class Ledger // 현재까지 이어진 블록들을 리스트 객체로 저장함
{
	private List<Block> blockchain; // Block형의 리스트
	private List<Transaction> unconfirmedTransaction; // 승인되지 않은 Transaction 리스트
	private Block newBlock; // 새로운 블록
	private boolean isNewBlockCreated; // 
	
	public Ledger()
	{
		blockchain = new ArrayList<Block>();
		unconfirmedTransaction = new ArrayList<Transaction>();
	}
	
	// Find Block Method
	public Block findBlock(long blockHeight)
	{
		try
		{
			Block find_block = blockchain.get((int)blockHeight);
			System.out.println("Block Found!");
			return find_block;
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("블록을 찾을 수 없습니다!");
		}
	}
	
	
	// Attribute Getter / Setter
	
	public List<Block> getBlockchain()
	{
		return blockchain;
	}
	public void setBlockchain(List<Block> blockchain)
	{
		this.blockchain = blockchain;
	}
	public List<Transaction> getUnconfirmedTransaction()
	{
		return unconfirmedTransaction;
	}
	public void setUnconfirmedTransaction(List<Transaction> unconfirmedTransaction)
	{
		this.unconfirmedTransaction = unconfirmedTransaction;
	}	
	public Block getNewBlock()
	{
		return newBlock;
	}
	public void setNewBlock(Block newBlock)
	{
		this.newBlock = newBlock;
	}
	public boolean isNewBlockCreated()
	{
		return isNewBlockCreated;
	}
	public void setNewBlockCreated(boolean isNewBlockCreated) {
		this.isNewBlockCreated = isNewBlockCreated;
	}	
}
