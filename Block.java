package Homework2;

import java.util.ArrayList;

public class Block // 승인 받은 Transaction의 모음을 Block이라 칭함
{
	// Block Head
	private String blockID; // 채굴자가 계산한 앞자리가 0이 3개인 Hash 값 - Hashcash
	private String blockMaker; // Block 생성자의 이름
	private long blockHeight; // Block 높이
	private String timestamp; // Block의 TimeStamp (한국시간 기준, ms까지)
	private String nonce; // Hash 난수
	private String preBlockID; // 이전 Block의 ID
	private String merkleRoot; // MerkleTree Root
	
	// Block Body
	private MerkleTree merkleTree;
	private ArrayList<Transaction> confirmingtransaction;
	
	// Block Constructor & Block Heigth Check
	
	public Block() {}
	public Block(String blockID,
			String blockMaker, 
			long blockHeight, 
			String timestamp,
			String Nonce,
			String preBlockID, 
			String merkleRoot,
			MerkleTree merkleTree,
			ArrayList<Transaction> transactions)
	{
		this.confirmingtransaction = new ArrayList<Transaction>();
		this.blockID = blockID;
		this.blockMaker = blockMaker;
		this.blockHeight = blockHeight;
		this.timestamp = timestamp;
		this.nonce = Nonce;
		
		if(blockHeight == 0)
		{
			this.preBlockID = "";
		}
		else
		{
			this.preBlockID = preBlockID;
		}
		
		this.merkleTree = merkleTree;
		this.merkleRoot = merkleRoot;
		this.confirmingtransaction = transactions;
	}
	
	// Attribute Getter & Setter
	
	public String getBlockID()
	{
		return blockID;
	}
	public void setBlockID(String blockID)
	{
		this.blockID = blockID;
	}
	public String getBlockMaker()
	{
		return blockMaker;
	}
	public void setBlockMaker(String blockMaker)
	{
		this.blockMaker = blockMaker;
	}
	public long getBlockHeight()
	{
		return blockHeight;
	}
	public void setBlockHeight(long blockHeight)
	{
		this.blockHeight = blockHeight;
	}
	public String getTimestamp()
	{
		return timestamp;
	}
	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}
	public String getNonce()
	{
		return nonce;
	}
	public void setNonce(String nonce)
	{
		this.nonce = nonce;
	}
	public String getPreBlockID()
	{
		return preBlockID;
	}
	public void setPreBlockID(String preBlockID)
	{
		this.preBlockID = preBlockID;
	}
	public String getMerkleRoot()
	{
		return merkleRoot;
	}
	public void setMerkleRoot(String merkleRoot)
	{
		this.merkleRoot = merkleRoot;
	}
	public MerkleTree getMerkleTree()
	{
		return merkleTree;
	}
	public void setMerkleTree(MerkleTree merkleTree)
	{
		this.merkleTree = merkleTree;
	}
	public ArrayList<Transaction> getConfirmingtransaction()
	{
		return confirmingtransaction;
	}
	public void setConfirmingtransaction(ArrayList<Transaction> confirmingtransaction)
	{
		this.confirmingtransaction = confirmingtransaction;
	}
	
	@Override
	public String toString()
	{
		return "========================= Block Head =========================" +
			   "\nBlock ID " + blockID +
			   "\nBlock Maker " + blockMaker +
			   "\nBlock Height " + blockHeight +
			   "\nTime Stamp : " + timestamp +
			   "\nPreBlock ID : " + preBlockID +
			   "\nMerkle Root : "  +  merkleRoot +
			   "\nNonce : " + nonce +
			   "\n======================== Block Body =========================";
	}
}
