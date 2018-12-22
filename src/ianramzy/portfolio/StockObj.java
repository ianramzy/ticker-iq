package ianramzy.portfolio;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class StockObj {
    private SimpleStringProperty symbol, entry, quantity, price, PL, PLP;

    public StockObj(String symbol, String entry, String quantity, String price, String PL, String PLP) {
        this.symbol = new SimpleStringProperty(symbol);
        this.entry = new SimpleStringProperty(entry);
        this.quantity = new SimpleStringProperty(quantity);
        this.price = new SimpleStringProperty(price);
        this.PL = new SimpleStringProperty(PL);
        this.PLP = new SimpleStringProperty(PLP);
    }

    public String getPL() {
        return PL.get();
    }

    public void setPL(String PL) {
        this.PL.set(PL);
    }

    public String getPLP() {
        return PLP.get();
    }

    public void setPLP(String PLP) {
        this.PLP.set(PLP);
    }

    public String getPrice() {
        return price.get();
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getQuantity() {
        return quantity.get();
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public String getSymbol() {
        return symbol.get();
    }

    public void setSymbol(String symbol) {
        this.symbol = new SimpleStringProperty(symbol);
    }

    public String getEntry() {
        return entry.get();
    }

    public void setEntry(String entry) {
        this.entry = new SimpleStringProperty(entry);
    }

    public String toString() {
        return String.format("%s %s", symbol, entry, quantity, PL, PLP);
    }
}
