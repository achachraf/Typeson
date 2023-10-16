package org.ach.typeson.mock;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "FirstCompositeMock{" +
                "name='" + name + '\'' +
                ", secondCompositeMock=" + secondCompositeMock +
                ", firstCompositeMock=" + firstCompositeMock +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirstCompositeMock that = (FirstCompositeMock) o;
        return Objects.equals(name, that.name) && Objects.equals(secondCompositeMock, that.secondCompositeMock) && Objects.equals(firstCompositeMock, that.firstCompositeMock);
    }

    @Override
    public int hashCode() {
        if(firstCompositeMock == this){
            return 0;
        }
        return Objects.hash(name, secondCompositeMock, firstCompositeMock);
    }
}
