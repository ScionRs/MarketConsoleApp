package ru.ravilov.market;

public class Seller {
    private int id;
    private String itemName;
    private int price;
    private int countItem;


    public Seller(String itemName, int price, int countItem) {
        this.itemName = itemName;
        this.price = price;
        this.countItem = countItem;
    }

    public Seller(int id, String itemName, int price, int countItem) {
        this.id = id;
        this.itemName = itemName;
        this.price = price;
        this.countItem = countItem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCountItem() {
        return countItem;
    }

    public void setCountItem(int countItem) {
        this.countItem = countItem;
    }

}
