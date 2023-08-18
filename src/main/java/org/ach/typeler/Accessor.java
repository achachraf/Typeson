package org.ach.typeler;

import java.lang.reflect.Method;

public class Accessor {

    private Method method;

    private ObjectInfo objectInfo;

    public Method getMethod() {
        return method;
    }

    public Accessor setMethod(Method method) {
        this.method = method;
        return this;
    }

    public ObjectInfo getObjectInfo() {
        return objectInfo;
    }

    public Accessor setObjectInfo(ObjectInfo objectInfo) {
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
