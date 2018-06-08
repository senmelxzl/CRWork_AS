package com.crwork.app.domain;

/**
 * LitterDomain
 *
 * @author xiezhenlin
 */
public class LitterDomain {
    private int ID;
    private String userId;
    private int littertypeID;
    private Double weight;
    private Double tPrice;
    private String litterdate;

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public String getUserID() {
        return userId;
    }

    public void setUserID(String userId) {
        this.userId = userId;
    }

    public int getLittertypeID() {
        return littertypeID;
    }

    public void setLittertypeID(int littertypeID) {
        this.littertypeID = littertypeID;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double gettPrice() {
        return tPrice;
    }

    public void settPrice(Double tPrice) {
        this.tPrice = tPrice;
    }

    public String getLitterdate() {
        return litterdate;
    }

    public void setLitterdate(String litterdate) {
        this.litterdate = litterdate;
    }
}
