package com.pettify.model.listeners;

public interface Listener<T> {
    void onComplete(T data);
}
