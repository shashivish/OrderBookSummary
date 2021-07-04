package com.order.example.process;

import com.order.example.model.Order;
import com.order.example.model.OrderSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProcessOrder {

    private static final Logger log = LoggerFactory.getLogger(ProcessOrder.class);


    public OrderSummary processOrder(String sequenceNumber, List<Order> orderList) {


        OrderSummary orderSummary = new OrderSummary();


        List<Order> buyList = new ArrayList<>();
        List<Order> sellList = new ArrayList<>();
        String timeStamp = "";
        float minPrice = Integer.MAX_VALUE;
        float maxPrice = 0;
        int totalAskSize = 0;
        int totalBidSize = 0;


        //log.info("Size of Order List : {} ", orderList.size());


        /**
         * Split BUY and SELL List
         */
        for (Order order : orderList) {

            timeStamp = order.getHost();

            if (order.getAddSide().equalsIgnoreCase("BUY")) {
                if (order.getAddOrderId() != null && order.getAddOrderId().length() > 0)
                    buyList.add(order);
            }

            if (order.getAddSide().equalsIgnoreCase("SELL")) {
                if (order.getAddOrderId() != null && order.getAddOrderId().length() > 0)
                    sellList.add(order);
            }
        }

        //Process BUY with Max Price


        if (buyList.size() > 0) {
            // log.info("Total Number of Records to be Processed in BUY List  : {} ", buyList.size());



            for (Order order : buyList) {

                if (order.getAddPrice() != null && order.getAddPrice().length() != 0 && order.getAddPrice() != " ") {
                    if (Float.valueOf(order.getAddPrice()) > maxPrice)
                        maxPrice = Float.valueOf(order.getAddPrice());
                }
            }



            for (Order order : buyList) {
                if (order.getAddPrice() != null && order.getAddPrice().length() != 0 && order.getAddPrice() != " ") {
                    if (maxPrice == Float.valueOf(order.getAddPrice()))
                        totalBidSize = totalBidSize + Integer.valueOf(order.getAddQuantity());
                }

            }

            orderSummary.setSequenceNumber(sequenceNumber);
            orderSummary.setBidPrice(maxPrice);
            orderSummary.setBidSize(totalBidSize);
            orderSummary.setTimestamp(timeStamp);

        }


        //Process SELL with Min Price

        if (sellList.size() > 0) {

            for (Order order : sellList) {

                if (order.getAddPrice() != null && order.getAddPrice().length() != 0 && order.getAddPrice() != " ") {
                    if (Float.valueOf(order.getAddPrice()) < minPrice)
                        minPrice = Float.valueOf(order.getAddPrice());
                }
            }



            for (Order order : sellList) {
                if (order.getAddPrice() != null && order.getAddPrice().length() != 0 && order.getAddPrice() != " ") {
                    if (minPrice == Float.valueOf(order.getAddPrice()))
                        totalAskSize = totalAskSize + Integer.valueOf(order.getAddQuantity());
                }

            }


            orderSummary.setAskPrice(minPrice);
            orderSummary.setAskSize(totalAskSize);
            //  log.info("Total Number of Records to be Processed in SELL List  : ", sellList.size());
        }

       // log.info("For Sequence Number  : {}  , Max Price : {}  Quantity : {} | Min Price : {} , Quantity : {} ", sequenceNumber, maxPrice, totalBidSize , minPrice,totalAskSize);

        return  orderSummary;

    }

}
