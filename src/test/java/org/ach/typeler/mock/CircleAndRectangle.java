package org.ach.typeler.mock;

import org.ach.typeler.ElementType;

@ElementType(name = "circleAndRectangle", field = "type")
public class CircleAndRectangle {

    String name;

    Shape circle;

    Shape rectangle;

    public CircleAndRectangle() {
    }


    public CircleAndRectangle setCircle(Shape circle) {
        this.circle = circle;
        return this;
    }

    public CircleAndRectangle setRectangle(Shape rectangle) {
        this.rectangle = rectangle;
        return this;
    }

    public Shape getCircle() {
        return circle;
    }

    public Shape getRectangle() {
        return rectangle;
    }

    public String getName() {
        return name;
    }

    public CircleAndRectangle setName(String name) {
        this.name = name;
        return this;
    }
}
