package com.order.example.process;


import com.order.example.model.OrderSummary;

import java.io.FileOutputStream;
import java.rmi.server.ExportException;

public class WriteToOrderBook {

    public void writeDataToFile(OrderSummary orderSummary, String targetFileLocation) {


        try {
            FileOutputStream fos = new FileOutputStream(targetFileLocation, true);
            String dataLine = orderSummary.getTimestamp() + ","+orderSummary.getBidPrice() +
                    ","+orderSummary.getAskPrice()+","+orderSummary.getBidSize()+","+orderSummary.getAskSize()+","+orderSummary.getSequenceNumber()+"\n";
            fos.write(dataLine.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
