package org.ach.typeson.mock;

import org.ach.typeson.domain.ElementType;

import java.util.Objects;

@ElementType(name = "shape", field = "type")
public class Shape {

    private String name;

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

    @Override
    public String toString() {
        return "Shape{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shape shape = (Shape) o;
        return Objects.equals(name, shape.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
