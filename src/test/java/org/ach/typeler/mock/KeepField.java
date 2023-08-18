package org.ach.typeler.mock;

import org.ach.typeler.ElementType;

@ElementType(name = "keepField", field="type", keepField = true)

public class KeepField {

    String name;

    public KeepField(String name) {
        this.name = name;
    }

    public KeepField() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;

    }
}
