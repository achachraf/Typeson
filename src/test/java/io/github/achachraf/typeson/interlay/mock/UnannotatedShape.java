package io.github.achachraf.typeson.interlay.mock;

public class UnannotatedShape extends Shape{

    private String additionalField;

    public String getAdditionalField() {
        return additionalField;
    }

    public UnannotatedShape setAdditionalField(String additionalField) {
        this.additionalField = additionalField;
        return this;
    }

    @Override
    public UnannotatedShape setName(String name) {
        super.setName(name);
        return this;
    }
}
