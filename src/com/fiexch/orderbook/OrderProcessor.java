package com.fiexch.orderbook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class OrderProcessor {
	protected String ordersFilePath = "../resources/trades.csv"; //Path has to be from configuration
	protected String acceptedOrdersFilePath = "../resources/AcceptedOrders.txt";
	protected String rejectedOrdersFilePath = "../resources/RejectedOrders.txt";
	protected String acceptedFixOrdersFilePath =  "../resources/AcceptedFixOrders.txt";
	protected String rejectedFixOrdersFilePath = "../resources/RejectedFixOrders.txt";	
	
	public OrderProcessor() {
	}
	
	public List<Order> readLinesFromCSV(){
		List<Order> orders = null;
		try {
			List<String> lines = Files.readAllLines(Paths.get(getFilePath()));
			System.out.println("Total lines in order " + (lines.size()-1)); //Ignoring CSV file Header
			orders = new ArrayList<>();
			int i = 0;
			for(String line : lines) {
				if(i == 0) {i++; continue;}
				orders.add(new Order(line,i));
				i++;
			}
		} catch (IOException e) {
			System.out.println("io exception");
			e.printStackTrace();
		}
        	return orders;
	}
	
	public void processOrders(List<Order> orders) {
		 Map<String, Integer> orderBrokerTSMap = new HashMap<>();
		 Map<String, List<String>> orderIdsMap = new HashMap<>();
		for(Order order : orders) {
			Utils.populateOrder(order);
			if(!order.isRejected()) {
				int count = 0;
				String brokerTS = order.getBrokerTimeStamp();
				if(orderBrokerTSMap.containsKey(brokerTS))
				{
					count = orderBrokerTSMap.get(brokerTS);
				}
				order.setOrderCountInAMin(++count);
	
				order.setDuplicateSeqID(false);
				if(orderIdsMap.containsKey(order.getBroker()))
				{
					for(String orderId : orderIdsMap.get(order.getBroker()))
					{
						if (orderId.equals(order.getSequenceID()))
							order.setDuplicateSeqID(true);
					}
				}

				RejectOrder rejectOrder = Utils.validateOrder(order);
				if(rejectOrder != null) {
					order.setRejected(true);
					order.setRejectionCode(rejectOrder.getRejectionCode());
					continue;
				}
				
				orderBrokerTSMap.put(brokerTS, count);
				if(orderIdsMap.containsKey(order.getBroker()))
				{
					orderIdsMap.get(order.getBroker()).add(order.getSequenceID());
				}
				else
				{
					List<String> orderIds = new ArrayList<String>();
					orderIds.add(order.getSequenceID());
					orderIdsMap.put(order.getBroker(), orderIds);	
				}
				
			}
		}

		
	}
	
	public void createFiles(List<Order> orders) {
		List<String> rejectedOrders = new ArrayList<>();
		List<String> rejectedFixOrders = new ArrayList<>();
		List<String> acceptedOrders = new ArrayList<>();
		List<String> acceptedFixOrders = new ArrayList<>();
		for(Order order : orders) {
			if(order.isRejected()) {
				rejectedOrders.add(order.getBroker()+" "+order.getSequenceID());
				rejectedFixOrders.add(order.getOrderInFixFormat());
			}else {
				acceptedOrders.add(order.getBroker()+" "+order.getSequenceID());
				acceptedFixOrders.add(order.getOrderInFixFormat());
			}
		}
		
		System.out.println("Total Accepted orders " + acceptedOrders.size());
		Utils.flushOrdersToFile(acceptedOrders,acceptedOrdersFilePath ); //Configurable FilePath
		System.out.println("Total Rejected orders " + rejectedOrders.size());
		Utils.flushOrdersToFile(rejectedOrders, rejectedOrdersFilePath); //Configurable FilePath
		Utils.flushOrdersToFile(acceptedFixOrders,acceptedFixOrdersFilePath); //Configurable FilePath
		Utils.flushOrdersToFile(rejectedFixOrders,rejectedFixOrdersFilePath); //Configurable FilePath
	}
	
	protected String getFilePath() {
		return ordersFilePath;
	}

}
