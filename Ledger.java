package Homework2;

import java.util.ArrayList;
import java.util.List;

public class Ledger // ������� �̾��� ��ϵ��� ����Ʈ ��ü�� ������
{
	private List<Block> blockchain; // Block���� ����Ʈ
	private List<Transaction> unconfirmedTransaction; // ���ε��� ���� Transaction ����Ʈ
	private Block newBlock; // ���ο� ���
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
			throw new IllegalArgumentException("����� ã�� �� �����ϴ�!");
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
