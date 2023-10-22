package com.ach.typeson.mock;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MockForJsonIgnore {

    private String notIgnored;

    private String ignored;



    public String getNotIgnored() {
        return notIgnored;
    }

    public MockForJsonIgnore setNotIgnored(String notIgnored) {
        this.notIgnored = notIgnored;
        return this;
    }

    public String getIgnored() {
        return ignored;
    }

    @JsonIgnore
    public MockForJsonIgnore setIgnored(String ignored) {
        this.ignored = ignored;
        return this;
    }


}
