package com.market.application.javaclass;

/**
 * Created by Administrator on 22/3/2560.
 */
public class Product {

    int id;
    String name;
    String pathimage;
    Type type;
    Disable disable;

    public Product() {
    }

    public Product(int id) {
        this.id = id;
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

    public String getPathimage() {
        return pathimage;
    }

    public void setPathimage(String pathimage) {
        this.pathimage = pathimage;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Disable getDisable() {
        return disable;
    }

    public void setDisable(Disable disable) {
        this.disable = disable;
    }
}
