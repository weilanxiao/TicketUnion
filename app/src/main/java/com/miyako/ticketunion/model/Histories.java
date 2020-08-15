package com.miyako.ticketunion.model;

import java.util.List;

public class Histories {

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Histories{" +
                "data=" + data +
                '}';
    }
}
