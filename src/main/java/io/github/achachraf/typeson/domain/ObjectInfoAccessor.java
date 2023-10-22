package io.github.achachraf.typeson.domain;

import java.lang.reflect.Method;

public class ObjectInfoAccessor {

    private Method method;

    private ObjectInfo objectInfo;

    public Method getMethod() {
        return method;
    }

    public ObjectInfoAccessor setMethod(Method method) {
        this.method = method;
        return this;
    }

    public ObjectInfo getObjectInfo() {
        return objectInfo;
    }

    public ObjectInfoAccessor setObjectInfo(ObjectInfo objectInfo) {
        this.objectInfo = objectInfo;
        return this;
    }

    @Override
    public String toString() {
        return "Accessor{" +
                "method=" + method +
                ", objectInfo=" + objectInfo +
                '}';
    }
}
