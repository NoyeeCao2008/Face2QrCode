package com.github.noyeecao2008.net;

import java.util.concurrent.ConcurrentHashMap;

public class RequestForPost extends ConcurrentHashMap<String, Object> {
    public RequestForPost param(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
