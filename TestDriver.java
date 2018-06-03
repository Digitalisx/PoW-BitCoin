package Homework2;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TestDriver
{	
	public static void main(String[] args)
	{
		Scanner cli = new Scanner(System.in);
		
		//이 리스트를 통해 List<Wallet>을 초기화 할것입니다.
		final int initCoin = 100;
		String[] userName = {"a", "b", "c", "d", "miner"};
		
		//Make Command Scheme, 이걸 기준으로 Exception을 생성하면 편할것입니다.
		String[] commands = {"send", "create_transaction", "block", "wallet", "create_fakeblock", "status", "flush"};
		int[] eachCommandsParameterNum = {4, 1, 1, 1, 0, 0, 0};
		ArrayList<CommandScheme> commandScheme = new ArrayList<CommandScheme>();
		
		for(int i=0; i< commands.length; i++)
		{
			commandScheme.add(new CommandScheme(commands[i], eachCommandsParameterNum[i]));
		}
		
		User dijkstra; //User에는 Wallet과 Ledger가 들어갈것
		Miner euler;  //Miner에도 Wallet과 Ledger가 들어갈것.
		
		Ledger blockchain; //User Node와 Miner Node가 공유할 장부 
		ArrayList<Wallet> wallet = new ArrayList<Wallet>();//User Node와 Miner Node가 공유할 지갑
		
		//Ledger를 초기화

		blockchain = new Ledger();
		
		//Wallet List를 초기화

		for(int i = 0; i < userName.length; i++)
		{
			wallet.add(new Wallet(userName[i], initCoin));
		}
		
		//Ledger와 Wallet 리스트를 초기화하고, 이것을 통해 user node와 miner node를 초기화 하는 코드가 여기 들어간다. 
		
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
				
				// 명령어 분류 및 검증
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
					throw new IllegalArgumentException("존재하지 않는 명령어입니다.");
				}
				
				// send에 대한 명령어와 예외처리
				if(commandIdx == 0)
				{	
					checkargu(commandScheme, commandIdx, cmdSplited.length);
					
					// 존재하는 사용자 명 확인
					String sender_name = cmdSplited[1];
					String receiver_name = cmdSplited[2];
					
					isHaveWallet(userName, sender_name, receiver_name);
					
					// 사용 화폐의 단위는 Float Check
					
					float trans_coin;
					float trans_fee;
					
					try
					{
						trans_coin = Float.parseFloat(cmdSplited[3]);
						trans_fee = Float.parseFloat(cmdSplited[4]);
					}
					catch (Exception e)
					{
						throw new IllegalArgumentException("매개변수는 Float 타입이여야 합니다.");
					}
					
					if(trans_coin < 0 || trans_fee < 0)
					{
						throw new IllegalArgumentException("수수료나 코인 매개변수가 음수입니다.");
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
					
					// Sender 잔고 Check
					if(sender_wallet.getUserCoin() < trans_coin)
					{
						throw new IllegalArgumentException("보내는 사람이 충분한 돈을 가지고 있지 않습니다.");
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
					//create_transaction 명령어의 검증 및 실행
					checkargu(commandScheme, commandIdx, cmdSplited.length);
					int transaction_num;
					
					try
					{
						transaction_num = Integer.parseInt(cmdSplited[1]);
					}
					catch(Exception e)
					{
						throw new IllegalArgumentException("매개변수의 타입이 맞지 않습니다.");
					}
					
					if(transaction_num > 100)
					{
						throw new IllegalArgumentException("너무 많은 트랜잭션 요청");
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
						else // 발생할 수 없는 상황
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
						throw new IllegalArgumentException("매개변수의 타입이 맞지 않습니다.");
					}
					
					Block find_block = blockchain.findBlock(Long.parseLong(cmdSplited[1]));
					System.out.println(find_block);
					for(Transaction sol_t : find_block.getConfirmingtransaction())
					{
						System.out.println(sol_t);
					}
				}
				
				// wallet 명령어 검증
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
						throw new IllegalArgumentException(cmdSplited[1] + " 은 없는 사용자입니다!");
					}
				}
				else if(commandIdx == 4)
				{
					//create_fakeblock 명령어의 검증 및 실행
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
						System.out.println("미승인 Transaction의 개수가 3개 이상이어야 합니다.");
					}
					// 미승인 트랜잭션 체크
					// 미승인에 있던 트랜잭션들 + 보상 트랜잭션 생성
					// 블록으로 만들고 월렛에 반영하기
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
			throw new IllegalArgumentException("매개변수의 갯수가 맞지 않습니다.");
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
			throw new IllegalArgumentException(sender_name + " 은 없는 사용자 입니다.");
		}
		
		if(!receiver_found)
		{
			throw new IllegalArgumentException(receiver_name + " 은 없는 사용자 입니다.");
		}
	}
}
