package com.ach.typeson.mock;

import com.ach.typeson.domain.ElementType;

@ElementType(name = "circle", field = "type")
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

    @Override
    public String toString() {
        return "Circle{" +
                "radius=" + radius +
                ", name='" + super.getName() + '\'' +
                '}';
    }
}
