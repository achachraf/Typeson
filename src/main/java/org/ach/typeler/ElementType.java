package org.ach.typeler;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface ElementType {

    public String TYPELER_FIELD_TYPE = "TYPELER_FIELD_TYPE";

    String name();

    String field() default TYPELER_FIELD_TYPE;

    boolean keepField() default false;
}
