package io.github.achachraf.typeson.domain;

import java.util.List;

public class ListObjectInfo extends ObjectInfo {

    private List<ObjectInfo> objectList;

    private Class<?> containerClass;


    @Override
    public Class<?> getType() {
        return super.getType();
    }

    @Override
    public ListObjectInfo setType(Class<?> type) {
        super.setType(type);
        return this;
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public ListObjectInfo setName(String name) {
        super.setName(name);
        return this;
    }

    public List<ObjectInfo> getObjectList() {
        return objectList;
    }

    public ListObjectInfo setObjectList(List<ObjectInfo> objectList) {
        this.objectList = objectList;
        return this;
    }

    public Class<?> getContainerClass() {
        return containerClass;
    }

    public ListObjectInfo setContainerClass(Class<?> containerClass) {
        this.containerClass = containerClass;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectInfo{" +
                "type=" + getType() +
                "objectList=" + objectList +
                ", containerClass=" + containerClass +
                '}';
    }
}
