package com.ach.typeson.mock;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"diff_name", "many_aliases"})
public class MockForJsonProperty {

    private String diffName;

    private String manyAliases;

    public String getDiffName() {
        return diffName;
    }

    @JsonProperty("diff_name")
    public MockForJsonProperty setDiffName(String diffName) {
        this.diffName = diffName;
        return this;
    }

    public String getManyAliases() {
        return manyAliases;
    }

    @JsonAlias({"many_aliases", "many_aliases2"})
    public MockForJsonProperty setManyAliases(String manyAliases) {
        this.manyAliases = manyAliases;
        return this;
    }
}
