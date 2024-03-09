package com.redbad.utils;

import java.util.HashMap;
import java.util.Map;


public class ComponentsPayload {
    public Map<String, Object> payloads;

    public ComponentsPayload() {
        this.payloads = new HashMap<>();
    }

    public boolean existsNumericPID(int npid) {
        for (String pid : payloads.keySet()) {
            if (pid.contains(String.valueOf(npid))) {
                return true;
            }
        }
        return false;
    }

    public int genNumericPID() {
        int npid = (int) (Math.random() * 1000000000);
        while (existsNumericPID(npid)) {
            npid  = (int) (Math.random() * 1000000000);
        }
        return npid;
    }

    public String addPayload(String componentId, Object payload) {
        String pid = String.format("%s:%s", componentId, genNumericPID());
        payloads.put(pid, payload);
        return pid;
    }

    public void dropPayload(String pid) {
        payloads.remove(pid);
    }

    public Object getPayload(String pid, boolean andDrop) {
        Object payload = payloads.get(pid);
        if (andDrop)
            dropPayload(pid);
        return payload;
    }
}
