package io.github.achachraf.typeson.interlay;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.achachraf.typeson.TypesonException;
import io.github.achachraf.typeson.aplication.DeserializationException;
import io.github.achachraf.typeson.domain.ElementType;
import io.github.achachraf.typeson.interlay.mock.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.junit.jupiter.api.Assertions.*;

public class TypesonUnmarshallTest {

    @Test
    public void testUnmarshall() {
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Shape circle = typeson.unmarshall(data, Shape.class);
        assertInstanceOf(Shape.class, circle);
        assertEquals("Acircle", circle.getName());
        assertEquals(10, ((Circle)circle).getRadius());

    }

    @Test
    public void testUnmarshallList(){
        Typeson typeson = new Typeson();
        String data = "[{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}]" ;
        List<? extends Shape> shapes = typeson.unmarshallList(data, Shape.class);
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
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshallList(data, Shape.class));
        assertInstanceOf(IllegalArgumentException.class, throwable.getCause());
        assertEquals("Source is not a Json Array: {\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallListWithObject(){
        Typeson typeson = new Typeson();
        String data = "[{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}]" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, Shape.class));
        assertInstanceOf(IllegalArgumentException.class, throwable.getCause());
        assertEquals("Source is not Json Object: [{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}]", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallInvalidJson(){
        Typeson typeson = new Typeson();
        String data = "invalid json" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, Shape.class));
        assertInstanceOf(DeserializationException.class, throwable.getCause());
        assertEquals("Error when processing Json Object: invalid json", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallInvalidType(){
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"invalid\",\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, Shape.class));
        assertEquals("Cannot find a sub-class of io.github.achachraf.typeson.interlay.mock.Shape annotated with @ElementType and value='invalid' in classpath", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallUnannotatedClass(){
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, io.github.achachraf.typeson.interlay.mock.UnannotatedShape.class));
        assertInstanceOf(IllegalStateException.class, throwable.getCause());
        assertEquals("Unknown properties: [type, radius] for type: class io.github.achachraf.typeson.interlay.mock.UnannotatedShape", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallUnannotatedSubClass(){
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"unannotatedShape\",\"name\":\"Acircle\"}" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, Shape.class));
        assertEquals("Cannot find a sub-class of io.github.achachraf.typeson.interlay.mock.Shape annotated with @ElementType and value='unannotatedShape' in classpath", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallNotASubClass(){
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"notASubType\",\"name\":\"Acircle\"}" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, Shape.class));
        assertEquals("Cannot find a sub-class of io.github.achachraf.typeson.interlay.mock.Shape annotated with @ElementType and value='notASubType' in classpath", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallObjectDataNull(){
        Typeson typeson = new Typeson();
        String data = null ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, Shape.class));
        assertInstanceOf(NullPointerException.class, throwable.getCause());
        assertEquals("Source cannot be null", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallObjectClassNull(){
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, null));
        assertEquals("Type cannot be null", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallListDataNull(){
        Typeson typeson = new Typeson();
        String data = null ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshallList(data, Shape.class));
        assertInstanceOf(NullPointerException.class, throwable.getCause());
        assertEquals("Source cannot be null", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallListClassNull(){
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshallList(data,(Class<? extends Object>) null));
        assertInstanceOf(NullPointerException.class, throwable.getCause());
        assertEquals("Type cannot be null", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallWithoutTypeField() {
        Typeson typeson = new Typeson();
        String data = "{\"name\":\"Acircle\",\"radius\":10}" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, Shape.class));
        assertInstanceOf(IllegalStateException.class, throwable.getCause());
        assertEquals("Unknown properties: [radius] for type: class io.github.achachraf.typeson.interlay.mock.Shape", throwable.getCause().getMessage());
    }

    @Test
    public void testUnmarshallSameClass(){
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}" ;
        Circle circle = typeson.unmarshall(data, Circle.class);
        assertInstanceOf(Circle.class, circle);
        assertEquals("Acircle", circle.getName());
        assertEquals(10, circle.getRadius());
    }

    @Test
    public void testUnmarshallDefaultField() {
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"defaultType\",\"name\":\"Acircle\"}" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, DefaultType.class));
        assertInstanceOf(IllegalStateException.class, throwable.getCause());
        assertEquals("Unknown properties: [type] for type: class io.github.achachraf.typeson.interlay.mock.DefaultType", throwable.getCause().getMessage());
    }


    @Test
    public void testUnmarshalComposite(){
        Typeson typeson = new Typeson();
        String data = "{\"type\":\"circleAndRectangle\",\"circle\":{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},\"rectangle\":{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}}";
        CircleAndRectangle circleAndRectangle = typeson.unmarshall(data, CircleAndRectangle.class);
        assertInstanceOf(CircleAndRectangle.class, circleAndRectangle);
        assertInstanceOf(Circle.class, circleAndRectangle.getCircle());
        assertInstanceOf(Rectangle.class, circleAndRectangle.getRectangle());
        assertEquals("Acircle", circleAndRectangle.getCircle().getName());
        assertEquals(10, ((Circle)(circleAndRectangle.getCircle())).getRadius());
        assertEquals("Arectangle", circleAndRectangle.getRectangle().getName());
        assertEquals(10, ((Rectangle)(circleAndRectangle.getRectangle())).getWidth());
        assertEquals(20, ((Rectangle)(circleAndRectangle.getRectangle())).getHeight());

    }


    @Test
    public void testUnmarshallListWithTypeRef(){
        Typeson typeson = new Typeson();
        String data = "[{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}]" ;
        List<Shape> shapes = typeson.unmarshallList(data, new TypeReference<>() {});
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
    public void testUnmarshallNestedListWithTypeRef(){
        Typeson typeson = new Typeson();
        String data = "[[{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}]]" ;
        List<List<Shape>> shapes = typeson.unmarshallList(data, new TypeReference<>() {});
        assertInstanceOf(List.class, shapes);
        assertEquals(1, shapes.size());
        assertInstanceOf(List.class, shapes.get(0));
        assertEquals(2, shapes.get(0).size());
        assertInstanceOf(Circle.class, shapes.get(0).get(0));
        assertEquals("Acircle", shapes.get(0).get(0).getName());
        assertEquals(10, ((Circle)shapes.get(0).get(0)).getRadius());
        assertInstanceOf(Rectangle.class, shapes.get(0).get(1));
        assertEquals("Arectangle", shapes.get(0).get(1).getName());
        assertEquals(10, ((Rectangle)shapes.get(0).get(1)).getWidth());
        assertEquals(20, ((Rectangle)shapes.get(0).get(1)).getHeight());
    }


    @Test
    public void testUnmarshallWithCustomConfig(){
        Typeson typeson = new Typeson(
                new ConfigProviderMap()
                        .setProperty(FAIL_ON_UNKNOWN_PROPERTIES, false)
        );
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10,\"color\":\"red\"}" ;
        Circle circle = typeson.unmarshall(data, Circle.class);
        assertInstanceOf(Circle.class, circle);
        assertEquals("Acircle", circle.getName());
        assertEquals(10, circle.getRadius());
    }

    @Test
    public void testUnmarshallWithCustomConfigFail(){
        Typeson typeson = new Typeson(
                new ConfigProviderMap()
                        .setProperty(FAIL_ON_UNKNOWN_PROPERTIES, true)
        );
        String data = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10,\"color\":\"red\"}" ;
        Throwable throwable = assertThrows(TypesonException.class, () ->
                typeson.unmarshall(data, Circle.class));
        assertInstanceOf(IllegalStateException.class, throwable.getCause());
        assertEquals("Unknown properties: [color] for type: class io.github.achachraf.typeson.interlay.mock.Circle", throwable.getCause().getMessage());
    }







}
