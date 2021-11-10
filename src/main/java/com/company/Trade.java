package com.company;

public class Trade {
    private String epic;
    private String isin;
    private String tradeRef;
    private String tradeType;
    private int quantity;
    private double price;
    private double value;

    public Trade() {
    }

    public Trade(String epic, String isin, String tradeRef, String tradeType, int quantity, double price) {
        this.epic = epic;
        this.isin = isin;
        this.tradeRef = tradeRef;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.price = price;
        this.value = quantity * price;
    }

    public String getEpic() {
        return epic;
    }

    public void setEpic(String epic) {
        this.epic = epic;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getTradeRef() {
        return tradeRef;
    }

    public void setTradeRef(String tradeRef) {
        this.tradeRef = tradeRef;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setValue() {
        this.value = this.price * this.quantity;
    }

    @Override
    public String toString() {
        return "{\"epic=\": \"" + epic + "\"" +
                ", \"isin\": \"" + isin + "\"" +
                ", \"trade_ref\": \"" + tradeRef + "\"" +
                ", \"trade_type\": \"" + tradeType + "\"" +
                ", \"quantity\": " + quantity +
                ", \"price\": " + price +
                ", \"value\": " + value +
                '}';
    }
}
