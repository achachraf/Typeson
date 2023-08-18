package org.ach.typeler.mock;

import org.ach.typeler.ElementType;

@ElementType(name = "defaultType", keepField = true)
public class DefaultType extends Shape {

    public DefaultType() {
    }

    public DefaultType(String name) {
        super(name);
    }
}
