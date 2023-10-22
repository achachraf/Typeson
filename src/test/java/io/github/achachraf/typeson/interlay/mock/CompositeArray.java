package io.github.achachraf.typeson.interlay.mock;

public class CompositeArray {

    private Shape[] shapes;

    private String name;

    public Shape[] getShapes() {
        return shapes;
    }

    public CompositeArray setShapes(Shape[] shapes) {
        this.shapes = shapes;
        return this;
    }

    public String getName() {
        return name;
    }

    public CompositeArray setName(String name) {
        this.name = name;
        return this;
    }
}
