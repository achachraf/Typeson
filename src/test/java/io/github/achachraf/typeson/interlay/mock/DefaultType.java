package io.github.achachraf.typeson.interlay.mock;

import io.github.achachraf.typeson.domain.ElementType;

@ElementType(name = "defaultType")
public class DefaultType extends Shape {

    public DefaultType() {
    }

    public DefaultType(String name) {
        super(name);
    }
}
