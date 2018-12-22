package ianramzy;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.Period;
import java.time.LocalDate;
import java.time.Period;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class StockObj {
    private SimpleStringProperty symbol, pricePaid;

    public StockObj(String symbol, String pricePaid) {
        this.symbol = new SimpleStringProperty(symbol);
        this.pricePaid = new SimpleStringProperty(pricePaid);
    }

    public String getSymbol() {
        return symbol.get();
    }

    public void setSymbol(String symbol) {
        this.symbol = new SimpleStringProperty(symbol);
    }

    public String getPricePaid() {
        return pricePaid.get();
    }

    public void setPricePaid(String pricePaid) {
        this.pricePaid = new SimpleStringProperty(pricePaid);
    }

    public String toString() {
        return String.format("%s %s", symbol, pricePaid);
    }
}