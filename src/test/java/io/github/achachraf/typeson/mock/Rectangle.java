package io.github.achachraf.typeson.mock;

import io.github.achachraf.typeson.domain.ElementType;

@ElementType(name = "rectangle", field = "type")
public class Rectangle extends Shape{
    int width;
    int height;

    public Rectangle() {
    }

    public Rectangle(String name, int width, int height) {
        super(name);
        this.width = width;
        this.height = height;
    }

    public Rectangle setWidth(int width) {
        this.width = width;
        return this;
    }

    public Rectangle setHeight(int height) {
        this.height = height;
        return this;
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "width=" + width +
                ", height=" + height +
                ", name='" + getName() + '\'' +
                '}';
    }
}
