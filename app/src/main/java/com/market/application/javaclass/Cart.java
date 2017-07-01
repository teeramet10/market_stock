package com.market.application.javaclass;

/**
 * Created by Administrator on 26/3/2560.
 */
public class Cart {
    int cart_id;
    Order order;
    DetailCart detailCart;

    public DetailCart getDetailCart() {
        return detailCart;
    }

    public void setDetailCart(DetailCart detailCart) {
        this.detailCart = detailCart;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }



    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }


}
