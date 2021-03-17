package com.pettify.model;

public interface Model {
    interface Listener<T> {
        void onComplete(T data);
    }

    interface EmptyListener {
        void onComplete();
    }
}
