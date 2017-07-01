package com.market.application.javaclass;

/**
 * Created by Administrator on 26/3/2560.
 */
public class Order {
    int order_id;
    long order_date;
    double discount;
    double order_total;
    Status status;


    public Order(int order_id) {
        this.order_id = order_id;
    }

    public Order() {
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public long getOrder_date() {
        return order_date;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setOrder_date(long order_date) {
        this.order_date = order_date;
    }

    public double getOrder_total() {
        return order_total;
    }

    public void setOrder_total(double order_total) {
        this.order_total = order_total;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
