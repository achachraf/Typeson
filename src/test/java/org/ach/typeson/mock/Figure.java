package org.ach.typeson.mock;

import java.util.ArrayList;
import java.util.List;

public class Figure {

    String name;

    List<Shape> shapes = new ArrayList<>();

    public Figure() {
    }

    public Figure(String name) {
        this.name = name;
    }

    public Figure setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public Figure addShape(Shape shape) {
        shapes.add(shape);
        return this;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public Figure setShapes(List<Shape> shapes) {
        this.shapes = shapes;
        return this;
    }
}
