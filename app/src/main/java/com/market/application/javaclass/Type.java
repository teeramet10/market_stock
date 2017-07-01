package com.market.application.javaclass;

/**
 * Created by Administrator on 22/3/2560.
 */
public class Type {
    int id;
    String name;
    Disable disable;

    public Type() {
    }

    public Type(int id) {
        this.id = id;
    }

    public Type(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Disable getDisable() {
        return disable;
    }

    public void setDisable(Disable disable) {
        this.disable = disable;
    }
}
