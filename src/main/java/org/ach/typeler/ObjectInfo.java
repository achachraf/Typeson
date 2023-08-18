package org.ach.typeler;

public abstract class ObjectInfo {

    private String name;

    private Class<?> type;


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
}
