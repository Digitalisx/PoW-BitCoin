package Homework2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

public class Miner extends User
{
	private Wallet miner;
	public Miner(Ledger ledger, ArrayList<Wallet> wallet, String minerName)
	{
		super(ledger, wallet);
		
		for(int i = 0; i < wallet.size(); i++)
		{
			if(wallet.get(i).getUserName().equals(minerName))
			{
				miner = wallet.get(i);
			}
		}
	}
	
	public void mine(int unconfirmeTransactionSizeLimit, int mode)
	{
		// Sorting Transaction - Using Comparator
		Collections.sort(publicLedger.getUnconfirmedTransaction());

		// Generate Reward Transaction
		float reward_coin = 12.5F;
		float reward_fee = 0.0F;
		Transaction reward_transaction = new Transaction(miner, reward_coin, reward_fee);

		// Main Transaction
		String[] txID_List = { publicLedger.getUnconfirmedTransaction().get(0).getTxID(),
				publicLedger.getUnconfirmedTransaction().get(1).getTxID(),
				publicLedger.getUnconfirmedTransaction().get(2).getTxID(), reward_transaction.getTxID() };

		// Generate Confirm Transaction List
		ArrayList<Transaction> confirm_txlist = new ArrayList<Transaction>();

		for (int i = 0; i < 3; i++) {
			confirm_txlist.add(publicLedger.getUnconfirmedTransaction().get(i));
		}
		confirm_txlist.add(reward_transaction);

		// MerkleTree Generator
		MerkleTree merkletree = new MerkleTree(txID_List, "");
		String merkle_root = merkletree.getRoot();

		// Miner Algorithm
		// PreBlock ID Generate
		String preblock_id;

		if (publicLedger.getBlockchain().size() == 0) {
			preblock_id = " ";
		} else {
			int block_size = publicLedger.getBlockchain().size();
			preblock_id = publicLedger.getBlockchain().get(block_size - 1).getBlockID();
		}

		// Nonce Generate
		while (true) {
			Random random = new Random();
			int nonce = random.nextInt(10000);
			String mine_string = preblock_id + merkle_root + nonce;
			String mine_sha;

			if (mode == 1) {
				mine_sha = Utils.getSHA256(mine_string);
			} else {
				mine_sha = Utils.getSHA256(Integer.toString(nonce));
			}

			if (mine_sha.substring(0, 3).equals("000")) {
				Block mine_block = new Block();

				// Block Head
				mine_block.setBlockID(mine_sha);
				mine_block.setBlockMaker("miner");
				mine_block.setBlockHeight(publicLedger.getBlockchain().size());

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Calendar calendar = Calendar.getInstance();

				mine_block.setTimestamp(dateFormat.format(calendar.getTime()));
				mine_block.setNonce(Integer.toString(nonce));
				mine_block.setPreBlockID(preblock_id);
				mine_block.setMerkleRoot(merkle_root);
				mine_block.setBlockHeight(publicLedger.getBlockchain().size());

				// Block Body
				// 트랜잭션 모아서 등록 confirm
				mine_block.setMerkleTree(merkletree);
				mine_block.setConfirmingtransaction(confirm_txlist);

				// Ledger에 신규 Block 등록하기
				publicLedger.setNewBlock(mine_block);
				publicLedger.setNewBlockCreated(true);

				break;
			}
		}
	}
	
	public void makeFakeBlock()
	{
		
	}
}
