package com.order.example.model;

public class OrderSummary {

    private String timestamp;
    private float bidPrice;
    private float askPrice;
    private int bidSize;
    private int askSize;
    private String sequenceNumber;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public float getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(float bidPrice) {
        this.bidPrice = bidPrice;
    }

    public float getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(float askPrice) {
        this.askPrice = askPrice;
    }

    public int getBidSize() {
        return bidSize;
    }

    public void setBidSize(int bidSize) {
        this.bidSize = bidSize;
    }

    public int getAskSize() {
        return askSize;
    }

    public void setAskSize(int askSize) {
        this.askSize = askSize;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public String toString() {
        return "OrderSummary{" +
                "timestamp='" + timestamp + '\'' +
                ", bidPrice=" + bidPrice +
                ", askPrice=" + askPrice +
                ", bidSize=" + bidSize +
                ", askSize=" + askSize +
                ", sequenceNumber='" + sequenceNumber + '\'' +
                '}';
    }
}
