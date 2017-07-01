package com.market.application.javaclass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

/**
 * Created by Administrator on 22/3/2560.
 */
public class Stock {
    int stockid;
    Product product;
    double product_cost;
    double product_price;
    int product_quality;
    long date_fist_in;

    public int getStockid() {
        return stockid;
    }

    public void setStockid(int stockid) {
        this.stockid = stockid;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getProduct_cost() {
        return product_cost;
    }

    public void setProduct_cost(double product_cost) {
        this.product_cost = product_cost;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public int getProduct_quality() {
        return product_quality;
    }

    public void setProduct_quality(int product_quality) {
        this.product_quality = product_quality;
    }

    public long getDate_fist_in() {
        return date_fist_in;
    }

    public void setDate_fist_in(long date_fist_in) {
        this.date_fist_in = date_fist_in;
    }

    //Asc น้อยไปมาก
    //Desc ,มากไปน้อย
    public static Comparator<Stock> sortAscStockName = new Comparator<Stock>() {
        @Override
        public int compare(Stock stock1, Stock stock2) {
            String stockname1 = stock1.getProduct().getName().toUpperCase();
            String stockname2 = stock2.getProduct().getName().toUpperCase();
            //ascending order
            return stockname1.compareTo(stockname2);
        }

    };


    public static Comparator<Stock> sortDescStockPrice = new Comparator<Stock>() {
        @Override
        public int compare(Stock stock1, Stock stock2) {
            return Double.compare(stock2.getProduct_price(), stock1.getProduct_price());

        }
    };

    public static Comparator<Stock> sortDescStockQuality = new Comparator<Stock>() {
        @Override
        public int compare(Stock stock1, Stock stock2) {
            return stock2.getProduct_quality() - stock1.getProduct_quality();

        }
    };

    public static Comparator<Stock> sortDescStockName = new Comparator<Stock>() {
        @Override
        public int compare(Stock stock1, Stock stock2) {
            String stockname1 = stock1.getProduct().getName().toUpperCase();
            String stockname2 = stock2.getProduct().getName().toUpperCase();
            //ascending order
            return stockname2.compareTo(stockname1);
        }

    };


    public static Comparator<Stock> sortAscStockPrice = new Comparator<Stock>() {
        @Override
        public int compare(Stock stock1, Stock stock2) {
            return Double.compare(stock1.getProduct_price(), stock2.getProduct_price());

        }
    };

    public static Comparator<Stock> sortAscStockQuality = new Comparator<Stock>() {
        @Override
        public int compare(Stock stock1, Stock stock2) {
            return stock1.getProduct_quality() - stock2.getProduct_quality();

        }
    };
}
