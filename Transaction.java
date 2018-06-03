package Homework2;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Transaction implements Comparable<Transaction>
{
	private String txID;
	private String sender;
	private float coin;
	private String receiver;
	private float fee;
	private String timeStamp;
	
	// Millisecond Support TimeStamp Generate
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	Calendar calendar = Calendar.getInstance();
	
	// Transaction ID는 TimeStamp String에 대한 SHA 256 Hash 값으로 결정
	// 일반적인 Transaction
	public Transaction(Wallet sender, Wallet receiver, float coin, float f)
	{ 
		this.sender = sender.getUserName();
		this.receiver = receiver.getUserName();
		this.coin = coin;
		this.fee = f;
		this.timeStamp = dateFormat.format(calendar.getTime());
		this.txID = Utils.getSHA256(timeStamp); 
	}
	
	// 채굴 보상 Transaction (보내는 사람 X)
	public Transaction(Wallet receiver, float coin, float f)
	{
		this.sender = "";
		this.receiver = receiver.getUserName();
		this.coin = coin;
		this.fee = f;
		this.timeStamp = dateFormat.format(calendar.getTime());
		this.txID = Utils.getSHA256(timeStamp);
	}
	
	// Attribute Getter & Setter
	public String getTxID()
	{
		return txID;
	}
	public void setTxID(String txID)
	{
		this.txID = txID;
	}
	public String getSender()
	{
		return sender;
	}
	public void setSender(String sender)
	{
		this.sender = sender;
	}
	public float getCoin()
	{
		return coin;
	}
	public void setCoin(float coin)
	{
		this.coin = coin;
	}
	public String getReceiver()
	{
		return receiver;
	}
	public void setReceiver(String receiver)
	{
		this.receiver = receiver;
	}
	public float getFee()
	{
		return fee;
	}
	public void setFee(float fee)
	{
		this.fee = fee;
	}
	public String getTimeStamp()
	{
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString()
	{
		
		return "\n======================= Transaction " + txID + " ======================="
			   + "\nSender : " + sender + "\nReceiver : " + receiver + "\nCoin : " + String.format("%.6f", coin) + " Fee : " + String.format("%.6f", fee)
			   + "\nTimeStamp : " + timeStamp + "\n==============================================";
	}
	
	@Override
	public int compareTo(Transaction o) {
		// TODO Auto-generated method stub
		if(this.fee < o.fee)
		{
			return 1;
		}
		else if(this.fee > o.fee)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}	
}
