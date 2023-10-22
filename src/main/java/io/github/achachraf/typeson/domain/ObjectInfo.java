package io.github.achachraf.typeson.domain;

public abstract class ObjectInfo {

    private String name;

    private Class<?> type;

    private int index=-1;


    public Class<?> getType() {
        return type;
    }

    public ObjectInfo setType(Class<?> type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public ObjectInfo setName(String name) {
        this.name = name;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public ObjectInfo setIndex(int index) {
        this.index = index;
        return this;
    }
}
