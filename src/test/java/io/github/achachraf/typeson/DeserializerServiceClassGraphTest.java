package io.github.achachraf.typeson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.achachraf.typeson.interlay.ConfigProviderMapImpl;
import io.github.achachraf.typeson.interlay.DeserializerServiceClassGraph;
import io.github.achachraf.typeson.mock.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class DeserializerServiceClassGraphTest {


    private final DeserializerServiceClassGraph deserializerService = new DeserializerServiceClassGraph(new ConfigProviderMapImpl());

    @Test
    public void testDeserialize(){
        String json = "{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}";
        Shape shape = deserializerService.deserialize(json, Shape.class);
        assertInstanceOf(Circle.class, shape);
        Circle circle = (Circle) shape;
        assertEquals(circle.getName(), "Acircle");
        assertEquals(circle.getRadius(), 10);


    }

    @Test
    public void testDeserializeList(){
        String json = "[{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}]" ;
        List<Shape> shapes = deserializerService.deserializeArray(json, Shape.class);
        assertEquals(shapes.size(), 2);
        assertInstanceOf(Circle.class, shapes.get(0));
        Circle circle = (Circle) shapes.get(0);
        assertEquals(circle.getName(), "Acircle");
        assertEquals(circle.getRadius(), 10);

        assertInstanceOf(Rectangle.class, shapes.get(1));
        Rectangle rectangle = (Rectangle) shapes.get(1);
        assertEquals(rectangle.getName(), "Arectangle");
        assertEquals(rectangle.getWidth(), 10);
        assertEquals(rectangle.getHeight(), 20);

    }

    @Test
    public void testDeserializeListToSet(){
        String json = "[{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}]" ;
        HashSet<Shape> shapes = deserializerService.deserializeArray(json, new TypeReference<>() {});
        assertEquals(shapes.size(), 2);
        Circle circle = (Circle) shapes.stream().filter(s -> s instanceof Circle).findFirst().orElseThrow();
        assertEquals(circle.getName(), "Acircle");
        assertEquals(circle.getRadius(), 10);

        Rectangle rectangle = (Rectangle) shapes.stream().filter(s -> s instanceof Rectangle).findFirst().orElseThrow();
        assertEquals(rectangle.getName(), "Arectangle");
        assertEquals(rectangle.getWidth(), 10);
        assertEquals(rectangle.getHeight(), 20);

    }

    @Test
    public void testDeserializeListToCustomCollection(){
        String json = "[{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}]" ;
        CustomCollection<Shape> shapes = deserializerService.deserializeArray(json, new TypeReference<>() {});

        assertEquals(shapes.size(), 2);
        Circle circle = (Circle) shapes.stream().filter(s -> s instanceof Circle).findFirst().orElseThrow();
        assertEquals(circle.getName(), "Acircle");
        assertEquals(circle.getRadius(), 10);

        Rectangle rectangle = (Rectangle) shapes.stream().filter(s -> s instanceof Rectangle).findFirst().orElseThrow();
        assertEquals(rectangle.getName(), "Arectangle");
        assertEquals(rectangle.getWidth(), 10);
        assertEquals(rectangle.getHeight(), 20);

    }

    @Test
    public void testDeserializeNestObject(){
        String json = "{\"name\":\"AcircleAndRectangle\",\"circle\":{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},\"rectangle\":{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}}" ;
        CircleAndRectangle circleAndRectangle = deserializerService.deserialize(json, CircleAndRectangle.class);

        assertEquals(circleAndRectangle.getName(), "AcircleAndRectangle");
        assertInstanceOf(Circle.class, circleAndRectangle.getCircle());
        Circle circle = (Circle) circleAndRectangle.getCircle();
        assertEquals(circle.getName(), "Acircle");
        assertEquals(circle.getRadius(), 10);

        assertInstanceOf(Rectangle.class, circleAndRectangle.getRectangle());
        Rectangle rectangle = (Rectangle) circleAndRectangle.getRectangle();
        assertEquals(rectangle.getName(), "Arectangle");
        assertEquals(rectangle.getWidth(), 10);
        assertEquals(rectangle.getHeight(), 20);

    }

    @Test
    public void testDeserializeNestedObjectWithNull(){
        String json = "{\"name\":\"AcircleAndRectangle\",\"circle\":{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},\"rectangle\":null}" ;
        CircleAndRectangle circleAndRectangle = deserializerService.deserialize(json, CircleAndRectangle.class);

        assertEquals(circleAndRectangle.getName(), "AcircleAndRectangle");
        assertInstanceOf(Circle.class, circleAndRectangle.getCircle());
        Circle circle = (Circle) circleAndRectangle.getCircle();
        assertEquals(circle.getName(), "Acircle");
        assertEquals(circle.getRadius(), 10);

        assertNull(circleAndRectangle.getRectangle());

    }

    @Test
    public void testDeserializeNestedObjectMissingField(){
        String json = "{\"name\":\"AcircleAndRectangle\",\"circle\":{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10}}" ;
        CircleAndRectangle circleAndRectangle = deserializerService.deserialize(json, CircleAndRectangle.class);

        assertEquals(circleAndRectangle.getName(), "AcircleAndRectangle");
        assertInstanceOf(Circle.class, circleAndRectangle.getCircle());
        Circle circle = (Circle) circleAndRectangle.getCircle();
        assertEquals(circle.getName(), "Acircle");
        assertEquals(circle.getRadius(), 10);

        assertNull(circleAndRectangle.getRectangle());

    }

    @Test
    public void testDeserializeListInObject(){
        String json = "{\"name\":\"Afigure\",\"shapes\":[{\"type\":\"circle\",\"name\":\"Acircle\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"Arectangle\",\"width\":10,\"height\":20}, {\"type\":\"shape\",\"name\":\"Ashape\"}]}";
        Figure figure = deserializerService.deserialize(json, Figure.class);
        assertEquals(figure.getName(), "Afigure");
        assertEquals(figure.getShapes().size(), 3);
        assertInstanceOf(Circle.class, figure.getShapes().get(0));
        Circle circle = (Circle) figure.getShapes().get(0);
        assertEquals(circle.getName(), "Acircle");
        assertEquals(circle.getRadius(), 10);

        assertInstanceOf(Rectangle.class, figure.getShapes().get(1));
        Rectangle rectangle = (Rectangle) figure.getShapes().get(1);
        assertEquals(rectangle.getName(), "Arectangle");
        assertEquals(rectangle.getWidth(), 10);
        assertEquals(rectangle.getHeight(), 20);

        assertInstanceOf(Shape.class, figure.getShapes().get(2));
        Shape shape = figure.getShapes().get(2);
        assertEquals(shape.getName(), "Ashape");
    }

    @Test
    public void testDeserializeListInObjectNoType(){
        DeserializerServiceClassGraph deserializerService = new DeserializerServiceClassGraph(
                new ConfigProviderMapImpl()
                        .addProperty(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        );
        String json = "{\"name\":\"Afigure\",\"shapes\":[{\"name\":\"Acircle\",\"radius\":10},{\"name\":\"Arectangle\",\"width\":10,\"height\":20}, {\"name\":\"Ashape\"}]}";
        Figure figure = deserializerService.deserialize(json, Figure.class);
        figure.getShapes().forEach(shape -> assertInstanceOf(Shape.class, shape));
        assertEquals(figure.getName(), "Afigure");
        assertEquals(figure.getShapes().size(), 3);
        assertEquals(figure.getShapes().get(0).getName(), "Acircle");
        assertEquals(figure.getShapes().get(1).getName(), "Arectangle");
        assertEquals(figure.getShapes().get(2).getName(), "Ashape");
    }

    @Test
    public void testDeserializeCompositeObject(){
        String json = "{\"name\":\"firstCompositeMock1\",\"secondCompositeMock\":{\"number\":10,\"shape\":{\"type\":\"circle\",\"name\":\"circle\",\"radius\":10},\"firstCompositeMock\":{\"name\":\"firstCompositeMock1.2.1\",\"secondCompositeMock\":{\"number\":20,\"shape\":{\"type\":\"rectangle\",\"name\":\"rectangle\",\"width\":10,\"height\":20},\"firstCompositeMock\":{\"name\":\"firstCompositeMock1.2.1.3\",\"secondCompositeMock\":{\"number\":30,\"shape\":{\"type\":\"circle\",\"name\":\"circle\",\"radius\":30},\"firstCompositeMock\":null},\"firstCompositeMock\":null}},\"firstCompositeMock\":null}},\"firstCompositeMock\":null}\n";
        FirstCompositeMock firstCompositeMock = deserializerService.deserialize(json, FirstCompositeMock.class);
        assertNull(firstCompositeMock.getFirstCompositeMock());
        assertEquals(firstCompositeMock.getName(), "firstCompositeMock1");
        assertNotNull(firstCompositeMock.getSecondCompositeMock());
        assertEquals(firstCompositeMock.getSecondCompositeMock().getNumber(), 10);
        assertNotNull(firstCompositeMock.getSecondCompositeMock().getShape());
        assertInstanceOf(Circle.class, firstCompositeMock.getSecondCompositeMock().getShape());
        Circle circle = (Circle) firstCompositeMock.getSecondCompositeMock().getShape();
        assertEquals( 10,circle.getRadius());
        assertEquals("circle", circle.getName());
        assertNotNull(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock());
        assertEquals(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getName(), "firstCompositeMock1.2.1");
        assertNull(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getFirstCompositeMock());
        assertNotNull(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock());
        assertEquals(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getNumber(), 20);
        assertNotNull(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getShape());
        assertInstanceOf(Rectangle.class, firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getShape());
        Rectangle rectangle = (Rectangle) firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getShape();
        assertEquals(rectangle.getName(), "rectangle");
        assertEquals(rectangle.getWidth(), 10);
        assertEquals(rectangle.getHeight(), 20);
        assertNotNull(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock());
        assertEquals(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock().getName(), "firstCompositeMock1.2.1.3");
        assertNotNull(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock());
        assertEquals(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getNumber(), 30);
        assertNotNull(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getShape());
        assertInstanceOf(Circle.class, firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getShape());
        Circle circle2 = (Circle) firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getShape();
        assertEquals(circle2.getName(), "circle");
        assertEquals(circle2.getRadius(), 30);
        assertNull(firstCompositeMock.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock());

    }

    @Test
    public void testDeserializeListCompositeObjectPolymorphism() {
        String json = "[{\"firstCompositeMock\":{\"name\":\"AfirstCompositeMock1\",\"secondCompositeMock\":{\"number\":10,\"shape\":null,\"firstCompositeMock\":{\"name\":\"AfirstCompositeMock1.1.2\",\"secondCompositeMock\":null,\"firstCompositeMock\":null}},\"firstCompositeMock\":null},\"secondCompositeMock\":{\"number\":20,\"shape\":null,\"firstCompositeMock\":{\"name\":\"AfirstCompositeMock1.2.1\",\"secondCompositeMock\":null,\"firstCompositeMock\":null}},\"firstCompositeMockList\":[{\"name\":\"AfirstCompositeMockList1\",\"secondCompositeMock\":null,\"firstCompositeMock\":null}],\"secondCompositeMockList\":[{\"number\":30,\"shape\":null,\"firstCompositeMock\":null},{\"type\":\"thirdCompositeMock\",\"number\":40,\"shape\":{\"name\":\"shape1\"},\"firstCompositeMock\":null,\"name\":\"AthirdCompositeMock1\",\"secondCompositeMock\":{\"number\":50,\"shape\":null,\"firstCompositeMock\":{\"name\":\"AfirstCompositeMock1.2.2\",\"secondCompositeMock\":null,\"firstCompositeMock\":null}}}]},{\"firstCompositeMock\":null,\"secondCompositeMock\":null,\"firstCompositeMockList\":[{\"name\":\"AfirstCompositeMock2\",\"secondCompositeMock\":{\"type\":\"thirdCompositeMock\",\"number\":60,\"shape\":{\"name\":\"shape2\"},\"firstCompositeMock\":null,\"name\":\"AthirdCompositeMock2\",\"secondCompositeMock\":null},\"firstCompositeMock\":null}],\"secondCompositeMockList\":[{\"number\":70,\"shape\":null,\"firstCompositeMock\":{\"name\":\"AfirstCompositeMock2.2.1\",\"secondCompositeMock\":null,\"firstCompositeMock\":null}}]}]";
        List<? extends CompositeCollection> compositeCollections = deserializerService.deserializeArray(json, CompositeCollection.class);
        assertEquals(compositeCollections.size(), 2);
        CompositeCollection compositeCollection1 = compositeCollections.get(0);
        assertNotNull(compositeCollection1.getFirstCompositeMock());
        assertEquals(compositeCollection1.getFirstCompositeMock().getName(), "AfirstCompositeMock1");
        assertNull(compositeCollection1.getFirstCompositeMock().getSecondCompositeMock().getShape());
        assertEquals(compositeCollection1.getFirstCompositeMock().getSecondCompositeMock().getNumber(), 10);
        assertEquals(compositeCollection1.getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock().getName(), "AfirstCompositeMock1.1.2");
        assertNull(compositeCollection1.getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock());
        assertNull(compositeCollection1.getFirstCompositeMock().getSecondCompositeMock().getFirstCompositeMock().getFirstCompositeMock());
        assertNotNull(compositeCollection1.getSecondCompositeMock());
        assertEquals(compositeCollection1.getSecondCompositeMock().getNumber(), 20);
        assertNull(compositeCollection1.getSecondCompositeMock().getShape());
        assertEquals(compositeCollection1.getSecondCompositeMock().getFirstCompositeMock().getName(), "AfirstCompositeMock1.2.1");
        assertNull(compositeCollection1.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock());
        assertNull(compositeCollection1.getSecondCompositeMock().getFirstCompositeMock().getFirstCompositeMock());
        assertEquals(compositeCollection1.getFirstCompositeMockList().size(), 1);
        assertEquals(compositeCollection1.getFirstCompositeMockList().get(0).getName(), "AfirstCompositeMockList1");
        assertNull(compositeCollection1.getFirstCompositeMockList().get(0).getSecondCompositeMock());
        assertNull(compositeCollection1.getFirstCompositeMockList().get(0).getFirstCompositeMock());
        assertEquals(compositeCollection1.getSecondCompositeMockList().size(), 2);
        assertEquals(compositeCollection1.getSecondCompositeMockList().get(0).getNumber(), 30);
        assertNull(compositeCollection1.getSecondCompositeMockList().get(0).getShape());
        assertNull(compositeCollection1.getSecondCompositeMockList().get(0).getFirstCompositeMock());
        assertEquals(compositeCollection1.getSecondCompositeMockList().get(1).getNumber(), 40);
        assertNotNull(compositeCollection1.getSecondCompositeMockList().get(1).getShape());
        assertEquals(compositeCollection1.getSecondCompositeMockList().get(1).getShape().getName(), "shape1");
        assertNull(compositeCollection1.getSecondCompositeMockList().get(1).getFirstCompositeMock());
        assertInstanceOf(ThirdCompositeMock.class, compositeCollection1.getSecondCompositeMockList().get(1));
        ThirdCompositeMock thirdCompositeMock1 = (ThirdCompositeMock) compositeCollection1.getSecondCompositeMockList().get(1);
        assertEquals(thirdCompositeMock1.getName(), "AthirdCompositeMock1");
        assertNotNull(thirdCompositeMock1.getSecondCompositeMock());
        assertEquals(thirdCompositeMock1.getSecondCompositeMock().getNumber(), 50);
        assertNull(thirdCompositeMock1.getSecondCompositeMock().getShape());
        assertEquals(thirdCompositeMock1.getSecondCompositeMock().getFirstCompositeMock().getName(), "AfirstCompositeMock1.2.2");
        assertNull(thirdCompositeMock1.getSecondCompositeMock().getFirstCompositeMock().getSecondCompositeMock());
        assertNull(thirdCompositeMock1.getSecondCompositeMock().getFirstCompositeMock().getFirstCompositeMock());
        CompositeCollection compositeCollection2 = compositeCollections.get(1);
        assertNull(compositeCollection2.getFirstCompositeMock());
        assertNull(compositeCollection2.getSecondCompositeMock());
        assertEquals(compositeCollection2.getFirstCompositeMockList().size(), 1);
        assertEquals(compositeCollection2.getFirstCompositeMockList().get(0).getName(), "AfirstCompositeMock2");
        assertNotNull(compositeCollection2.getFirstCompositeMockList().get(0).getSecondCompositeMock());
        assertEquals(compositeCollection2.getFirstCompositeMockList().get(0).getSecondCompositeMock().getNumber(), 60);
        assertNotNull(compositeCollection2.getFirstCompositeMockList().get(0).getSecondCompositeMock().getShape());
        assertEquals(compositeCollection2.getFirstCompositeMockList().get(0).getSecondCompositeMock().getShape().getName(), "shape2");
        assertNull(compositeCollection2.getFirstCompositeMockList().get(0).getSecondCompositeMock().getFirstCompositeMock());
        assertInstanceOf(ThirdCompositeMock.class, compositeCollection2.getFirstCompositeMockList().get(0).getSecondCompositeMock());
        ThirdCompositeMock thirdCompositeMock2 = (ThirdCompositeMock) compositeCollection2.getFirstCompositeMockList().get(0).getSecondCompositeMock();
        assertEquals(thirdCompositeMock2.getName(), "AthirdCompositeMock2");
        assertNull(thirdCompositeMock2.getSecondCompositeMock());
        assertNull(thirdCompositeMock2.getFirstCompositeMock());
        assertEquals(thirdCompositeMock2.getNumber(), 60);
        assertNotNull(thirdCompositeMock2.getShape());
        assertEquals(thirdCompositeMock2.getShape().getName(), "shape2");
        assertEquals(compositeCollection2.getSecondCompositeMockList().size(), 1);
        assertEquals(compositeCollection2.getSecondCompositeMockList().get(0).getNumber(), 70);
        assertNull(compositeCollection2.getSecondCompositeMockList().get(0).getShape());
        assertNotNull(compositeCollection2.getSecondCompositeMockList().get(0).getFirstCompositeMock());
        assertEquals(compositeCollection2.getSecondCompositeMockList().get(0).getFirstCompositeMock().getName(), "AfirstCompositeMock2.2.1");
        assertNull(compositeCollection2.getSecondCompositeMockList().get(0).getFirstCompositeMock().getSecondCompositeMock());
        assertNull(compositeCollection2.getSecondCompositeMockList().get(0).getFirstCompositeMock().getFirstCompositeMock());


    }

    @Test
    public void testDeserializeCompositeArray(){
        String json = "{\"shapes\":[{\"name\":\"shape1\"},{\"type\":\"circle\",\"name\":\"circle1\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"rectangle1\",\"width\":10,\"height\":20}],\"name\":\"AcompositeArray\"}";
        CompositeArray compositeArray = deserializerService.deserialize(json, CompositeArray.class);
        assertNotNull(compositeArray);
        assertEquals(compositeArray.getName(), "AcompositeArray");
        assertNotNull(compositeArray.getShapes());
        assertEquals(compositeArray.getShapes().length, 3);
        assertEquals(compositeArray.getShapes()[0].getName(), "shape1");
        assertInstanceOf(Circle.class, compositeArray.getShapes()[1]);
        Circle circle = (Circle) compositeArray.getShapes()[1];
        assertEquals(circle.getName(), "circle1");
        assertEquals(circle.getRadius(), 10);
        assertInstanceOf(Rectangle.class, compositeArray.getShapes()[2]);
        Rectangle rectangle = (Rectangle) compositeArray.getShapes()[2];
        assertEquals(rectangle.getName(), "rectangle1");
        assertEquals(rectangle.getWidth(), 10);
        assertEquals(rectangle.getHeight(), 20);

    }

    @Test
    public void testDeserializeNestedList(){
        String json = "{\"listListShapes\":[[{\"name\":\"shape11\"},{\"type\":\"circle\",\"name\":\"circle12\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"rectangle13\",\"width\":10,\"height\":20}],[{\"name\":\"shape21\"},{\"type\":\"rectangle\",\"name\":\"rectangle22\",\"width\":10,\"height\":20}],[{\"name\":\"shape31\"},{\"type\":\"circle\",\"name\":\"circle32\",\"radius\":10}]],\"listArrayShapes\":null,\"allShapes\":null}";
        NestedList nestedList = deserializerService.deserialize(json, NestedList.class);
        assertNotNull(nestedList);
        assertNotNull(nestedList.getListListShapes());
        assertInstanceOf(ArrayList.class, nestedList.getListListShapes());
        assertEquals(nestedList.getListListShapes().size(), 3);
        List<Shape> firstRow = nestedList.getListListShapes().get(0);
        assertEquals(firstRow.size(), 3);
        assertEquals(firstRow.get(0).getName(), "shape11");
        assertInstanceOf(Circle.class, firstRow.get(1));
        Circle circle = (Circle) firstRow.get(1);
        assertEquals(circle.getName(), "circle12");
        assertEquals(circle.getRadius(), 10);
        assertInstanceOf(Rectangle.class, firstRow.get(2));
        Rectangle rectangle = (Rectangle) firstRow.get(2);
        assertEquals(rectangle.getName(), "rectangle13");
        List<Shape> secondRow = nestedList.getListListShapes().get(1);
        assertEquals(secondRow.size(), 2);
        assertEquals(secondRow.get(0).getName(), "shape21");
        assertInstanceOf(Rectangle.class, secondRow.get(1));
        Rectangle rectangle1 = (Rectangle) secondRow.get(1);
        assertEquals(rectangle1.getName(), "rectangle22");
        List<Shape> thirdRow = nestedList.getListListShapes().get(2);
        assertEquals(thirdRow.size(), 2);
        assertEquals(thirdRow.get(0).getName(), "shape31");
        assertInstanceOf(Circle.class, thirdRow.get(1));
        Circle circle1 = (Circle) thirdRow.get(1);
        assertEquals(circle1.getName(), "circle32");
        assertEquals(circle1.getRadius(), 10);
        assertNull(nestedList.getAllShapes());
        assertNull(nestedList.getListArrayShapes());


    }

    @Test
    public void testDeserializeNestedArrayList(){
        String json = "{\"listListShapes\":null,\"listArrayShapes\":[[{\"name\":\"shape11\"},{\"type\":\"circle\",\"name\":\"circle12\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"rectangle13\",\"width\":10,\"height\":20}],[{\"name\":\"shape21\"},{\"type\":\"rectangle\",\"name\":\"rectangle22\",\"width\":10,\"height\":20}],[{\"name\":\"shape31\"},{\"type\":\"circle\",\"name\":\"circle32\",\"radius\":10}]],\"allShapes\":null}";
        NestedList nestedList = deserializerService.deserialize(json, NestedList.class);
        assertNotNull(nestedList);
        assertNotNull(nestedList.getListArrayShapes());
        assertInstanceOf(ArrayList.class, nestedList.getListArrayShapes());
        assertEquals(nestedList.getListArrayShapes().size(), 3);
        Shape[] firstRow = nestedList.getListArrayShapes().get(0);
        assertEquals(firstRow.length, 3);
        assertEquals(firstRow[0].getName(), "shape11");
        assertInstanceOf(Circle.class, firstRow[1]);
        Circle circle = (Circle) firstRow[1];
        assertEquals(circle.getName(), "circle12");
        assertEquals(circle.getRadius(), 10);
        assertInstanceOf(Rectangle.class, firstRow[2]);
        Rectangle rectangle = (Rectangle) firstRow[2];
        assertEquals(rectangle.getName(), "rectangle13");
        Shape[] secondRow = nestedList.getListArrayShapes().get(1);
        assertEquals(secondRow.length, 2);
        assertEquals(secondRow[0].getName(), "shape21");
        assertInstanceOf(Rectangle.class, secondRow[1]);
        Rectangle rectangle1 = (Rectangle) secondRow[1];
        assertEquals(rectangle1.getName(), "rectangle22");
        Shape[] thirdRow = nestedList.getListArrayShapes().get(2);
        assertEquals(thirdRow.length, 2);
        assertEquals(thirdRow[0].getName(), "shape31");
        assertInstanceOf(Circle.class, thirdRow[1]);
        Circle circle1 = (Circle) thirdRow[1];
        assertEquals(circle1.getName(), "circle32");
        assertEquals(circle1.getRadius(), 10);
        assertNull(nestedList.getAllShapes());
        assertNull(nestedList.getListListShapes());

    }

    @Test
    public void testDeserializeNestedJavaArray(){
        String json = "{\"listListShapes\":null,\"arrayArrayShapes\":[[{\"name\":\"shape11\"},{\"type\":\"circle\",\"name\":\"circle12\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"rectangle13\",\"width\":10,\"height\":20}],[{\"name\":\"shape21\"},{\"type\":\"rectangle\",\"name\":\"rectangle22\",\"width\":10,\"height\":20}],[{\"name\":\"shape31\"},{\"type\":\"circle\",\"name\":\"circle32\",\"radius\":10}]],\"allShapes\":null,\"listArrayShapes\":null}";
        NestedList nestedList = deserializerService.deserialize(json, NestedList.class);
        assertNotNull(nestedList);
        assertNotNull(nestedList.getArrayArrayShapes());
        assertInstanceOf(Shape[][].class, nestedList.getArrayArrayShapes());
        assertEquals(nestedList.getArrayArrayShapes().length, 3);
        Shape[] firstRow = nestedList.getArrayArrayShapes()[0];
        assertEquals(firstRow.length, 3);
        assertEquals(firstRow[0].getName(), "shape11");
        assertInstanceOf(Circle.class, firstRow[1]);
        Circle circle = (Circle) firstRow[1];
        assertEquals(circle.getName(), "circle12");
        assertEquals(circle.getRadius(), 10);
        assertInstanceOf(Rectangle.class, firstRow[2]);
        Rectangle rectangle = (Rectangle) firstRow[2];
        assertEquals(rectangle.getName(), "rectangle13");
        Shape[] secondRow = nestedList.getArrayArrayShapes()[1];
        assertEquals(secondRow.length, 2);
        assertEquals(secondRow[0].getName(), "shape21");
        assertInstanceOf(Rectangle.class, secondRow[1]);
        Rectangle rectangle1 = (Rectangle) secondRow[1];
        assertEquals(rectangle1.getName(), "rectangle22");
        Shape[] thirdRow = nestedList.getArrayArrayShapes()[2];
        assertEquals(thirdRow.length, 2);
        assertEquals(thirdRow[0].getName(), "shape31");
        assertInstanceOf(Circle.class, thirdRow[1]);
        Circle circle1 = (Circle) thirdRow[1];
        assertEquals(circle1.getName(), "circle32");
        assertEquals(circle1.getRadius(), 10);
        assertNull(nestedList.getAllShapes());
        assertNull(nestedList.getListListShapes());


    }

    @Test
    public void testDeserializeListSetQueue(){
        String json = "{\"listListShapes\":null,\"listArrayShapes\":null,\"allShapes\":[[[{\"name\":\"shape111\"},{\"type\":\"circle\",\"name\":\"circle112\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"rectangle113\",\"width\":10,\"height\":20}],[{\"name\":\"shape121\"},{\"type\":\"rectangle\",\"name\":\"rectangle122\",\"width\":10,\"height\":20}]],[[{\"name\":\"shape211\"},{\"type\":\"circle\",\"name\":\"circle212\",\"radius\":10}],[{\"name\":\"shape221\"},{\"type\":\"rectangle\",\"name\":\"rectangle222\",\"width\":10,\"height\":20}]]],\"arrayArrayShapes\":null}";
        NestedList nestedList = deserializerService.deserialize(json, NestedList.class);

        assertNotNull(nestedList);
        assertNotNull(nestedList.getAllShapes());
        assertInstanceOf(List.class, nestedList.getAllShapes());
        assertEquals(nestedList.getAllShapes().size(), 2);
        assertInstanceOf(HashSet.class, nestedList.getAllShapes().get(0));
        assertInstanceOf(HashSet.class, nestedList.getAllShapes().get(1));
        Set<Queue<Shape>> firstRow =  nestedList.getAllShapes().get(0);
        assertEquals(firstRow.size(), 2);
        Iterator<Queue<Shape>> iterator = firstRow.iterator();
        Queue<Shape> firstQueue = iterator.next();
        assertInstanceOf(LinkedList.class, firstQueue);
        assertEquals(firstQueue.size(), 3);
        Shape shape = firstQueue.poll();
        assertEquals(shape.getName(), "shape111");
        Shape shape1 = firstQueue.poll();
        assertInstanceOf(Circle.class, shape1);
        Circle circle = (Circle) shape1;
        assertEquals(circle.getName(), "circle112");
        assertEquals(circle.getRadius(), 10);
        Shape shape2 = firstQueue.poll();
        assertInstanceOf(Rectangle.class, shape2);
        Rectangle rectangle = (Rectangle) shape2;
        assertEquals(rectangle.getName(), "rectangle113");
        Set<Queue<Shape>> secondRow =  nestedList.getAllShapes().get(1);
        assertEquals(secondRow.size(), 2);
        Iterator<Queue<Shape>> iterator1 = secondRow.iterator();
        Queue<Shape> secondQueue = iterator1.next();
        assertInstanceOf(LinkedList.class, secondQueue);
        assertEquals(secondQueue.size(), 2);
        Shape shape3 = secondQueue.poll();
        assertEquals(shape3.getName(), "shape211");
        Shape shape4 = secondQueue.poll();
        assertInstanceOf(Circle.class, shape4);
        Circle circle1 = (Circle) shape4;
        assertEquals(circle1.getName(), "circle212");
        assertEquals(circle1.getRadius(), 10);
        Queue<Shape> thirdQueue = iterator1.next();
        assertInstanceOf(LinkedList.class, thirdQueue);
        assertEquals(thirdQueue.size(), 2);
        Shape shape5 = thirdQueue.poll();
        assertEquals(shape5.getName(), "shape221");
        Shape shape6 = thirdQueue.poll();
        assertInstanceOf(Rectangle.class, shape6);
        Rectangle rectangle1 = (Rectangle) shape6;
        assertEquals(rectangle1.getName(), "rectangle222");
        assertNull(nestedList.getListListShapes());
        assertNull(nestedList.getArrayArrayShapes());

    }


    @Test
    public void testDeserializeObjectWithEmptyList(){
        Figure figure = deserializerService.deserialize("{\"shapes\":[]}", Figure.class);
        assertNotNull(figure);
        assertNotNull(figure.getShapes());
        assertEquals(figure.getShapes().size(), 0);

    }




    // Error cases

    @Test
    public void testTypeNull(){
        String json = "{\"lisListShapes\":null,\"listArrayShapes\":null,\"allShapes\":null,\"arrayArrayShapes\":null}";
        Throwable throwable = assertThrows(NullPointerException.class, () -> deserializerService.deserialize(json, null));
        assertEquals( "Type cannot be null",throwable.getMessage());

    }

    @Test
    public void testJsonNull(){
        Throwable throwable = assertThrows(NullPointerException.class, () -> deserializerService.deserialize(null, NestedList.class));
        assertEquals("Source cannot be null", throwable.getMessage());
    }

    @Test
    public void testDeserializeArrayWithDeserializeObject(){
        String json = "[{\"name\":\"shape11\"},{\"type\":\"circle\",\"name\":\"circle12\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"rectangle13\",\"width\":10,\"height\":20}]";
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> deserializerService.deserialize(json, NestedList.class));
        assertEquals("Source is not Json Object: "+json, throwable.getMessage());
    }

    @Test
    public void testDeserializeValue(){
        String json = "10";
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> deserializerService.deserialize(json, NestedList.class));
        assertEquals("Source is not Json Object: "+json, throwable.getMessage());
    }

    @Test
    public void testDeserializeMalformedJson(){
        String json = "{\"name\":\"shape1\"";
        Throwable throwable = assertThrows(RuntimeException.class, () -> deserializerService.deserialize(json, NestedList.class));
        assertEquals(throwable.getMessage(), "Error when processing Json Object: "+json);
        assertInstanceOf(JsonEOFException.class, throwable.getCause());
    }

    @Test
    public void testDeserializeArrayNullSource(){
        Throwable throwable = assertThrows(NullPointerException.class, () -> deserializerService.deserializeArray(null, NestedList.class));
        assertEquals("Source cannot be null",throwable.getMessage());
    }

    @Test
    public void testDeserializeArrayNullType(){
        String json = "[{\"name\":\"shape11\"},{\"type\":\"circle\",\"name\":\"circle12\",\"radius\":10},{\"type\":\"rectangle\",\"name\":\"rectangle13\",\"width\":10,\"height\":20}]";
        Throwable throwable = assertThrows(NullPointerException.class, () -> deserializerService.deserializeArray(json, (TypeReference<? extends Collection>) null));
        assertEquals( "TypeReference cannot be null", throwable.getMessage());
    }

    @Test
    public void testDeserializeObjectWithDeserializeArray(){
        String json = "{\"name\":\"shape1\"}";
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> deserializerService.deserializeArray(json, Shape.class));
        assertEquals("Source is not a Json Array: "+json, throwable.getMessage());
    }

    @Test
    public void testDeserializeMalformedArrayJson(){
        String json = "[{\"name\":\"shape1\"}";
        Throwable throwable = assertThrows(RuntimeException.class, () -> deserializerService.deserializeArray(json, Shape.class));
        assertEquals(throwable.getMessage(), "Error when processing Json Array: "+json);
        assertInstanceOf(JsonEOFException.class, throwable.getCause());
    }

    @Test
    public void testDeserializeArrayWithNoParameterType(){
        String json = "[{\"name\":\"shape1\"}]";
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> deserializerService.deserializeArray(json, new TypeReference<ArrayList>(){}));
        assertEquals("Collection not provided with generic type: "+ArrayList.class.getName(), throwable.getMessage());

    }

    @Test
    public void testDeserializeArrayWithManyParameterType(){
        String json = "[{\"name\":\"shape1\"}]";
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> deserializerService.deserializeArray(json, new TypeReference<CollectionMultipleTypes<Shape, Circle>>(){}));
        assertEquals("Expected 1 generic type for collection, found: 2", throwable.getMessage());
    }

    @Test
    public void testDeserializeObjectWithWrongType(){
        String json = "{\"name\":\"figure1\",\"shapes\":{\"name\":\"shape11\"}}";
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> deserializerService.deserialize(json, Figure.class));
        assertEquals("Field shapes expected to be a array node, found object node: {\"name\":\"shape11\"}", throwable.getMessage());
    }

    @Test
    public void testDeserializeGenericObject(){
        String json = "{\"value\":{\"name\":\"shape11\"}}";
        Throwable throwable = assertThrows(IllegalArgumentException.class, () ->deserializerService.deserialize(json, CustomGeneric.class));
        assertEquals("Field class io.github.achachraf.typeson.mock.CustomGeneric is a generic, and it is not supported yet", throwable.getMessage());

    }

    @Test
    public void testDeserializeIncompatibleType() {
        String json = "{\"name\":{\"aaa\":\"bbb\"}}";
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> deserializerService.deserialize(json, Shape.class));
        assertEquals("Field name expected to be a value node, found object node: {\"aaa\":\"bbb\"}", throwable.getMessage());

    }

    @Test
    public void testDeserializeIncompatibleType2()  {
        String json = "{\"type\":\"circle\",\"name\":\"circle\",\"radius\":\"10\"}";
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> deserializerService.deserialize(json, Shape.class));
        assertEquals("Field radius expected to be a NUMBER node, found STRING node: \"10\"", throwable.getMessage());

    }

    @Test
    public void testDeserializeDifferentTypes() throws JsonProcessingException {
        String json = "{\"name\":\"shape1\"}";
        Figure figure = deserializerService.deserialize(json, Figure.class);
        System.out.println(figure);
        Figure figure1 = new ObjectMapper().readValue(json, Figure.class);
        System.out.println(figure1);
    }

    @Test
    public void testJsonIgnore(){
        String json = "{\"notIgnored\":\"aa\"}";
        MockForJsonIgnore shape = deserializerService.deserialize(json, MockForJsonIgnore.class);
        assertEquals(shape.getNotIgnored(), "aa");
        assertNull(shape.getIgnored());

    }

    @Test
    public void testJsonIgnoreFilled(){
        String json = "{\"notIgnored\":\"aa\", \"ignored\":\"bb\"}";
        MockForJsonIgnore shape = deserializerService.deserialize(json, MockForJsonIgnore.class);
        assertEquals(shape.getNotIgnored(), "aa");
        assertNull(shape.getIgnored());
    }

    @Test
    public void testJsonProperty(){
        String json = "{\"diff_name\":\"aa\", \"many_aliases\":\"bb\"}";
        MockForJsonProperty shape = deserializerService.deserialize(json, MockForJsonProperty.class);
        assertEquals(shape.getDiffName(), "aa");
        assertEquals(shape.getManyAliases(), "bb");

        json = "{\"diff_name\":\"aa\", \"many_aliases2\":\"bb\"}";
        shape = deserializerService.deserialize(json, MockForJsonProperty.class);
        assertEquals(shape.getDiffName(), "aa");
        assertEquals(shape.getManyAliases(), "bb");

        String json2 = "{\"diff_name\":\"aa\", \"many_aliases3\":\"bb\"}";
        Throwable throwable = assertThrows(IllegalStateException.class, () -> deserializerService.deserialize(json2, MockForJsonProperty.class));
        assertEquals("Unknown properties: [many_aliases3] for type: class io.github.achachraf.typeson.mock.MockForJsonProperty", throwable.getMessage());
    }



}
