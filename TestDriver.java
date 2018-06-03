package Homework2;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TestDriver
{	
	public static void main(String[] args)
	{
		Scanner cli = new Scanner(System.in);
		
		//�� ����Ʈ�� ���� List<Wallet>�� �ʱ�ȭ �Ұ��Դϴ�.
		final int initCoin = 100;
		String[] userName = {"a", "b", "c", "d", "miner"};
		
		//Make Command Scheme, �̰� �������� Exception�� �����ϸ� ���Ұ��Դϴ�.
		String[] commands = {"send", "create_transaction", "block", "wallet", "create_fakeblock", "status", "flush"};
		int[] eachCommandsParameterNum = {4, 1, 1, 1, 0, 0, 0};
		ArrayList<CommandScheme> commandScheme = new ArrayList<CommandScheme>();
		
		for(int i=0; i< commands.length; i++)
		{
			commandScheme.add(new CommandScheme(commands[i], eachCommandsParameterNum[i]));
		}
		
		User dijkstra; //User���� Wallet�� Ledger�� ����
		Miner euler;  //Miner���� Wallet�� Ledger�� ����.
		
		Ledger blockchain; //User Node�� Miner Node�� ������ ��� 
		ArrayList<Wallet> wallet = new ArrayList<Wallet>();//User Node�� Miner Node�� ������ ����
		
		//Ledger�� �ʱ�ȭ

		blockchain = new Ledger();
		
		//Wallet List�� �ʱ�ȭ

		for(int i = 0; i < userName.length; i++)
		{
			wallet.add(new Wallet(userName[i], initCoin));
		}
		
		//Ledger�� Wallet ����Ʈ�� �ʱ�ȭ�ϰ�, �̰��� ���� user node�� miner node�� �ʱ�ȭ �ϴ� �ڵ尡 ���� ����. 
		
		dijkstra = new User(blockchain, wallet);
		euler = new Miner(blockchain, wallet, "miner");
		
		String cmd = "";
		String[] cmdSplited;
		int commandIdx = -1;
		while(true)
		{
			try
			{
				System.out.print(">");
				System.out.flush();
				cmd = cli.nextLine();
				cmdSplited = cmd.split(" ");
				
				// ��ɾ� �з� �� ����
				int count = 0;
				
				boolean command_exist = false;
				
				for(String command:commands)
				{
					if(command.equals(cmdSplited[0]))
					{
						commandIdx = count;
						command_exist = true;
						break;
					}
					count += 1;
				}
				
				if(!command_exist)
				{
					throw new IllegalArgumentException("�������� �ʴ� ��ɾ��Դϴ�.");
				}
				
				// send�� ���� ��ɾ�� ����ó��
				if(commandIdx == 0)
				{	
					checkargu(commandScheme, commandIdx, cmdSplited.length);
					
					// �����ϴ� ����� �� Ȯ��
					String sender_name = cmdSplited[1];
					String receiver_name = cmdSplited[2];
					
					isHaveWallet(userName, sender_name, receiver_name);
					
					// ��� ȭ���� ������ Float Check
					
					float trans_coin;
					float trans_fee;
					
					try
					{
						trans_coin = Float.parseFloat(cmdSplited[3]);
						trans_fee = Float.parseFloat(cmdSplited[4]);
					}
					catch (Exception e)
					{
						throw new IllegalArgumentException("�Ű������� Float Ÿ���̿��� �մϴ�.");
					}
					
					if(trans_coin < 0 || trans_fee < 0)
					{
						throw new IllegalArgumentException("�����ᳪ ���� �Ű������� �����Դϴ�.");
					}
					
					Wallet sender_wallet = null;
					Wallet receiver_wallet = null;
					
					for(int i = 0; i < wallet.size(); i++)
					{
						if(sender_name.equals(wallet.get(i).getUserName()))
						{
							sender_wallet = wallet.get(i);
						}
					}
					
					for(int j = 0; j < wallet.size(); j++)
					{
						if(receiver_name.equals(wallet.get(j).getUserName()))
						{
							receiver_wallet = wallet.get(j);
						}
					}
					
					// Sender �ܰ� Check
					if(sender_wallet.getUserCoin() < trans_coin)
					{
						throw new IllegalArgumentException("������ ����� ����� ���� ������ ���� �ʽ��ϴ�.");
					}
					
					dijkstra.sendTransaction(sender_wallet, receiver_wallet, trans_coin, trans_fee);
					
					if(blockchain.getUnconfirmedTransaction().size() >= 5)
					{
						euler.mine(100, 1);
						dijkstra.validateNewBlock();
						dijkstra.updateWallet();
					}
				}
				else if(commandIdx == 1)
				{
					//create_transaction ��ɾ��� ���� �� ����
					checkargu(commandScheme, commandIdx, cmdSplited.length);
					int transaction_num;
					
					try
					{
						transaction_num = Integer.parseInt(cmdSplited[1]);
					}
					catch(Exception e)
					{
						throw new IllegalArgumentException("�Ű������� Ÿ���� ���� �ʽ��ϴ�.");
					}
					
					if(transaction_num > 100)
					{
						throw new IllegalArgumentException("�ʹ� ���� Ʈ����� ��û");
					}
					
					Random generator = new Random();
					
					for(int i = 0; i < transaction_num; i++)
					{
						int first_number = generator.nextInt(5);
						String first_name = wallet.get(first_number).getUserName();
						
						int second_number = generator.nextInt(5);
						String second_name = wallet.get(second_number).getUserName();
					
						Wallet send_wallet = dijkstra.findWallet(first_name);
						Wallet receive_wallet = dijkstra.findWallet(second_name);
						
						float trans_coin;
						float trans_fee;
						
						if(send_wallet.getUserCoin() < 3)
						{
							trans_coin = 0.0F;
							trans_fee = 0.0F;
						}
						else if(send_wallet.getUserCoin() >= 3)
						{
							trans_coin = send_wallet.getRandomCoinToSend();
							trans_fee = trans_coin / 10;
						}
						else // �߻��� �� ���� ��Ȳ
						{
							trans_coin = 0.0F;
							trans_fee = 0.0F;
						}
						
						dijkstra.sendTransaction(send_wallet, receive_wallet, trans_coin, trans_fee);
						
						if(blockchain.getUnconfirmedTransaction().size() >= 5)
						{
							euler.mine(100, 1);
							dijkstra.validateNewBlock();
							dijkstra.updateWallet();
						}
					}
				}
				else if(commandIdx == 2)
				{
					checkargu(commandScheme, commandIdx, cmdSplited.length);
					
					try
					{
						int block_height = Integer.parseInt(cmdSplited[1]);
					}
					catch(Exception e)
					{
						throw new IllegalArgumentException("�Ű������� Ÿ���� ���� �ʽ��ϴ�.");
					}
					
					Block find_block = blockchain.findBlock(Long.parseLong(cmdSplited[1]));
					System.out.println(find_block);
					for(Transaction sol_t : find_block.getConfirmingtransaction())
					{
						System.out.println(sol_t);
					}
				}
				
				// wallet ��ɾ� ����
				else if(commandIdx == 3)
				{
					checkargu(commandScheme, commandIdx, cmdSplited.length);
					
					boolean wallet_found = false;
					for(Wallet buff_w:wallet)
					{
						if(cmdSplited[1].equals(buff_w.getUserName()))
						{
							wallet_found = true;
							System.out.println(buff_w);
						}
					}
					
					if(!wallet_found)
					{
						throw new IllegalArgumentException(cmdSplited[1] + " �� ���� ������Դϴ�!");
					}
				}
				else if(commandIdx == 4)
				{
					//create_fakeblock ��ɾ��� ���� �� ����
					checkargu(commandScheme, commandIdx, cmdSplited.length);
					
					for(int i = 0; i < 5; i++)
					{
						Wallet send_wallet = dijkstra.findWallet("a");
						Wallet receive_wallet = dijkstra.findWallet("b");
						
						float trans_coin = 0.1F;
						float trans_fee = 0.1F;
									
						dijkstra.sendTransaction(send_wallet, receive_wallet, trans_coin, trans_fee);
						
						if(blockchain.getUnconfirmedTransaction().size() >= 5)
						{
							euler.mine(100, 0);
							dijkstra.validateNewBlock();
						}
					}
					
				}
				else if(commandIdx == 5)
				{
					checkargu(commandScheme, commandIdx, cmdSplited.length);
					System.out.println(wallet);
					int blockchain_length = blockchain.getBlockchain().size();
					System.out.printf("Curren Height : %d\n", blockchain_length);
					if(blockchain.getBlockchain().size() > 0)
					{
						System.out.println("=========================Up to Date Block===============");
						System.out.println(blockchain.getBlockchain().get(blockchain_length - 1));
						for(Transaction sol_t : blockchain.getBlockchain().get(blockchain_length - 1).getConfirmingtransaction())
						{
							System.out.println(sol_t);
						}
					}
					System.out.println("=========================Unconfirmed Transaction===============");
					System.out.println(blockchain.getUnconfirmedTransaction());
						
				}
				else if(commandIdx == 6)
				{
					checkargu(commandScheme, commandIdx, cmdSplited.length);
					
					if(blockchain.getUnconfirmedTransaction().size() >= 3)
					{
						euler.mine(100, 1);
						dijkstra.validateNewBlock();
						dijkstra.updateWallet();
					}
					else
					{
						System.out.println("�̽��� Transaction�� ������ 3�� �̻��̾�� �մϴ�.");
					}
					// �̽��� Ʈ����� üũ
					// �̽��ο� �ִ� Ʈ����ǵ� + ���� Ʈ����� ����
					// ������� ����� ������ �ݿ��ϱ�
				}	
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}	
		}
	}
	
	public static void checkargu(ArrayList<CommandScheme> commandScheme, int commandIDx, int length)
	{
		int command_num = commandScheme.get(commandIDx).parameterNum;
		if((length - 1) != command_num)
		{
			throw new IllegalArgumentException("�Ű������� ������ ���� �ʽ��ϴ�.");
		}
	}
	
	public static void isHaveWallet(String[] userName, String sender_name, String receiver_name)
	{
		boolean sender_found = false;
		boolean receiver_found = false;
		
		for(String name:userName)
		{
			if(name.equals(sender_name))
			{
				sender_found = true;
			}
			
			if(name.equals(receiver_name))
			{
				receiver_found = true;
			}
		}
		
		if(!sender_found)
		{
			throw new IllegalArgumentException(sender_name + " �� ���� ����� �Դϴ�.");
		}
		
		if(!receiver_found)
		{
			throw new IllegalArgumentException(receiver_name + " �� ���� ����� �Դϴ�.");
		}
	}
}
