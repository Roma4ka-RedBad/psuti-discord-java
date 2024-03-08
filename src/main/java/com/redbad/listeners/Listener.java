package com.redbad.listeners;

import org.redbad.Parser;

public interface Listener<T> {
    public void run(T event, Parser parser);
}
