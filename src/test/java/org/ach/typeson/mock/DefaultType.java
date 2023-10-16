package org.ach.typeson.mock;

import org.ach.typeson.domain.ElementType;

@ElementType(name = "defaultType")
public class DefaultType extends Shape {

    public DefaultType() {
    }

    public DefaultType(String name) {
        super(name);
    }
}
