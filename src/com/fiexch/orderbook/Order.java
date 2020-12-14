package com.fiexch.orderbook;

public class Order {
	
	public Order() {
		isDuplicateSeqID = false;
		rejected = false;	
	}
	
	public Order(String orderLine,Integer incomingSequenceID) {
		this.orderLine = orderLine;
		this.incomingSequenceID = incomingSequenceID;
		isDuplicateSeqID = false;
		rejected = false;	
	}

	private String timestamp;
	
	private String broker;
	
    	private String sequenceID;
	
	private String type;
	
	private String symbol;
	
	private String quantity;
	
	private String price;
	
	private String side;
	
	private Integer incomingSequenceID;//The order in which it was received
	
	private String brokerTimeStamp;
	
	private boolean rejected;
	
	private String rejectionCode;
	
	private String orderLine;

	private int countInAMin;
	
	private boolean isDuplicateSeqID;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getQuantity() {
		return quantity;
	}
	
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getSequenceID() {
		return sequenceID;
	}

	public void setSequenceID(String sequenceID) {
		this.sequenceID = sequenceID;
	}
	
	public int getOrderCountInAMin() {
		return countInAMin;
	}
	
	public void setOrderCountInAMin(int count)
	{
		this.countInAMin = count;	
	}

	public boolean isDuplicateSeqID()
	{
		return isDuplicateSeqID;
	}

	public void setDuplicateSeqID(boolean b)
	{
		isDuplicateSeqID = b;
	}

	@Override
	public String toString() {
		return "orderline->" + orderLine + ";" + 
				"isRejected->" + rejected + ";" + 
				"rejectionCode->" + rejectionCode ;
	}

	public String getOrderInFixFormat() {
		StringBuilder sb = new StringBuilder();
		sb.append("35=D|");
		sb.append("52=");
		sb.append(getTimestamp()+"|");
		sb.append("11=");
		sb.append(getSequenceID()+"|");
		sb.append("55=");
		sb.append(getSymbol()+"|");
		sb.append("31=");
		sb.append(getPrice()+"|");
		sb.append("32=");
		sb.append(getQuantity()+"|");
		sb.append("54=");
		if("Buy".equals(getSide()))
		   sb.append("1|");
		else if("Sell".equals(getSide()))
		   sb.append("2|");
		//Append Header and Trailer
		return sb.toString();
	}

	public Integer getIncomingSequenceID() {
		return incomingSequenceID;
	}

	public void setIncomingSequenceID(Integer incomingSequenceID) {
		this.incomingSequenceID = incomingSequenceID;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	
	public String getOrderLine() {
		return orderLine;
	}

	public void setOrderLine(String orderLine) {
		this.orderLine = orderLine;
	}

	public String getRejectionCode() {
		return rejectionCode;
	}

	public void setRejectionCode(String rejectionCode) {
		this.rejectionCode = rejectionCode;
	}

	public String getBrokerTimeStamp() {
		return brokerTimeStamp;
	}

	public void setBrokerTimeStamp(String brokerTimeStamp) {
		this.brokerTimeStamp = brokerTimeStamp;
	}
	
	
}
