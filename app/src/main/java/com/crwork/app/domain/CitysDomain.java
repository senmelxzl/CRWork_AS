package com.crwork.app.domain;

public class CitysDomain {
    private int id;
    private int parent_id;
    private String city_name_zh;
    private String city_name_en;
    private int city_level;
    private String city_code;
    private int city_status_cr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getCity_name_zh() {
        return city_name_zh;
    }

    public void setCity_name_zh(String city_name_zh) {
        this.city_name_zh = city_name_zh;
    }

    public String getCity_name_en() {
        return city_name_en;
    }

    public void setCity_name_en(String city_name_en) {
        this.city_name_en = city_name_en;
    }

    public int getCity_level() {
        return city_level;
    }

    public void setCity_level(int city_level) {
        this.city_level = city_level;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public int getCity_status_cr() {
        return city_status_cr;
    }

    public void setCity_status_cr(int city_status_cr) {
        this.city_status_cr = city_status_cr;
    }
}
