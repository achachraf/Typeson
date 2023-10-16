package org.ach.typeson.domain;

import java.util.List;

public class SingleObjectInfo extends ObjectInfo{

    private List<ObjectInfoAccessor> getters;

    private List<ObjectInfoAccessor> setters;

    private List<ObjectInfo> subObjects;

    @Override
    public Class<?> getType() {
        return super.getType();
    }

    @Override
    public SingleObjectInfo setType(Class<?> type) {
        super.setType(type);
        return this;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public SingleObjectInfo setName(String name) {
        super.setName(name);
        return this;
    }

    public List<ObjectInfoAccessor> getGetters() {
        return getters;
    }

    public SingleObjectInfo setGetters(List<ObjectInfoAccessor> getters) {
        this.getters = getters;
        return this;
    }

    public List<ObjectInfoAccessor> getSetters() {
        return setters;
    }

    public SingleObjectInfo setSetters(List<ObjectInfoAccessor> setters) {
        this.setters = setters;
        return this;
    }

    public List<ObjectInfo> getSubObjects() {
        return subObjects;
    }

    public SingleObjectInfo setSubObjects(List<ObjectInfo> subObjects) {
        this.subObjects = subObjects;
        return this;
    }

    public ObjectInfo getSubObject(String name) {
        return subObjects.stream()
                .filter(subObject -> name.equals(subObject.getName()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No subObject with name " + name));
    }


    @Override
    public String toString() {
        return "SingleObjectInfo{" +
                "type=" + getType() +
                ",\n getters=" + getters +
                ",\n setters=" + setters +
                ",\n\t subObjects=" + subObjects +
                '}';
    }
}
