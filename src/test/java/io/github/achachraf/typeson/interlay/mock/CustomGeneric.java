package io.github.achachraf.typeson.interlay.mock;

public class CustomGeneric <T>{

    private T value;

    public T getValue() {
        return value;
    }

    public CustomGeneric<T> setValue(T value) {
        this.value = value;
        return this;
    }
}
