package Homework2;

import java.util.ArrayList;
import java.util.List;

public class User
{
	protected Ledger publicLedger = new Ledger();
	protected List<Wallet> wallets = new ArrayList<Wallet>();
	public User(Ledger ledger, ArrayList<Wallet> wallet)
	{
		// connectedUser = new ArrayList<User>();
		this.publicLedger = ledger;
		this.wallets = wallet;
	}
	
	// Transaction을 생성한다.
	public void sendTransaction(Wallet sender, Wallet receiver, float coin, float fee)
	{
		Transaction new_transaction = new Transaction(sender, receiver, coin, fee);
		publicLedger.getUnconfirmedTransaction().add(new_transaction);
	}
	
	// 새로운 Block에 대해 검증한다.
	public void validateNewBlock()
	{
		if(publicLedger.isNewBlockCreated() == true)
		{
			// 신규 블록의 속성 추출해서 재조합 해보기
			Block new_block = publicLedger.getNewBlock();
			String preblock_id = new_block.getPreBlockID();
			String merkle_root = new_block.getMerkleRoot();
			String nonce = new_block.getNonce();
			String mine_string = preblock_id + merkle_root + nonce;
			String validate_sha = Utils.getSHA256(mine_string);
			
			if(validate_sha.equals(new_block.getBlockID()))
			{
				// 새로운 블록을 블록 체인에 연결하고, 미승인된 Transaction 비우기
				publicLedger.getBlockchain().add(new_block);
				publicLedger.getUnconfirmedTransaction().clear();
				System.out.println("블록 채굴 성공!");
				System.out.println("New Block is validated!");
			}
			else
			{
				System.out.println("Attack the blockchain!");
				throw new IllegalArgumentException("블록 인증 실패!");
			}
		}
		else
		{
			System.out.println("새로운 Block이 생성되지 않았습니다.");
		}
	}
	
	protected void updateWallet()
	{
		// 각 Transaction 순회하기
		for(int i = 0; i < publicLedger.getNewBlock().getConfirmingtransaction().size(); i++)
		{
			// Transaction에서 Wallet에 필요한 정보 추출
			Transaction confirm_tx = publicLedger.getNewBlock().getConfirmingtransaction().get(i);
			float confirm_coin = confirm_tx.getCoin();
			float confirm_fee = confirm_tx.getFee();
			String confirm_receiver = confirm_tx.getReceiver();
			String confirm_sender = confirm_tx.getSender();
						
			// 송금으로 인한 잔고 차감
			for(int j = 0; j < wallets.size(); j++)
			{
				if(confirm_sender == wallets.get(j).getUserName())
				{
					wallets.get(j).decreaseCoin(confirm_coin + confirm_fee);
				}
			}
			
			// 입금으로 인한 잔고 증가
			for(int k = 0; k < wallets.size(); k++)
			{
				if(confirm_receiver == wallets.get(k).getUserName())
				{
					wallets.get(k).increaseCoin(confirm_coin);
				}
			}
			
			// 송금으로 인한 수수료 획득
			for(int l = 0; l < wallets.size(); l++)
			{
				if("miner" == wallets.get(l).getUserName())
				{
					wallets.get(l).increaseCoin(confirm_fee);
				}
			}
		}	
	}
	
	protected Wallet findWallet(String username)
	{
		for(Wallet buff_wallet : wallets)
		{
			if(username.equals(buff_wallet.getUserName()))
			{
				return buff_wallet;
			}
		}
		return null;
	}
}
