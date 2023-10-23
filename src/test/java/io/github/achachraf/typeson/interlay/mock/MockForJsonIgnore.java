package io.github.achachraf.typeson.interlay.mock;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MockForJsonIgnore {

    private String notIgnored;

    private String ignored;

    private Shape ignoredShape;



    public String getNotIgnored() {
        return notIgnored;
    }

    public MockForJsonIgnore setNotIgnored(String notIgnored) {
        this.notIgnored = notIgnored;
        return this;
    }
    @JsonIgnore
    public String getIgnored() {
        return ignored;
    }


    public MockForJsonIgnore setIgnored(String ignored) {
        this.ignored = ignored;
        return this;
    }

    @JsonIgnore
    public Shape getIgnoredShape() {
        return ignoredShape;
    }

    public MockForJsonIgnore setIgnoredShape(Shape ignoredShape) {
        this.ignoredShape = ignoredShape;
        return this;
    }
}
