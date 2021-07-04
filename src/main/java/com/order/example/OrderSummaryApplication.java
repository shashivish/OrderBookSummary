package com.order.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.order.example.model.Order;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import com.order.example.model.OrderSummary;
import com.order.example.process.ProcessOrder;
import com.order.example.process.WriteToOrderBook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OrderSummaryApplication {

    private static final Logger log = LoggerFactory.getLogger(OrderSummaryApplication.class);


    public static void main(String[] args) throws IOException {
        String l3InputData = args[0];
        String orderBookOutputPath = args[1];


        //If Outfile is Present , then delete and re-create;
        File f = new File(orderBookOutputPath);
        if(f.exists() && !f.isDirectory()) {
           f.delete();
           f.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(orderBookOutputPath, true);
        String header = "time,bid_price,ask_price,bid_size,ask_size,seq_num \n";
        fos.write(header.getBytes());
        fos.close();



        OrderSummaryApplication orderSummaryApplication = new OrderSummaryApplication();
        orderSummaryApplication.readInputDataFile(l3InputData, orderBookOutputPath);

    }

    public void readInputDataFile(String inputPath, String outputDataPath) {


        Map<String, List<Order>> sequenceNumberOrderMap = new LinkedHashMap<>();
        List<Order> orderList = new ArrayList<>();

        try (
                Reader reader = Files.newBufferedReader(Paths.get(inputPath));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            // Reading Records One by One in a String array

            csvReader.readNext(); // Ignore Header Row

            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                List<Order> sequenceOrderList = new ArrayList<>();

                Order order = new Order();
                order.setHost(nextRecord[0]);
                order.setSequenceNumber(nextRecord[1]);
                order.setIsImage(nextRecord[2]);
                order.setAddOrderId(nextRecord[3]);
                order.setAddSide(nextRecord[4]);
                order.setAddPrice(nextRecord[5]);
                order.setAddQuantity(nextRecord[6]);

                String sequenceNumber = order.getSequenceNumber();

                if (sequenceNumberOrderMap.containsKey(sequenceNumber)) {
                    //If Sequence Number Already Present Update Order List
                    sequenceNumberOrderMap.get(sequenceNumber).add(order);
                } else {
                    //Add New Sequence Number
                    sequenceOrderList.add(order);
                    sequenceNumberOrderMap.put(sequenceNumber, sequenceOrderList);
                }


            }


        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }


        log.info("Distinct Sequence Number : {} ", sequenceNumberOrderMap.size());

        ProcessOrder processOrder = new ProcessOrder();
        WriteToOrderBook writeToOrderBook = new WriteToOrderBook();




        AtomicReference<OrderSummary> bookedOrderSummary = new AtomicReference<>(new OrderSummary());
        sequenceNumberOrderMap.forEach((x, y) ->
        {

            OrderSummary orderSummary = processOrder.processOrder(x, y);

            if (bookedOrderSummary.get().getSequenceNumber() == null || bookedOrderSummary.get().getSequenceNumber() == "") {
                bookedOrderSummary.set(orderSummary);
                writeToOrderBook.writeDataToFile(bookedOrderSummary.get(),outputDataPath);
            } else {

                boolean isWrite = false;
                OrderSummary orderSummaryUpdated = bookedOrderSummary.get();

                float bidPrice = bookedOrderSummary.get().getBidPrice();

                if (orderSummary.getBidPrice() > bidPrice) // If greater then discard old price and quantity
                {
                    orderSummaryUpdated.setBidPrice(orderSummary.getBidPrice());
                    orderSummaryUpdated.setBidSize(orderSummary.getBidSize());
                    isWrite = true;
                }

                if (orderSummary.getBidPrice() == bidPrice) //  If Equal than add quantity
                {
                    orderSummaryUpdated.setBidSize(bookedOrderSummary.get().getBidSize() + orderSummary.getBidSize());
                    isWrite = true;
                }

                if (orderSummary.getAskPrice() == bookedOrderSummary.get().getAskPrice()) {
                    orderSummaryUpdated.setAskSize(bookedOrderSummary.get().getAskSize() + orderSummary.getAskSize());
                    isWrite = true;
                }

                if (isWrite) {
                    bookedOrderSummary.set(orderSummaryUpdated);
                    writeToOrderBook.writeDataToFile(bookedOrderSummary.get(),outputDataPath);
                }

            }


        });


    }

}
