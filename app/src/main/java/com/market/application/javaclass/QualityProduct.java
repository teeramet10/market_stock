package com.market.application.javaclass;

/**
 * Created by barbie on 5/21/2017.
 */

public class QualityProduct {

    int qual;
    int idproduct;

    public QualityProduct(int qual, int idproduct) {
        this.qual = qual;
        this.idproduct = idproduct;
    }

    public int getQual() {
        return qual;
    }

    public void setQul(int qul) {
        this.qual = qual;
    }

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }
}
