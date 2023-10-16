package org.ach.typeson;

import org.ach.typeson.interlay.SerializerServiceImpl;
import org.ach.typeson.mock.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializerServiceImplTest {

    private final SerializerServiceImpl serializerService = new SerializerServiceImpl();

    @Test
    public void testSerialize(){
        Shape shape = new Circle("circle", 10);
        String json = serializerService.serialize(shape);
        assertEquals("{\"name\":\"circle\",\"radius\":10,\"type\":\"circle\"}", json);
    }

    @Test
    public void testSerializeNestedObject(){
        CircleAndRectangle circleAndRectangle = new CircleAndRectangle()
                .setName("circleAndRectangle")
                .setCircle(new Circle("circle", 10))
                .setRectangle(new Rectangle("rectangle", 10, 20));
                ;
        String json = serializerService.serialize(circleAndRectangle);
        assertEquals("{\"name\":\"circleAndRectangle\",\"circle\":{\"name\":\"circle\",\"radius\":10,\"type\":\"circle\"},\"rectangle\":{\"name\":\"rectangle\",\"width\":10,\"height\":20,\"type\":\"rectangle\"},\"type\":\"circleAndRectangle\"}", json);
    }

    @Test
    public void testSerializeList(){
        List<Shape> shapes = List.of(
                new Circle("Acircle", 10),
                new Rectangle("Arectangle", 10, 20)
        );
        String json = serializerService.serialize(shapes);
        assertEquals("[{\"name\":\"Acircle\",\"radius\":10,\"type\":\"circle\"},{\"name\":\"Arectangle\",\"width\":10,\"height\":20,\"type\":\"rectangle\"}]", json);
    }

    @Test
    public void testSerializeNestedObjectWithNull(){
        CircleAndRectangle circleAndRectangle = new CircleAndRectangle()
                .setName("circleAndRectangle")
                .setCircle(new Circle("circle", 10))
                .setRectangle(null);
        String json = serializerService.serialize(circleAndRectangle);
        assertEquals("{\"name\":\"circleAndRectangle\",\"circle\":{\"name\":\"circle\",\"radius\":10,\"type\":\"circle\"},\"rectangle\":null,\"type\":\"circleAndRectangle\"}", json);
    }

    @Test
    public void testSerializeNestedObjectMissingField(){
        CircleAndRectangle circleAndRectangle = new CircleAndRectangle()
                .setName("circleAndRectangle")
                .setCircle(new Circle("circle", 10));
        String json = serializerService.serialize(circleAndRectangle);
        assertEquals("{\"name\":\"circleAndRectangle\",\"circle\":{\"name\":\"circle\",\"radius\":10,\"type\":\"circle\"},\"rectangle\":null,\"type\":\"circleAndRectangle\"}", json);
    }

    @Test
    public void testSerializeListInObject(){
        Figure figure = new Figure()
                .setName("figure")
                .setShapes(List.of(
                        new Circle("Acircle", 10),
                        new Rectangle("Arectangle", 10, 20)
                ));
        String json = serializerService.serialize(figure);
        assertEquals("{\"name\":\"figure\",\"shapes\":[{\"name\":\"Acircle\",\"radius\":10,\"type\":\"circle\"},{\"name\":\"Arectangle\",\"width\":10,\"height\":20,\"type\":\"rectangle\"}]}", json);
    }

    @Test
    public void testSerializeListInObjectNoType(){
        UnannotatedShape unannotatedMock = new UnannotatedShape()
                .setName("unannotatedMock")
                .setAdditionalField("unannotatedMock")
                ;
        String json = serializerService.serialize(unannotatedMock);
        assertEquals("{\"name\":\"unannotatedMock\",\"additionalField\":\"unannotatedMock\"}", json);
    }

    @Test
    public void testSerializeCompositeObject(){
        FirstCompositeMock firstCompositeMock = new FirstCompositeMock()
                .setName("firstCompositeMock")
                .setSecondCompositeMock(new SecondCompositeMock()
                        .setNumber(20)
                        .setShape(new Circle("circle", 10))
                        .setFirstCompositeMock(new FirstCompositeMock()
                                .setName("firstCompositeMock")
                                .setSecondCompositeMock(new SecondCompositeMock()
                                        .setNumber(20)
                                        .setShape(new Rectangle("rectangle", 10, 20))
                                )
                        )
                );
        String json = serializerService.serialize(firstCompositeMock);
        assertEquals("{\"name\":\"firstCompositeMock\",\"secondCompositeMock\":{\"number\":20,\"shape\":{\"name\":\"circle\",\"radius\":10,\"type\":\"circle\"},\"firstCompositeMock\":{\"name\":\"firstCompositeMock\",\"secondCompositeMock\":{\"number\":20,\"shape\":{\"name\":\"rectangle\",\"width\":10,\"height\":20,\"type\":\"rectangle\"},\"firstCompositeMock\":null,\"type\":\"secondCompositeMock\"},\"firstCompositeMock\":null},\"type\":\"secondCompositeMock\"},\"firstCompositeMock\":null}", json);
    }

    @Test
    public void testSerializeListCompositeObjectPolymorphism(){
        List<CompositeCollection> compositeCollectionList = List.of(
                new CompositeCollection()
                        .setFirstCompositeMock(new FirstCompositeMock()
                                .setName("AfirstCompositeMock1")
                                .setSecondCompositeMock(new SecondCompositeMock()
                                        .setNumber(10)
                                        .setFirstCompositeMock(new FirstCompositeMock()
                                                .setName("AfirstCompositeMock1.1.2")
                                        )
                                )
                        )
                        .setSecondCompositeMock(new SecondCompositeMock()
                                .setNumber(20)
                                .setFirstCompositeMock(new FirstCompositeMock()
                                        .setName("AfirstCompositeMock1.2.1")
                                )
                        )
                        .addFirstCompositeMock(new FirstCompositeMock()
                                .setName("AfirstCompositeMockList1")
                        )
                        .addSecondCompositeMock(new SecondCompositeMock()
                                .setNumber(30)
                        )
                        .addSecondCompositeMock(new ThirdCompositeMock()
                                .setNumber(40)
                                .setShape(new Shape("shape1"))
                                .setName("AthirdCompositeMock1")
                                .setSecondCompositeMock(new SecondCompositeMock()
                                        .setNumber(50)
                                        .setFirstCompositeMock(new FirstCompositeMock()
                                                .setName("AfirstCompositeMock1.2.2")
                                        )
                                )
                        )
                ,
                new CompositeCollection()
                        .addFirstCompositeMock(new FirstCompositeMock()
                                .setName("AfirstCompositeMock2")
                                .setSecondCompositeMock(new ThirdCompositeMock()
                                        .setNumber(60)
                                        .setName("AthirdCompositeMock2")
                                        .setShape(new Shape("shape2"))
                                )
                        )
                        .addSecondCompositeMock(new SecondCompositeMock()
                                .setNumber(70)
                                .setFirstCompositeMock(new FirstCompositeMock()
                                        .setName("AfirstCompositeMock2.2.1")
                                )
                        )

        );
        String json = serializerService.serialize(compositeCollectionList);
        assertEquals("[{\"firstCompositeMock\":{\"name\":\"AfirstCompositeMock1\",\"secondCompositeMock\":{\"number\":10,\"shape\":null,\"firstCompositeMock\":{\"name\":\"AfirstCompositeMock1.1.2\",\"secondCompositeMock\":null,\"firstCompositeMock\":null},\"type\":\"secondCompositeMock\"},\"firstCompositeMock\":null},\"secondCompositeMock\":{\"number\":20,\"shape\":null,\"firstCompositeMock\":{\"name\":\"AfirstCompositeMock1.2.1\",\"secondCompositeMock\":null,\"firstCompositeMock\":null},\"type\":\"secondCompositeMock\"},\"firstCompositeMockList\":[{\"name\":\"AfirstCompositeMockList1\",\"secondCompositeMock\":null,\"firstCompositeMock\":null}],\"secondCompositeMockList\":[{\"number\":30,\"shape\":null,\"firstCompositeMock\":null,\"type\":\"secondCompositeMock\"},{\"number\":40,\"shape\":{\"name\":\"shape1\",\"type\":\"shape\"},\"firstCompositeMock\":null,\"name\":\"AthirdCompositeMock1\",\"secondCompositeMock\":{\"number\":50,\"shape\":null,\"firstCompositeMock\":{\"name\":\"AfirstCompositeMock1.2.2\",\"secondCompositeMock\":null,\"firstCompositeMock\":null},\"type\":\"secondCompositeMock\"},\"type\":\"thirdCompositeMock\"}]},{\"firstCompositeMock\":null,\"secondCompositeMock\":null,\"firstCompositeMockList\":[{\"name\":\"AfirstCompositeMock2\",\"secondCompositeMock\":{\"number\":60,\"shape\":{\"name\":\"shape2\",\"type\":\"shape\"},\"firstCompositeMock\":null,\"name\":\"AthirdCompositeMock2\",\"secondCompositeMock\":null,\"type\":\"thirdCompositeMock\"},\"firstCompositeMock\":null}],\"secondCompositeMockList\":[{\"number\":70,\"shape\":null,\"firstCompositeMock\":{\"name\":\"AfirstCompositeMock2.2.1\",\"secondCompositeMock\":null,\"firstCompositeMock\":null},\"type\":\"secondCompositeMock\"}]}]", json);
    }

    @Test
    public void testJsonIgnore(){
        MockForJsonIgnore mockForJsonIgnore = new MockForJsonIgnore()
                .setIgnored("ignored")
                .setNotIgnored("notIgnored");
        String json = serializerService.serialize(mockForJsonIgnore);
        assertEquals("{\"notIgnored\":\"notIgnored\"}", json);
    }

    @Test
    public void testJsonProperty(){
        MockForJsonProperty mockForJsonProperty = new MockForJsonProperty()
                .setDiffName("diffName")
                .setManyAliases("manyAliases");
        String json = serializerService.serialize(mockForJsonProperty);
        assertEquals("{\"diff_name\":\"diffName\",\"manyAliases\":\"manyAliases\"}", json);
    }
}
