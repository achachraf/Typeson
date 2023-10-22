package io.github.achachraf.typeson.interlay.mock;

import io.github.achachraf.typeson.domain.ElementType;

import java.util.Objects;

@ElementType(name = "secondCompositeMock", field = "type")
public class SecondCompositeMock {

    private int number;

    private Shape shape;

    private FirstCompositeMock firstCompositeMock;


    public SecondCompositeMock() {
    }


    public SecondCompositeMock(int number) {
        this.number = number;
    }

    public SecondCompositeMock setNumber(int number) {
        this.number = number;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public Shape getShape() {
        return shape;
    }

    public SecondCompositeMock setShape(Shape shape) {
        this.shape = shape;
        return this;
    }

    public FirstCompositeMock getFirstCompositeMock() {
        return firstCompositeMock;
    }

    public SecondCompositeMock setFirstCompositeMock(FirstCompositeMock firstCompositeMock) {
        this.firstCompositeMock = firstCompositeMock;
        return this;
    }

    @Override
    public String toString() {
        return "SecondCompositeMock{" +
                "number=" + number +
                ", shape=" + shape +
                ", firstCompositeMock=" + firstCompositeMock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecondCompositeMock that = (SecondCompositeMock) o;
        return number == that.number && Objects.equals(shape, that.shape) && Objects.equals(firstCompositeMock, that.firstCompositeMock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, shape, firstCompositeMock);
    }
}


