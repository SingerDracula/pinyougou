package com.pinyougou.pojo;

import java.io.Serializable;

public class Brand implements Serializable {
    private int id;
    private String name;
    private String first_char;

    public Brand(int id, String name, String first_char) {
        this.id = id;
        this.name = name;
        this.first_char = first_char;
    }

    public Brand() {
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

    public String getFirst_char() {
        return first_char;
    }

    public void setFirst_char(String first_char) {
        this.first_char = first_char;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", first_char='" + first_char + '\'' +
                '}';
    }
}
