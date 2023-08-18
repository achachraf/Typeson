package org.ach.typeler.mock;

public class ThirdCompositeMock extends SecondCompositeMock {

    private String name;

    private SecondCompositeMock secondCompositeMock;

    public String getName() {
        return name;
    }

    public ThirdCompositeMock setName(String name) {
        this.name = name;
        return this;
    }

    public SecondCompositeMock getSecondCompositeMock() {
        return secondCompositeMock;
    }

    public ThirdCompositeMock setSecondCompositeMock(SecondCompositeMock secondCompositeMock) {
        this.secondCompositeMock = secondCompositeMock;
        return this;
    }
}
