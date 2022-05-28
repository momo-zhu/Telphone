package cn.zicoo.ir2teledemo.model;

public class OrderItemModel {
    public String name;
    public String subtitle;
    public  double price;

    public OrderItemModel(String name, String subtitle, double price) {
        this.name = name;
        this.subtitle = subtitle;
        this.price = price;
    }
}
