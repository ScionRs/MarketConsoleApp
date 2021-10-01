package ru.ravilov.market;

public class Customer {

   private int id;
   private int cash;

    public Customer(int cash) {
        this.cash = cash;
    }

    public Customer(int id, int cash) {
        this.id = id;
        this.cash = cash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCash() {
        return cash;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
}
