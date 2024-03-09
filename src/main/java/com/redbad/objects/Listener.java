package com.redbad.objects;

import org.redbad.Parser;


public interface Listener<T> {
    void run(T event, Parser parser);
}
