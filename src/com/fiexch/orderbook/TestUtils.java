package com.fiexch.orderbook;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestUtils {

	private String validOrderLine="10/5/2017 10:00:23,Fidelity,1,2,BARK,100,1.195,Buy";//ValidOrder
	private String invalidOrderLine="10/5/2017 10:00:23,Fidelity,1,2,BARK,100,1.195";//Does not have last column
	private String invalidSymbolLine="10/5/2017 10:00:23,Fidelity,1,2,BARR,100,1.195,Buy";//Symbol is not from defined List
	private String invalidBrokerLine="10/5/2017 10:00:23,FidelityPartners,1,2,BARK,100,1.195,Buy";//Broker is not from defined List
	private String validOrderLine2="10/5/2017 10:00:33,Fidelity,2,2,BARK,100,1.195,Buy";//ValidOrder
	private String validOrderLine3="10/5/2017 10:00:43,Fidelity,3,2,BARK,100,1.195,Buy";//ValidOrder
	private String validOrderLine4="10/5/2017 10:00:53,Fidelity,4,2,BARK,100,1.195,Buy";//ValidOrder
	private String inValidOrderEmptyField="10/5/2017 10:00:53,Fidelity,4,2,BARK,,1.195,Buy";//InValidOrder
	
	@Test
	public void testPopulateValidOrder() {
		Order order = new Order(validOrderLine,1);
		Utils.populateOrder(order);
		RejectOrder rejectOrder = Utils.validateOrder(order);
		assertTrue(rejectOrder == null);
	}
	
	@Test
	public void testPopulateOrderInvalidLine() {
		Order order = new Order(invalidOrderLine,1);
		Utils.populateOrder(order);
		assertTrue(order.isRejected());
		assertEquals(order.getRejectionCode(),Utils.INVALID_DATA);
	}
	
	@Test
	public void testPopulateOrderInvalidSymbol() {
		Order order = new Order(invalidSymbolLine,1);
		Utils.populateOrder(order);
		RejectOrder rejectOrder = Utils.validateOrder(order);
		assertNotNull(rejectOrder);
		assertEquals(rejectOrder.getRejectionCode(),Utils.INVALID_SYMBOL);
	}
	
	@Test
	public void testPopulateOrderInvalidBroker() {
		Order order = new Order(invalidBrokerLine,1);
		Utils.populateOrder(order);
		RejectOrder rejectOrder = Utils.validateOrder(order);
		assertNotNull(rejectOrder);
		assertEquals(rejectOrder.getRejectionCode(),Utils.INVALID_BROKER);
	}
	
	@Test
	public void testPopulate4OrdersInAMin() {
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(validOrderLine,1));
		orders.add(new Order(validOrderLine2,2));
		orders.add(new Order(validOrderLine3,3));
		orders.add(new Order(validOrderLine4,4));
		OrderProcessor orderProcessor = new OrderProcessor();
		orderProcessor.processOrders(orders);
		assertTrue(orders.get(3).isRejected());
		assertEquals(orders.get(3).getRejectionCode(),Utils.INVALID_TIMESTAMP);
	}
	
	@Test
	public void testPopulateOrderDuplicateSeqID() {
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(validOrderLine,1));
		orders.add(new Order(validOrderLine,1));
		OrderProcessor orderProcessor = new OrderProcessor();
		orderProcessor.processOrders(orders);
		assertTrue(orders.get(1).isRejected());
		assertEquals(orders.get(1).getRejectionCode(),Utils.INVALID_SEQID);
	}
	

	@Test
	public void testIsEmpty() {
		Order order = new Order(inValidOrderEmptyField,1);
		Utils.populateOrder(order);
		RejectOrder rejectOrder = Utils.validateOrder(order);
		assertNotNull(rejectOrder);
		assertEquals(rejectOrder.getRejectionCode(),Utils.INSUFFICIENT_DATA);
	}
	
	@Test
	public void testProcessValidOrders() {
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(validOrderLine,1));
		orders.add(new Order(validOrderLine2,2));
		OrderProcessor orderProcessor = new OrderProcessor();
		orderProcessor.processOrders(orders);
		assertFalse(orders.get(0).isRejected());
		assertFalse(orders.get(1).isRejected());
	}
	
}
