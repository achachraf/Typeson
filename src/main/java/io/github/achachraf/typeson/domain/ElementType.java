package io.github.achachraf.typeson.domain;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to specify the type of the element and field name during the serialization and deserialization process.
 * <p>
 *     The field name is optional, and it is used to specify the field name that will be used to store the type of the element.<br>
 *     If the field name is not specified, the default value is "TYPESON_FIELD_TYPE".
 *     <br>
 *     <br>
 *     Example:
 *     <br>
 *     <br>
 *     <code>
 *         {@literal @}ElementType(name = "circle", field = "type")<br>
 *         public class Circle extends Shape {<br>
 *              // ...<br>
 *         }
 *     </code>
 *</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface ElementType {

    String TYPESON_FIELD_TYPE = "TYPESON_FIELD_TYPE";

    String name();

    String field() default TYPESON_FIELD_TYPE;

}
