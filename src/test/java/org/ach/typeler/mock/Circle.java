package org.ach.typeler.mock;

import org.ach.typeler.ElementType;

@ElementType(name = "Circle", field = "type")
public class Circle extends Shape {

    int radius;

    public Circle() {
    }

    public Circle(String name, int radius) {
        super(name);
        this.radius = radius;
    }

    public Circle setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    public int getRadius() {
        return radius;
    }
}
