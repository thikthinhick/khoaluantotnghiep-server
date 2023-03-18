package com.vnu.server.model;

import com.google.gson.Gson;
import com.vnu.server.model.Detail;

import java.util.HashMap;

public class MessageConsumption {
    private String time;
    HashMap<Long, Detail> data = new HashMap<>();

    public HashMap<Long, Detail> getData() {
        return data;
    }

    public void setData(HashMap<Long, Detail> data) {
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
