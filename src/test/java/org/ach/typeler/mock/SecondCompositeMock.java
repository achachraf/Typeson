package org.ach.typeler.mock;

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
}


