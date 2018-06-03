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
	
	// Transaction�� �����Ѵ�.
	public void sendTransaction(Wallet sender, Wallet receiver, float coin, float fee)
	{
		Transaction new_transaction = new Transaction(sender, receiver, coin, fee);
		publicLedger.getUnconfirmedTransaction().add(new_transaction);
	}
	
	// ���ο� Block�� ���� �����Ѵ�.
	public void validateNewBlock()
	{
		if(publicLedger.isNewBlockCreated() == true)
		{
			// �ű� ����� �Ӽ� �����ؼ� ������ �غ���
			Block new_block = publicLedger.getNewBlock();
			String preblock_id = new_block.getPreBlockID();
			String merkle_root = new_block.getMerkleRoot();
			String nonce = new_block.getNonce();
			String mine_string = preblock_id + merkle_root + nonce;
			String validate_sha = Utils.getSHA256(mine_string);
			
			if(validate_sha.equals(new_block.getBlockID()))
			{
				// ���ο� ����� ��� ü�ο� �����ϰ�, �̽��ε� Transaction ����
				publicLedger.getBlockchain().add(new_block);
				publicLedger.getUnconfirmedTransaction().clear();
				System.out.println("��� ä�� ����!");
				System.out.println("New Block is validated!");
			}
			else
			{
				System.out.println("Attack the blockchain!");
				throw new IllegalArgumentException("��� ���� ����!");
			}
		}
		else
		{
			System.out.println("���ο� Block�� �������� �ʾҽ��ϴ�.");
		}
	}
	
	protected void updateWallet()
	{
		// �� Transaction ��ȸ�ϱ�
		for(int i = 0; i < publicLedger.getNewBlock().getConfirmingtransaction().size(); i++)
		{
			// Transaction���� Wallet�� �ʿ��� ���� ����
			Transaction confirm_tx = publicLedger.getNewBlock().getConfirmingtransaction().get(i);
			float confirm_coin = confirm_tx.getCoin();
			float confirm_fee = confirm_tx.getFee();
			String confirm_receiver = confirm_tx.getReceiver();
			String confirm_sender = confirm_tx.getSender();
						
			// �۱����� ���� �ܰ� ����
			for(int j = 0; j < wallets.size(); j++)
			{
				if(confirm_sender == wallets.get(j).getUserName())
				{
					wallets.get(j).decreaseCoin(confirm_coin + confirm_fee);
				}
			}
			
			// �Ա����� ���� �ܰ� ����
			for(int k = 0; k < wallets.size(); k++)
			{
				if(confirm_receiver == wallets.get(k).getUserName())
				{
					wallets.get(k).increaseCoin(confirm_coin);
				}
			}
			
			// �۱����� ���� ������ ȹ��
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
