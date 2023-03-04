package com.vnu.server.model;

import com.google.gson.Gson;

import java.util.HashMap;

public class MessageConsumption {
    HashMap<Long, Integer> data = new HashMap<>();
    private String time;

    public HashMap<Long, Integer> getData() {
        return data;
    }

    public void setData(HashMap<Long, Integer> data) {
        this.data = data;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
