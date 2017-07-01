package com.market.application.javaclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by barbie on 5/6/2017.
 */

public class SummarySale implements Parcelable {
    int id;
    String name;
    int value;
    double total;
    double cost;
    double discount;
    double profit;
    String path;

    public SummarySale() {
    }

    protected SummarySale(Parcel in) {
        name = in.readString();
        value = in.readInt();
        total = in.readDouble();
        cost = in.readDouble();
        profit = in.readDouble();
        path = in.readString();
        discount=in.readDouble();
    }

    public static final Creator<SummarySale> CREATOR = new Creator<SummarySale>() {
        @Override
        public SummarySale createFromParcel(Parcel in) {
            return new SummarySale(in);
        }

        @Override
        public SummarySale[] newArray(int size) {
            return new SummarySale[size];
        }
    };


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(value);
        parcel.writeDouble(total);
        parcel.writeDouble(cost);
        parcel.writeDouble(profit);
        parcel.writeString(path);
    }
}
