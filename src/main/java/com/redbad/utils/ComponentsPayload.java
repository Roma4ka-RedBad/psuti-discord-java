package com.redbad.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ComponentsPayload {
    public Map<String, Object> payloads;
    public ScheduledExecutorService scheduler;

    public ComponentsPayload() {
        this.payloads = new HashMap<>();
        scheduler = Executors.newSingleThreadScheduledExecutor();
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
        scheduler.schedule(() -> {
            payloads.remove(pid);
        }, 15, TimeUnit.MINUTES);
        return pid;
    }

    public Object getPayload(String pid) {
        return payloads.get(pid);
    }
}
