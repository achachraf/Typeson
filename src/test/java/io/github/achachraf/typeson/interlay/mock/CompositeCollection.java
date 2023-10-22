package io.github.achachraf.typeson.interlay.mock;

import java.util.ArrayList;
import java.util.List;

public class CompositeCollection {

    private FirstCompositeMock firstCompositeMock;

    private SecondCompositeMock secondCompositeMock;

    private List<FirstCompositeMock> firstCompositeMockList = new ArrayList<>();

    private List<SecondCompositeMock> secondCompositeMockList = new ArrayList<>();

    public FirstCompositeMock getFirstCompositeMock() {
        return firstCompositeMock;
    }

    public CompositeCollection setFirstCompositeMock(FirstCompositeMock firstCompositeMock) {
        this.firstCompositeMock = firstCompositeMock;
        return this;
    }

    public SecondCompositeMock getSecondCompositeMock() {
        return secondCompositeMock;
    }

    public CompositeCollection setSecondCompositeMock(SecondCompositeMock secondCompositeMock) {
        this.secondCompositeMock = secondCompositeMock;
        return this;
    }

    public List<FirstCompositeMock> getFirstCompositeMockList() {
        return firstCompositeMockList;
    }

    public CompositeCollection setFirstCompositeMockList(List<FirstCompositeMock> firstCompositeMockList) {
        this.firstCompositeMockList = firstCompositeMockList;
        return this;
    }

    public List<SecondCompositeMock> getSecondCompositeMockList() {
        return secondCompositeMockList;
    }

    public CompositeCollection setSecondCompositeMockList(List<SecondCompositeMock> secondCompositeMockList) {
        this.secondCompositeMockList = secondCompositeMockList;
        return this;
    }

    public CompositeCollection addFirstCompositeMock(FirstCompositeMock firstCompositeMock) {
        firstCompositeMockList.add(firstCompositeMock);
        return this;
    }

    public CompositeCollection addSecondCompositeMock(SecondCompositeMock secondCompositeMock) {
        secondCompositeMockList.add(secondCompositeMock);
        return this;
    }
}
