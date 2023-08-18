package org.ach.typeler;

import org.ach.typeler.mock.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TypelerUnmarshallTest {

    @Test
    public void testUnmarshall() {
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Shape circle = typeler.unmarshall(data, Shape.class);
        assertInstanceOf(Shape.class, circle);
        assertEquals("Acircle", circle.getName());
        assertEquals(10, ((Circle)circle).getRadius());

    }

    @Test
    public void testUnmarshallList(){
        Typeler typeler = new Typeler();
        String data = "[{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}]" ;
        List<? extends Shape> shapes = typeler.unmarshallList(data, Shape.class);
        assertInstanceOf(List.class, shapes);
        assertEquals(2, shapes.size());
        assertInstanceOf(Circle.class, shapes.get(0));
        assertEquals("Acircle", shapes.get(0).getName());
        assertEquals(10, ((Circle)shapes.get(0)).getRadius());
        assertInstanceOf(Rectangle.class, shapes.get(1));
        assertEquals("Arectangle", shapes.get(1).getName());
        assertEquals(10, ((Rectangle)shapes.get(1)).getWidth());
        assertEquals(20, ((Rectangle)shapes.get(1)).getHeight());

    }

    @Test
    public void testUnmarshallObjectWithList(){
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(TypelerException.class, () ->
                typeler.unmarshallList(data, Shape.class));
        assertEquals(data+" is not a list", throwable.getMessage());
    }

    @Test
    public void testUnmarshallListWithObject(){
        Typeler typeler = new Typeler();
        String data = "[{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}]" ;
        Throwable throwable = assertThrows(TypelerException.class, () ->
                typeler.unmarshall(data, Shape.class));
        assertEquals(data+" is not an object", throwable.getMessage());
    }

    @Test
    public void testUnmarshallInvalidJson(){
        Typeler typeler = new Typeler();
        String data = "invalid json" ;
        Throwable throwable = assertThrows(TypelerException.class, () ->
                typeler.unmarshall(data, Shape.class));
        assertEquals(data+" is not a valid JSON", throwable.getMessage());
    }

    @Test
    public void testUnmarshallInvalidType(){
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"invalid\",\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(TypelerException.class, () ->
                typeler.unmarshall(data, Shape.class));
        assertEquals("org.ach.typeler.TypelerException: Type not found: invalid", throwable.getMessage());
    }

    @Test
    public void testUnmarshallUnannotatedClass(){
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(TypelerException.class, () ->
                typeler.unmarshall(data, UnannotatedMock.class));
        assertEquals("org.ach.typeler.TypelerTest$UnannotatedMock is not annotated with @ElementType", throwable.getMessage());
    }

    @Test
    public void testUnmarshallUnannotatedSubClass(){
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"unannotatedShape\",\"name\":\"Acircle\"}" ;
        Throwable throwable = assertThrows(TypelerException.class, () ->
                typeler.unmarshall(data, Shape.class));
        assertEquals("org.ach.typeler.TypelerException: Type not found: unannotatedShape", throwable.getMessage());
    }

    @Test
    public void testUnmarshallNotASubClass(){
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"notASubType\",\"name\":\"Acircle\"}" ;
        Throwable throwable = assertThrows(TypelerException.class, () ->
                typeler.unmarshall(data, Shape.class));
        assertEquals("org.ach.typeler.TypelerException: Type not found: notASubType", throwable.getMessage());
    }

    @Test
    public void testUnmarshallObjectDataNull(){
        Typeler typeler = new Typeler();
        String data = null ;
        Throwable throwable = assertThrows(IllegalArgumentException.class, () ->
                typeler.unmarshall(data, Shape.class));
        assertEquals("Json String is null", throwable.getMessage());
    }

    @Test
    public void testUnmarshallObjectClassNull(){
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(IllegalArgumentException.class, () ->
                typeler.unmarshall(data, null));
        assertEquals("Class type is null", throwable.getMessage());
    }

    @Test
    public void testUnmarshallListDataNull(){
        Typeler typeler = new Typeler();
        String data = null ;
        Throwable throwable = assertThrows(IllegalArgumentException.class, () ->
                typeler.unmarshallList(data, Shape.class));
        assertEquals("Json String is null", throwable.getMessage());
    }

    @Test
    public void testUnmarshallListClassNull(){
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(IllegalArgumentException.class, () ->
                typeler.unmarshallList(data, null));
        assertEquals("Class type is null", throwable.getMessage());
    }

    @Test
    public void testUnmarshallWithoutTypeField() {
        Typeler typeler = new Typeler();
        String data = "{\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(TypelerException.class, () ->
                typeler.unmarshall(data, Shape.class));
        assertEquals("org.ach.typeler.TypelerException: Type field not found: type", throwable.getMessage());
    }

    @Test
    public void testUnmarshallSameClass(){
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Circle circle = typeler.unmarshall(data, Circle.class);
        assertInstanceOf(Circle.class, circle);
        assertEquals("Acircle", circle.getName());
        assertEquals(10, circle.getRadius());
    }

    @Test
    public void testUnmarshallDefaultField() {
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"defaultType\",\"name\":\"Acircle\"}" ;
        Throwable throwable = assertThrows(TypelerException.class, () ->
                typeler.unmarshall(data, DefaultType.class));
        assertEquals("org.ach.typeler.TypelerException: Type field not found: "+ElementType.TYPELER_FIELD_TYPE, throwable.getMessage());
    }

    @Test
    public void testUnmarshallKeepField() {
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"keepField\",\"name\":\"AnObject\"}" ;
        KeepField keepField = typeler.unmarshall(data, KeepField.class);
        assertInstanceOf(KeepField.class, keepField);
        assertEquals("Acircle", keepField.getName());
    }

    @Test
    public void testUnmarshalComposite(){
        Typeler typeler = new Typeler();
        String data = "{\"type\":\"circleAndRectangle\",\"circle\":{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},\"rectangle\":{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}}";
        CircleAndRectangle circleAndRectangle = typeler.unmarshall(data, CircleAndRectangle.class);
        assertInstanceOf(CircleAndRectangle.class, circleAndRectangle);
        assertInstanceOf(Circle.class, circleAndRectangle.getCircle());
        assertInstanceOf(Rectangle.class, circleAndRectangle.getRectangle());
        assertEquals("Acircle", circleAndRectangle.getCircle().getName());
        assertEquals(10, ((Circle)(circleAndRectangle.getCircle())).getRadius());
        assertEquals("Arectangle", circleAndRectangle.getRectangle().getName());
        assertEquals(10, ((Rectangle)(circleAndRectangle.getRectangle())).getWidth());
        assertEquals(20, ((Rectangle)(circleAndRectangle.getRectangle())).getHeight());

    }





    private static class UnannotatedShape extends Shape {
        public UnannotatedShape() {
        }

        public UnannotatedShape(String name) {
            super(name);
        }
    }

    @ElementType(name = "notASubType", field = "type")
    private static class NotASubType {
        public NotASubType() {
        }

        public NotASubType(String name) {
            this.name = name;
        }

        String name;
    }





}
