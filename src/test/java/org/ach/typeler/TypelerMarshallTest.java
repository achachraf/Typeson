package org.ach.typeler;

import org.ach.typeler.mock.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TypelerMarshallTest {

    @Test
    public void testMarshal() {
        Typeler typeler = new Typeler();
        Shape circle = new Circle("Acircle", 10);
        String json = typeler.marshall(circle);
        assertEquals("{\"name\":\"Acircle\",\"radius\":10}", json);
    }

    @Test
    public void testMarshallKeepField() {
        Typeler typeler = new Typeler();
        KeepField keepField = new KeepField("AnObject");
        String json = typeler.marshall(keepField);
        assertEquals("{\"name\":\"AnObject\",\"type\":\"keepField\"}", json);
    }

    @Test
    public void testMarshallDefaultField() {
        Typeler typeler = new Typeler();
        Shape defaultType = new DefaultType("AnObject");
        String json = typeler.marshall(defaultType);
        String expected = String.format("{\"name\":\"AnObject\",\"%s\":\"defaultType\"}", ElementType.TYPELER_FIELD_TYPE);
        assertEquals(expected, json);
    }

    @Test
    public void testMarshallUnannotated() {
        Typeler typeler = new Typeler();
        UnannotatedMock unannotatedShape = new UnannotatedMock();
        Throwable throwable = assertThrows(TypelerException.class, () ->
                typeler.marshall(unannotatedShape));
        assertEquals("ElementType annotation not found on class: org.ach.typeler.mock.UnannotatedMock", throwable.getMessage());
    }

    @Test
    public void testMarshallList(){
        Typeler typeler = new Typeler();
        Shape circle = new Circle("Acircle", 5);
        Shape rectangle = new Rectangle("Asquare", 7, 11);
        List<Shape> shapes = List.of(circle, rectangle);
        String json = typeler.marshall(shapes);
        assertEquals("[{\"name\":\"Acircle\",\"radius\":10},{\"name\":\"Asquare\",\"side\":10}]", json);
    }


}
