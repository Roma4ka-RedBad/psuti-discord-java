package com.redbad.utils;

import java.util.HashMap;
import java.util.Map;

public class ListenerFactory {
    public Map<Object, Map<String, Object>> listeners;
    public ListenerFactory() {
        this.listeners = new HashMap<>();
    }

    public void addEvents(Object... eventTypes) {
        for (Object eventType : eventTypes) {
            this.listeners.put(eventType, new HashMap<>());
        }
    }

    public void addListener(Object eventType, String mainId, Object listener) {
        Map<String, Object> map = this.listeners.get(eventType);
        map.put(mainId, listener);
    }

    public Object getListenerStartsWith(Object eventType, String mainId) {
        Object listener = null;
        for (Map.Entry<String, Object> entry : this.listeners.get(eventType).entrySet()) {
            if (mainId.startsWith(entry.getKey()))
                listener = entry.getValue();
        }
        return listener;
    }

}
