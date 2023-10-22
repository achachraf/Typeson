package com.ach.typeson.mock;

import com.ach.typeson.domain.ElementType;

@ElementType(name = "defaultType")
public class DefaultType extends Shape {

    public DefaultType() {
    }

    public DefaultType(String name) {
        super(name);
    }
}
