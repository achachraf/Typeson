package io.github.achachraf.typeson.interlay.mock;

import io.github.achachraf.typeson.domain.ElementType;

import java.util.List;

@ElementType(name = "art", field = "type")
public class Art implements Draw<Shape>{

    private List<Shape> shapes = List.of(new Shape());

    public List<Shape> getShapes() {
        return shapes;
    }

    public Art setShapes(List<Shape> shapes) {
        this.shapes = shapes;
        return this;
    }

    @Override
    public Shape getDrawing() {
        return shapes.get(0);
    }

    @Override
    public String toString() {
        return "Art{" +
                "shape=" + shapes +
                '}';
    }
}
