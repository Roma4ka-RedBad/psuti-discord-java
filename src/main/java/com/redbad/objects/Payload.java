package com.redbad.objects;

import java.util.HashMap;
import java.util.Map;


public class Payload {
    public Map<String, Object> data;

    public Payload() {
        data = new HashMap<>();
    }

    public Payload put(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Object drop(String key) {
        data.remove(key);
        return this;
    }
}
