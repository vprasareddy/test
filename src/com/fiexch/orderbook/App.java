package com.fiexch.orderbook;
  
import java.util.List;

public class App {

        public static void main(String[] args) {
                System.out.println("Start App");
                //Read CSV file
                OrderProcessor orderProcessor = new OrderProcessor();
                List<Order> orders = orderProcessor.readLinesFromCSV();
                if(orders != null) {
                        orderProcessor.processOrders(orders);
                        orderProcessor.createFiles(orders);
                }
                //Debug Info
                for(Order order : orders) {
                        System.out.println(order.toString());
                }
        }
}
