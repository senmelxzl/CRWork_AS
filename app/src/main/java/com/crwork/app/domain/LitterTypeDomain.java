package com.crwork.app.domain;

/**
 * litter type data
 *
 * @author xiezhenlin
 */
public class LitterTypeDomain {
    private int ID;
    private int littertypeID;
    private String typeName;
    private int typemark;
    private Double price;

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getLittertypeID() {
        return littertypeID;
    }

    public void setLittertypeID(int littertypeID) {
        this.littertypeID = littertypeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypemark() {
        return typemark;
    }

    public void setTypemark(int typemark) {
        this.typemark = typemark;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
