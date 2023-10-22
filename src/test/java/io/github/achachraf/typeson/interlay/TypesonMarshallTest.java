package io.github.achachraf.typeson.interlay;

import io.github.achachraf.typeson.domain.ElementType;
import io.github.achachraf.typeson.interlay.mock.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypesonMarshallTest {

    @Test
    public void testMarshal() {
        Typeson typeson = new Typeson();
        Shape circle = new Circle("Acircle", 10);
        String json = typeson.marshall(circle);
        assertEquals("{\"name\":\"Acircle\",\"radius\":10,\"type\":\"circle\"}", json);
    }



    @Test
    public void testMarshallDefaultField() {
        Typeson typeson = new Typeson();
        Shape defaultType = new DefaultType("AnObject");
        String json = typeson.marshall(defaultType);
        String expected = String.format("{\"name\":\"AnObject\",\"%s\":\"defaultType\"}", ElementType.TYPESON_FIELD_TYPE);
        assertEquals(expected, json);
    }

    @Test
    public void testMarshallUnannotated() {
        Typeson typeson = new Typeson();
        Shape unannotatedMock = new UnannotatedShape()
                .setAdditionalField("additionalField")
                .setName("aName");
        String json = typeson.marshall(unannotatedMock);
        String expected = "{\"name\":\"aName\",\"additionalField\":\"additionalField\"}";
        assertEquals(expected, json);
    }

    @Test
    public void testMarshallList(){
        Typeson typeson = new Typeson();
        Shape circle = new Circle("Acircle", 5);
        Shape rectangle = new Rectangle("Asquare", 7, 11);
        List<Shape> shapes = List.of(circle, rectangle);
        String json = typeson.marshall(shapes);
        assertEquals("[{\"name\":\"Acircle\",\"radius\":5,\"type\":\"circle\"},{\"name\":\"Asquare\",\"width\":7,\"height\":11,\"type\":\"rectangle\"}]", json);
    }


}
