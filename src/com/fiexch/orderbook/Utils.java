package com.fiexch.orderbook;

import java.util.Arrays;
import java.util.List;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
	//To be Read from symbols.txt
    private static final List<String> SYMBOLS = Arrays.asList("BARK","CARD","BRIC", "LOUD", "GLOO", "YLLW", "BRIC", "KRIL", "LGHT", "VELL");

	//To be Read from firms.txt
    private static final List<String> BROKERS = Arrays.asList("Fidelity","Charles Schwab", "Edward Jones", "Ameriprise Financial","TD Ameritrade", "Raymond James Financial", "AXA Advisors", "LPL Financial", "National Planning Corporation", "Wells Fargo AdvisorsWaddell & Reed", "Transamerica Financial");
	public static final String INSUFFICIENT_DATA = "Insufficient data";
	public static final String INVALID_TIMESTAMP = "Invalid Timestamp";
	public static final String INVALID_SYMBOL = "Invalid Symbol";
	public static final String INVALID_BROKER= "Invalid Broker";
	public static final String INVALID_DATA= "Invalid Data";
	public static final String INVALID_SEQID= "Invalid Trade Seq ID";
	
	public static void populateOrder(Order order) {
		//Populate order from orderline.
		if(order == null || order.getOrderLine() == null) {
			return;
		}
		String orderLine = order.getOrderLine();
		String[] columns = orderLine.split(",");
		if(columns.length != 8) {
			order.setRejected(true);
			order.setRejectionCode(INVALID_DATA);
			return;
		}
		order.setTimestamp(columns[0]);
		order.setBroker(columns[1]);
		order.setSequenceID(columns[2]);
		order.setType(columns[3]);
		order.setSymbol(columns[4]);
		order.setQuantity(columns[5]);
		order.setPrice(columns[6]);
		order.setSide(columns[7]);
		String timeStampUptoMin = columns[0].trim().substring(0, columns[0].trim().length() - 3); //timestamp remove whitespaces, seconds and colon
		order.setBrokerTimeStamp(columns[1]+timeStampUptoMin); //BrokerName+timeStampUptoMin
	}

	public static RejectOrder validateOrder(Order order) {
		//Applying rules
		//broker,symbol,type,sequenceID,quantity,side,price are required.
		if(isEmpty(order.getBroker()) || isEmpty(order.getSymbol()) || isEmpty(order.getSequenceID())
				|| isEmpty(order.getQuantity()) || isEmpty(order.getSide()) || isEmpty(order.getPrice())) {
			System.out.println("Rejecting order line " + (order.getIncomingSequenceID() + 1) + " as some values are empty for trade-> " + order);
			return new RejectOrder(INSUFFICIENT_DATA);
		}
		
		
		//Only symbols traded on the exchange should be traded.
		if(!SYMBOLS.contains(order.getSymbol())) { 
			//null check already done above.
			//doing exact match for now. Not considering case sensitive symbols
			System.out.println("Rejecting order line " + (order.getIncomingSequenceID() + 1) + " as symbol " + order.getSymbol() + " is not valid from Broker:"+order.getBroker());
			return new RejectOrder(INVALID_SYMBOL);
		}
		
		//Only brokers allowed on the exchange should be trading.
		if(!BROKERS.contains(order.getBroker())) { 
			System.out.println("Rejecting order line " + (order.getIncomingSequenceID() + 1) + " as broker " + order.getBroker() + " is not valid");
			return new RejectOrder(INVALID_BROKER);
		}

		//check timestamp is within limit for a trade submitted by a broker
		if(order.getOrderCountInAMin()>3) { 
			System.out.println("Rejecting order line " + (order.getIncomingSequenceID() + 1) + " as TimeStampCount " + order.getOrderCountInAMin() + " is not valid from Broker:"+order.getBroker());
			return new RejectOrder(INVALID_TIMESTAMP);
		}

		//check a trade is already submitted by a broker with this SeqID
		if(order.isDuplicateSeqID()) { 
			System.out.println("Rejecting order line " + (order.getIncomingSequenceID() + 1) + " as SeqID " + order.getSequenceID() + " is Duplicate and not valid from Broker:"+order.getBroker());
			return new RejectOrder(INVALID_SEQID);
		}
		return null;
	}
	
	public static void flushOrdersToFile(List<String> orders, String filePath)
	{
	  	PrintWriter pw = null;
		try {
			File file = new File(filePath);
			FileWriter fw = new FileWriter(file, false);
			pw = new PrintWriter(fw);
			for(String line: orders)
				pw.println(line);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
  		}	
	}
	
	protected static boolean isEmpty(String value) {
		return (value == null || value.trim().length() == 0);
	}
	
}
