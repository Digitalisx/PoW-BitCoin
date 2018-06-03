package Homework2;

import java.util.Random;

public class Wallet
{
	private String userName;
	private float userCoin;
	
	public Wallet() {}
	public Wallet(String userName, float initCoin)
	{
		this.userName = userName;
		this.userCoin = initCoin;
	}
	
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public float getUserCoin()
	{
		return userCoin;
	}
	
	public void setUserCoin(float userCoin)
	{
		this.userCoin = userCoin;
	}
	
	public float getRandomCoinToSend()
	{
		float random_coin;
		Random generator = new Random();
		
		while(true)
		{
			// User 소유 금액 (정수 단위로 랜덤 생성)
			// 소수점 0.1 단위 조절
			int int_number = generator.nextInt(Math.round(userCoin));
			float double_number = generator.nextInt(10) / 10;
		
			random_coin = int_number + double_number;
			
			if(random_coin <= userCoin)
			{
				return random_coin;
			}
		}
	}
	
	// Coin Increase & Decrease
	public void increaseCoin(float coin)
	{
		this.userCoin += coin;
	}
	public void decreaseCoin(float coin)
	{
		this.userCoin -= coin;
	}
	
	@Override
	public String toString()
	{
		return "\n========== Wallet ==========\n"+"User Name : " + userName + "\nUser Coin : " + userCoin + "\n============================";
	}
}
