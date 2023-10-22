package io.github.achachraf.typeson.domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface ElementType {

    String TYPESON_FIELD_TYPE = "TYPESON_FIELD_TYPE";

    String name();

    String field() default TYPESON_FIELD_TYPE;

}
