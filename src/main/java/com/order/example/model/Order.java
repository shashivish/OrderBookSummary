package com.order.example.model;


import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

@Data
public class Order {

    @CsvBindByPosition(position = 0)
    private String host;

    @CsvBindByPosition(position = 1)
    private String sequenceNumber ;

    @CsvBindByPosition(position = 2)
    private String isImage;

    @CsvBindByPosition(position = 3)
    private String addOrderId;

    @CsvBindByPosition(position = 4)
    private String addSide;

    @CsvBindByPosition(position = 5)
    private String addPrice;

    @CsvBindByPosition(position = 6)
    private String addQuantity;


}
