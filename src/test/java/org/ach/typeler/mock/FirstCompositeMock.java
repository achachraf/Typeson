package org.ach.typeler.mock;

public class FirstCompositeMock {

    private String name;

    private SecondCompositeMock secondCompositeMock;

    private FirstCompositeMock firstCompositeMock;



    public FirstCompositeMock() {
    }

    public FirstCompositeMock(String name) {
        this.name = name;
    }

    public FirstCompositeMock setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public SecondCompositeMock getSecondCompositeMock() {
        return secondCompositeMock;
    }

    public FirstCompositeMock setSecondCompositeMock(SecondCompositeMock secondCompositeMock) {
        this.secondCompositeMock = secondCompositeMock;
        return this;
    }

    public FirstCompositeMock getFirstCompositeMock() {
        return firstCompositeMock;
    }

    public FirstCompositeMock setFirstCompositeMock(FirstCompositeMock firstCompositeMock) {
        this.firstCompositeMock = firstCompositeMock;
        return this;
    }
}
