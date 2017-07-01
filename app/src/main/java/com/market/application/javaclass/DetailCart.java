package com.market.application.javaclass;

/**
 * Created by Administrator on 26/3/2560.
 */
public class DetailCart {
    int detail_cart_id;
    Stock stock;
    int detail_cart_quality;
    double detail_cart_price;
    double detail_cart_total;
    double detail_cart_cost;

    public int getDetail_cart_id() {
        return detail_cart_id;
    }

    public void setDetail_cart_id(int detail_cart_id) {
        this.detail_cart_id = detail_cart_id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getDetail_cart_quality() {
        return detail_cart_quality;
    }

    public void setDetail_cart_quality(int detail_cart_quality) {
        this.detail_cart_quality = detail_cart_quality;
    }

    public double getDetail_cart_price() {
        return detail_cart_price;
    }

    public void setDetail_cart_price(double detail_cart_price) {
        this.detail_cart_price = detail_cart_price;
    }

    public double getDetail_cart_cost() {
        return detail_cart_cost;
    }

    public void setDetail_cart_cost(double detail_cart_cost) {
        this.detail_cart_cost = detail_cart_cost;
    }

    public double getDetail_cart_total() {
        return detail_cart_total;
    }

    public void setDetail_cart_total(double detail_cart_total) {
        this.detail_cart_total = detail_cart_total;
    }

}


