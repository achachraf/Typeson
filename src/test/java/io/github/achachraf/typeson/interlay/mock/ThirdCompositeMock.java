package io.github.achachraf.typeson.interlay.mock;

import io.github.achachraf.typeson.domain.ElementType;

@ElementType(name = "thirdCompositeMock", field = "type")
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

    @Override
    public ThirdCompositeMock setNumber(int number) {
        super.setNumber(number);
        return this;
    }

    @Override
    public ThirdCompositeMock setShape(Shape shape) {
        super.setShape(shape);
        return this;
    }


}
