package org.ach.typeler.mock;

import org.ach.typeler.ElementType;

@ElementType(name = "Shape", field = "type")
public class Shape {

    String name;

    public Shape() {
    }

    public Shape(String name) {
        this.name = name;
    }

    public Shape setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }


}
