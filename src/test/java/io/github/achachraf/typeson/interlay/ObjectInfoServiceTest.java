package io.github.achachraf.typeson.interlay;

import io.github.achachraf.typeson.domain.ListObjectInfo;
import io.github.achachraf.typeson.domain.ObjectInfo;
import io.github.achachraf.typeson.domain.SingleObjectInfo;
import io.github.achachraf.typeson.interlay.mock.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ObjectInfoServiceTest {

    private final ObjectInfoService objectInfoService = new ObjectInfoService();

    @Test
    public void testGetSingleObjectInfo() {
        Shape shape = new Circle("Acircle", 10);
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(shape);
        assertInstanceOf(SingleObjectInfo.class, objectInfo);
        SingleObjectInfo singleObjectInfo = (SingleObjectInfo) objectInfo;
        assertEquals(Circle.class, objectInfo.getType());
        assertEquals(0, singleObjectInfo.getGetters().size());
        assertEquals(0, singleObjectInfo.getSetters().size());
        singleObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        singleObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertTrue(singleObjectInfo.getSubObjects().isEmpty());
    }

    @Test
    public void testGetSingleObjectInfoTwoObjects() {
        CircleAndRectangle circleAndRectangle = new CircleAndRectangle()
                .setName("AcircleAndRectangle")
                .setCircle(new Circle("Acircle", 10))
                .setRectangle(new Rectangle("Arectangle", 10, 20));
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(circleAndRectangle);
        assertInstanceOf(SingleObjectInfo.class, objectInfo);
        SingleObjectInfo singleObjectInfo = (SingleObjectInfo) objectInfo;
        assertEquals(CircleAndRectangle.class, objectInfo.getType());
        assertEquals(2, singleObjectInfo.getGetters().size());
        assertGetters(singleObjectInfo);
        assertEquals(2, singleObjectInfo.getSetters().size());
        assertSetters(singleObjectInfo);
        assertEquals(2, singleObjectInfo.getSubObjects().size());

        ObjectInfo rectangleInfo = singleObjectInfo.getSubObject("rectangle");
        assertInstanceOf(SingleObjectInfo.class, rectangleInfo);
        SingleObjectInfo rectangleSingleObjectInfo = (SingleObjectInfo) rectangleInfo;
        assertEquals(Rectangle.class, rectangleInfo.getType());
        assertEquals(0, rectangleSingleObjectInfo.getGetters().size());
        assertEquals(0, rectangleSingleObjectInfo.getSetters().size());
        assertTrue(rectangleSingleObjectInfo.getSubObjects().isEmpty());

        ObjectInfo circleInfo = singleObjectInfo.getSubObject("circle");
        assertInstanceOf(SingleObjectInfo.class, circleInfo);
        SingleObjectInfo circleSingleObjectInfo = (SingleObjectInfo) circleInfo;
        assertEquals(Circle.class, circleInfo.getType());
        assertEquals(0, circleSingleObjectInfo.getGetters().size());
        assertEquals(0, circleSingleObjectInfo.getSetters().size());
        assertTrue(circleSingleObjectInfo.getSubObjects().isEmpty());
    }

    @Test
    public void testGetSingleObjectInfoComposite(){
        FirstCompositeMock firstCompositeMock = new FirstCompositeMock()
                .setName("AfirstCompositeMock")
                .setSecondCompositeMock(new SecondCompositeMock()
                        .setNumber(10)
                        .setShape(new Circle("Acircle", 10))
                );
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(firstCompositeMock);
        assertInstanceOf(SingleObjectInfo.class, objectInfo);
        SingleObjectInfo singleObjectInfo = (SingleObjectInfo) objectInfo;
        assertEquals(FirstCompositeMock.class, objectInfo.getType());
        assertEquals(1, singleObjectInfo.getGetters().size());
        assertGetters(singleObjectInfo);
        assertEquals(1, singleObjectInfo.getSetters().size());
        assertSetters(singleObjectInfo);
        assertEquals(1, singleObjectInfo.getSubObjects().size());

        ObjectInfo secondCompositeInfo = singleObjectInfo.getSubObject("secondCompositeMock");
        assertInstanceOf(SingleObjectInfo.class, secondCompositeInfo);
        SingleObjectInfo secondCompositeSingleObjectInfo = (SingleObjectInfo) secondCompositeInfo;
        assertEquals(SecondCompositeMock.class, secondCompositeInfo.getType());
        assertEquals(1, secondCompositeSingleObjectInfo.getGetters().size());
        assertGetters(secondCompositeSingleObjectInfo);
        assertEquals(1, secondCompositeSingleObjectInfo.getSetters().size());
        assertSetters(secondCompositeSingleObjectInfo);
        assertEquals(1, secondCompositeSingleObjectInfo.getSubObjects().size());

        ObjectInfo circleInfo = secondCompositeSingleObjectInfo.getSubObject("shape");
        assertInstanceOf(SingleObjectInfo.class, circleInfo);
        SingleObjectInfo circleSingleObjectInfo = (SingleObjectInfo) circleInfo;
        assertEquals(Circle.class, circleInfo.getType());
        assertEquals(0, circleSingleObjectInfo.getGetters().size());
        assertEquals(0, circleSingleObjectInfo.getSetters().size());
        circleSingleObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        circleSingleObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertTrue(circleSingleObjectInfo.getSubObjects().isEmpty());
    }

    @Test
    public void testGetSingleObjectInfoCompositeLoop(){
        FirstCompositeMock firstCompositeMock = new FirstCompositeMock()
                .setName("AfirstCompositeMock")
                .setSecondCompositeMock(new SecondCompositeMock()
                        .setNumber(10)
                        .setShape(new Circle("Acircle", 10))
                        .setFirstCompositeMock(new FirstCompositeMock()
                                .setName("AfirstCompositeMock2")
                                .setSecondCompositeMock(new SecondCompositeMock()
                                        .setNumber(11)
                                        .setShape(new Circle("Acircle2", 11))
                                )
                        )
                );
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(firstCompositeMock);
        assertInstanceOf(SingleObjectInfo.class, objectInfo);
        SingleObjectInfo singleObjectInfo = (SingleObjectInfo) objectInfo;
        assertEquals(FirstCompositeMock.class, objectInfo.getType());
        assertEquals(1, singleObjectInfo.getGetters().size());
        assertGetters(singleObjectInfo);
        assertEquals(1, singleObjectInfo.getSetters().size());
        assertSetters(singleObjectInfo);
        assertEquals(1, singleObjectInfo.getSubObjects().size());

        ObjectInfo secondCompositeInfo = singleObjectInfo.getSubObject("secondCompositeMock");
        assertInstanceOf(SingleObjectInfo.class, secondCompositeInfo);
        SingleObjectInfo secondCompositeSingleObjectInfo = (SingleObjectInfo) secondCompositeInfo;
        assertEquals(SecondCompositeMock.class, secondCompositeInfo.getType());
        assertEquals(2, secondCompositeSingleObjectInfo.getGetters().size());
        assertGetters(secondCompositeSingleObjectInfo);
        assertEquals(2, secondCompositeSingleObjectInfo.getSetters().size());
        assertSetters(secondCompositeSingleObjectInfo);
        assertEquals(2, secondCompositeSingleObjectInfo.getSubObjects().size());

        ObjectInfo circleInfo = secondCompositeSingleObjectInfo.getSubObject("shape");
        assertInstanceOf(SingleObjectInfo.class, circleInfo);
        SingleObjectInfo circleSingleObjectInfo = (SingleObjectInfo) circleInfo;
        assertEquals(Circle.class, circleInfo.getType());
        assertEquals(0, circleSingleObjectInfo.getGetters().size());
        assertEquals(0, circleSingleObjectInfo.getSetters().size());
        circleSingleObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        circleSingleObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertTrue(circleSingleObjectInfo.getSubObjects().isEmpty());

        ObjectInfo firstCompositeInfo = secondCompositeSingleObjectInfo.getSubObject("firstCompositeMock");
        assertEquals(FirstCompositeMock.class, firstCompositeInfo.getType());
        assertInstanceOf(SingleObjectInfo.class, firstCompositeInfo);
        SingleObjectInfo firstCompositeSingleObjectInfo = (SingleObjectInfo) firstCompositeInfo;
        assertEquals(1, firstCompositeSingleObjectInfo.getGetters().size());
        assertGetters(firstCompositeSingleObjectInfo);
        assertEquals(1, firstCompositeSingleObjectInfo.getSetters().size());
        assertSetters(firstCompositeSingleObjectInfo);
        assertEquals(1, firstCompositeSingleObjectInfo.getSubObjects().size());

        ObjectInfo secondCompositeInfo2 = firstCompositeSingleObjectInfo.getSubObject("secondCompositeMock");
        assertInstanceOf(SingleObjectInfo.class, secondCompositeInfo2);
        SingleObjectInfo secondCompositeSingleObjectInfo2 = (SingleObjectInfo) secondCompositeInfo2;
        assertEquals(SecondCompositeMock.class, secondCompositeInfo2.getType());
        assertEquals(1, secondCompositeSingleObjectInfo2.getGetters().size());
        assertGetters(secondCompositeSingleObjectInfo2);
        assertEquals(1, secondCompositeSingleObjectInfo2.getSetters().size());
        assertSetters(secondCompositeSingleObjectInfo2);
        assertEquals(1, secondCompositeSingleObjectInfo2.getSubObjects().size());

        ObjectInfo circleInfo2 = secondCompositeSingleObjectInfo2.getSubObject("shape");
        assertInstanceOf(SingleObjectInfo.class, circleInfo2);
        SingleObjectInfo circleSingleObjectInfo2 = (SingleObjectInfo) circleInfo2;
        assertEquals(Circle.class, circleInfo2.getType());
        assertEquals(0, circleSingleObjectInfo2.getGetters().size());
        assertEquals(0, circleSingleObjectInfo2.getSetters().size());
        circleSingleObjectInfo2.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        circleSingleObjectInfo2.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertTrue(circleSingleObjectInfo2.getSubObjects().isEmpty());

    }

    @Test
    public void testGetSingleObjectInfoCompositeLoopRecursive(){
        FirstCompositeMock firstCompositeMock = new FirstCompositeMock()
                .setName("AfirstCompositeMock")
                .setFirstCompositeMock(new FirstCompositeMock()
                        .setName("AfirstCompositeMock2")
                );
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(firstCompositeMock);
        assertInstanceOf(SingleObjectInfo.class, objectInfo);
        SingleObjectInfo singleObjectInfo = (SingleObjectInfo) objectInfo;
        assertEquals(FirstCompositeMock.class, objectInfo.getType());
        assertEquals(1, singleObjectInfo.getGetters().size());
        assertGetters(singleObjectInfo);
        assertEquals(1, singleObjectInfo.getSetters().size());
        assertSetters(singleObjectInfo);
        assertEquals(1, singleObjectInfo.getSubObjects().size());

        ObjectInfo firstCompositeInfo = singleObjectInfo.getSubObject("firstCompositeMock");
        assertInstanceOf(SingleObjectInfo.class, firstCompositeInfo);
        SingleObjectInfo firstCompositeSingleObjectInfo = (SingleObjectInfo) firstCompositeInfo;
        assertEquals(FirstCompositeMock.class, firstCompositeInfo.getType());
        assertEquals(0, firstCompositeSingleObjectInfo.getGetters().size());
        assertEquals(0, firstCompositeSingleObjectInfo.getSetters().size());
        firstCompositeSingleObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        firstCompositeSingleObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertTrue(firstCompositeSingleObjectInfo.getSubObjects().isEmpty());

    }

    @Test
    public void testGetSingleObjectInfoCompositeLoopRecursiveReference(){
        FirstCompositeMock firstCompositeMock = new FirstCompositeMock()
                .setName("AfirstCompositeMock");
        firstCompositeMock.setFirstCompositeMock(firstCompositeMock);
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(firstCompositeMock);
        System.out.println(objectInfo);
    }

    @Test
    public void testGetObjectInfoTwoSameTypes(){
        CircleAndRectangle circleAndRectangle = new CircleAndRectangle()
                .setName("AcircleAndRectangle")
                .setCircle(new Shape("Acircle"))
                .setRectangle(new Shape("Arectangle"));
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(circleAndRectangle);

        assertInstanceOf(SingleObjectInfo.class, objectInfo);
        SingleObjectInfo singleObjectInfo = (SingleObjectInfo) objectInfo;

        ObjectInfo shape = singleObjectInfo.getSubObject("circle");
        System.out.println(shape);

    }

    @Test
    public void testGetListObjectInfo(){
        List<Shape> shapes = List.of(
                new Shape("shape1"),
                new Shape("shape2")
        );
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(shapes);

        assertInstanceOf(ListObjectInfo.class, objectInfo);
        ListObjectInfo listObjectInfo = (ListObjectInfo) objectInfo;

        assertInstanceOf(SingleObjectInfo.class, listObjectInfo.getObjectList().get(0));
        SingleObjectInfo singleObjectInfo1 = (SingleObjectInfo) listObjectInfo.getObjectList().get(0);
        assertEquals("$ROOT_OBJECT$[0]", singleObjectInfo1.getName());
        assertEquals(Shape.class, singleObjectInfo1.getType());
        assertEquals(0, singleObjectInfo1.getGetters().size());
        assertEquals(0, singleObjectInfo1.getSetters().size());
        singleObjectInfo1.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        singleObjectInfo1.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertTrue(singleObjectInfo1.getSubObjects().isEmpty());

        SingleObjectInfo singleObjectInfo2 = (SingleObjectInfo) listObjectInfo.getObjectList().get(1);
        assertEquals("$ROOT_OBJECT$[1]", singleObjectInfo2.getName());
        assertEquals(Shape.class, singleObjectInfo2.getType());
        assertEquals(0, singleObjectInfo2.getGetters().size());
        assertEquals(0, singleObjectInfo2.getSetters().size());
        singleObjectInfo2.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        singleObjectInfo2.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertTrue(singleObjectInfo2.getSubObjects().isEmpty());

        assertTrue(List.class.isAssignableFrom(listObjectInfo.getContainerClass()));


    }

    @Test
    public void testGetListObjectInfoSubTypes(){
        List<Shape> shapes = List.of(
                new Circle("circle1", 1),
                new Rectangle("rectangle1", 2,3)
        );
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(shapes);

        assertInstanceOf(ListObjectInfo.class, objectInfo);
        ListObjectInfo listObjectInfo = (ListObjectInfo) objectInfo;

        assertInstanceOf(SingleObjectInfo.class, listObjectInfo.getObjectList().get(0));
        SingleObjectInfo singleObjectInfo1 = (SingleObjectInfo) listObjectInfo.getObjectList().get(0);
        assertEquals("$ROOT_OBJECT$[0]", singleObjectInfo1.getName());
        assertEquals(Circle.class, singleObjectInfo1.getType());
        assertEquals(0, singleObjectInfo1.getGetters().size());
        assertEquals(0, singleObjectInfo1.getSetters().size());
        singleObjectInfo1.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        singleObjectInfo1.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertTrue(singleObjectInfo1.getSubObjects().isEmpty());

        assertInstanceOf(SingleObjectInfo.class, listObjectInfo.getObjectList().get(1));
        SingleObjectInfo singleObjectInfo2 = (SingleObjectInfo) listObjectInfo.getObjectList().get(1);
        assertEquals("$ROOT_OBJECT$[1]", singleObjectInfo2.getName());
        assertEquals(Rectangle.class, singleObjectInfo2.getType());
        assertEquals(0, singleObjectInfo2.getGetters().size());
        assertEquals(0, singleObjectInfo2.getSetters().size());
        singleObjectInfo2.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        singleObjectInfo2.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertTrue(singleObjectInfo2.getSubObjects().isEmpty());
        assertTrue(List.class.isAssignableFrom(listObjectInfo.getContainerClass()));

    }

    @Test
    public void testGetSingleObjectInfoWithObjectListInfo(){
        Figure figure = new Figure()
                .setName("figure1")
                .setShapes(List.of(
                        new Circle("circle1", 1),
                        new Rectangle("rectangle1", 2,3)
                ));
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(figure);

        assertInstanceOf(SingleObjectInfo.class, objectInfo);
        SingleObjectInfo singleObjectInfo = (SingleObjectInfo) objectInfo;
        assertEquals(Figure.class, singleObjectInfo.getType());
        assertEquals(1, singleObjectInfo.getGetters().size());
        assertGetters(singleObjectInfo);
        assertEquals(1, singleObjectInfo.getSetters().size());
        assertSetters(singleObjectInfo);
        assertEquals(1, singleObjectInfo.getSubObjects().size());

        ObjectInfo objectListInfo = singleObjectInfo.getSubObject("shapes");
        assertInstanceOf(ListObjectInfo.class, objectListInfo);
        ListObjectInfo listObjectInfo = (ListObjectInfo) objectListInfo;
        assertEquals(2, listObjectInfo.getObjectList().size());

        assertInstanceOf(SingleObjectInfo.class, listObjectInfo.getObjectList().get(0));
        SingleObjectInfo circleObjectInfo = (SingleObjectInfo) listObjectInfo.getObjectList().get(0);
        assertEquals("shapes[0]", circleObjectInfo.getName());
        assertEquals(Circle.class, circleObjectInfo.getType());
        assertEquals(0, circleObjectInfo.getGetters().size());
        assertEquals(0, circleObjectInfo.getSetters().size());
        circleObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        circleObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertTrue(circleObjectInfo.getSubObjects().isEmpty());

        assertInstanceOf(SingleObjectInfo.class, listObjectInfo.getObjectList().get(1));
        SingleObjectInfo rectangleObjectInfo = (SingleObjectInfo) listObjectInfo.getObjectList().get(1);
        assertEquals("shapes[1]", rectangleObjectInfo.getName());
        assertEquals(Rectangle.class, rectangleObjectInfo.getType());
        assertEquals(0, rectangleObjectInfo.getGetters().size());
        assertEquals(0, rectangleObjectInfo.getSetters().size());
        assertTrue(rectangleObjectInfo.getSubObjects().isEmpty());

    }

    @Test
    public void testGetListObjectInfoWithCompositeListLoopRecursiveSubtypes(){
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
                                .setFirstCompositeMock(new FirstCompositeMock()
                                        .setName("AfirstCompositeMock1.3.1")
                                        .setSecondCompositeMock(new SecondCompositeMock()
                                                .setNumber(50)
                                                .setFirstCompositeMock(new FirstCompositeMock()
                                                        .setName("AfirstCompositeMock1.3.1.1")
                                                )
                                        )
                                )
                        )
                ,
                new CompositeCollection()
                        .addFirstCompositeMock(new FirstCompositeMock()
                                .setName("AfirstCompositeMock2")
                                .setSecondCompositeMock(new ThirdCompositeMock()
                                        .setNumber(60)
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
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(compositeCollectionList);

        assertInstanceOf(ListObjectInfo.class, objectInfo);
        ListObjectInfo listObjectInfo = (ListObjectInfo) objectInfo;
        assertEquals(2, listObjectInfo.getObjectList().size());
        assertTrue(List.class.isAssignableFrom(listObjectInfo.getContainerClass()));

        SingleObjectInfo collectionCompositeObjectInfo1 = (SingleObjectInfo) listObjectInfo.getObjectList().get(0);
        assertEquals("$ROOT_OBJECT$[0]", collectionCompositeObjectInfo1.getName());
        assertEquals(CompositeCollection.class, collectionCompositeObjectInfo1.getType());
        assertEquals(4, collectionCompositeObjectInfo1.getGetters().size());
        assertGetters(collectionCompositeObjectInfo1);
        assertEquals(4, collectionCompositeObjectInfo1.getSetters().size());
        assertSetters(collectionCompositeObjectInfo1);
        assertEquals(4, collectionCompositeObjectInfo1.getSubObjects().size());

        SingleObjectInfo firstCompositeMockObjectInfo1 = (SingleObjectInfo) collectionCompositeObjectInfo1.getSubObject("firstCompositeMock");
        assertEquals(FirstCompositeMock.class, firstCompositeMockObjectInfo1.getType());
        assertEquals(1, firstCompositeMockObjectInfo1.getGetters().size());
        assertGetters(firstCompositeMockObjectInfo1);
        assertEquals(1, firstCompositeMockObjectInfo1.getSetters().size());
        assertSetters(firstCompositeMockObjectInfo1);
        assertEquals(1, firstCompositeMockObjectInfo1.getSubObjects().size());

        SingleObjectInfo firstCompositeSecondCompositeMockObjectInfo = (SingleObjectInfo) firstCompositeMockObjectInfo1.getSubObject("secondCompositeMock");
        assertEquals(SecondCompositeMock.class, firstCompositeSecondCompositeMockObjectInfo.getType());
        assertEquals(1, firstCompositeSecondCompositeMockObjectInfo.getGetters().size());
        assertGetters(firstCompositeSecondCompositeMockObjectInfo);
        assertEquals(1, firstCompositeSecondCompositeMockObjectInfo.getSetters().size());
        assertSetters(firstCompositeSecondCompositeMockObjectInfo);
        assertEquals(1, firstCompositeSecondCompositeMockObjectInfo.getSubObjects().size());

        SingleObjectInfo firstCompositeSecondCompositeMockFirstCompositeMockObjectInfo = (SingleObjectInfo) firstCompositeSecondCompositeMockObjectInfo.getSubObject("firstCompositeMock");
        assertEquals(FirstCompositeMock.class, firstCompositeSecondCompositeMockFirstCompositeMockObjectInfo.getType());
        assertEquals(0, firstCompositeSecondCompositeMockFirstCompositeMockObjectInfo.getGetters().size());
        assertEquals(0, firstCompositeSecondCompositeMockFirstCompositeMockObjectInfo.getSetters().size());
        firstCompositeSecondCompositeMockFirstCompositeMockObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        firstCompositeSecondCompositeMockFirstCompositeMockObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertEquals(0, firstCompositeSecondCompositeMockFirstCompositeMockObjectInfo.getSubObjects().size());


        SingleObjectInfo secondCompositeMockObjectInfo1 = (SingleObjectInfo) collectionCompositeObjectInfo1.getSubObject("secondCompositeMock");
        assertEquals(SecondCompositeMock.class, secondCompositeMockObjectInfo1.getType());
        assertEquals(1, secondCompositeMockObjectInfo1.getGetters().size());
        assertGetters(secondCompositeMockObjectInfo1);
        assertEquals(1, secondCompositeMockObjectInfo1.getSetters().size());
        assertSetters(secondCompositeMockObjectInfo1);
        assertEquals(1, secondCompositeMockObjectInfo1.getSubObjects().size());

        SingleObjectInfo secondCompositeMockFirstCompositeMockObjectInfo = (SingleObjectInfo) secondCompositeMockObjectInfo1.getSubObject("firstCompositeMock");
        assertEquals(FirstCompositeMock.class, secondCompositeMockFirstCompositeMockObjectInfo.getType());
        assertEquals(0, secondCompositeMockFirstCompositeMockObjectInfo.getGetters().size());
        assertEquals(0, secondCompositeMockFirstCompositeMockObjectInfo.getSetters().size());
        secondCompositeMockFirstCompositeMockObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        secondCompositeMockFirstCompositeMockObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertEquals(0, secondCompositeMockFirstCompositeMockObjectInfo.getSubObjects().size());

        ListObjectInfo firstCompositeMockList = (ListObjectInfo) collectionCompositeObjectInfo1.getSubObject("firstCompositeMockList");
        assertEquals(1, firstCompositeMockList.getObjectList().size());
        assertTrue(List.class.isAssignableFrom(firstCompositeMockList.getContainerClass()));
        SingleObjectInfo firstCompositeMockListObjectInfo = (SingleObjectInfo) firstCompositeMockList.getObjectList().get(0);
        assertEquals("firstCompositeMockList[0]", firstCompositeMockListObjectInfo.getName());
        assertEquals(FirstCompositeMock.class, firstCompositeMockListObjectInfo.getType());
        assertEquals(0, firstCompositeMockListObjectInfo.getGetters().size());
        assertEquals(0, firstCompositeMockListObjectInfo.getSetters().size());
        firstCompositeMockListObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        firstCompositeMockListObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertEquals(0, firstCompositeMockListObjectInfo.getSubObjects().size());

        ListObjectInfo secondCompositeMockList = (ListObjectInfo) collectionCompositeObjectInfo1.getSubObject("secondCompositeMockList");
        assertEquals(2, secondCompositeMockList.getObjectList().size());
        assertTrue(List.class.isAssignableFrom(secondCompositeMockList.getContainerClass()));
        SingleObjectInfo secondCompositeMockListObjectInfo = (SingleObjectInfo) secondCompositeMockList.getObjectList().get(0);
        assertEquals("secondCompositeMockList[0]", secondCompositeMockListObjectInfo.getName());
        assertEquals(SecondCompositeMock.class, secondCompositeMockListObjectInfo.getType());
        assertEquals(0, secondCompositeMockListObjectInfo.getGetters().size());
        assertEquals(0, secondCompositeMockListObjectInfo.getSetters().size());
        secondCompositeMockListObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        secondCompositeMockListObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertEquals(0, secondCompositeMockListObjectInfo.getSubObjects().size());

        ObjectInfo thirdCompositeMockObjectInfo = secondCompositeMockList.getObjectList().get(1);
        assertInstanceOf(SingleObjectInfo.class, thirdCompositeMockObjectInfo);
        SingleObjectInfo thirdCompositeMockSingleObjectInfo = (SingleObjectInfo) thirdCompositeMockObjectInfo;
        assertEquals("secondCompositeMockList[1]", thirdCompositeMockSingleObjectInfo.getName());
        assertEquals(ThirdCompositeMock.class, thirdCompositeMockSingleObjectInfo.getType());
        assertEquals(2, thirdCompositeMockSingleObjectInfo.getGetters().size());
        assertGetters(thirdCompositeMockSingleObjectInfo);
        assertEquals(2, thirdCompositeMockSingleObjectInfo.getSetters().size());
        assertSetters(thirdCompositeMockSingleObjectInfo);
        assertEquals(2, thirdCompositeMockSingleObjectInfo.getSubObjects().size());

        SingleObjectInfo thirdCompositeMockFirstCompositeMockObjectInfo =
                (SingleObjectInfo) thirdCompositeMockSingleObjectInfo.getSubObject("firstCompositeMock");
        assertEquals(FirstCompositeMock.class, thirdCompositeMockFirstCompositeMockObjectInfo.getType());
        assertEquals(1, thirdCompositeMockFirstCompositeMockObjectInfo.getGetters().size());
        assertGetters(thirdCompositeMockFirstCompositeMockObjectInfo);
        assertEquals(1, thirdCompositeMockFirstCompositeMockObjectInfo.getSetters().size());
        assertSetters(thirdCompositeMockFirstCompositeMockObjectInfo);
        assertEquals(1, thirdCompositeMockFirstCompositeMockObjectInfo.getSubObjects().size());

        SingleObjectInfo thirdCompositeMockFirstCompositeMockSecondCompositeMockObjectInfo =
                (SingleObjectInfo) thirdCompositeMockFirstCompositeMockObjectInfo.getSubObject("secondCompositeMock");
        assertEquals(SecondCompositeMock.class, thirdCompositeMockFirstCompositeMockSecondCompositeMockObjectInfo.getType());
        assertEquals(1, thirdCompositeMockFirstCompositeMockSecondCompositeMockObjectInfo.getGetters().size());
        assertGetters(thirdCompositeMockFirstCompositeMockSecondCompositeMockObjectInfo);
        assertEquals(1, thirdCompositeMockFirstCompositeMockSecondCompositeMockObjectInfo.getSetters().size());
        assertSetters(thirdCompositeMockFirstCompositeMockSecondCompositeMockObjectInfo);
        assertEquals(1, thirdCompositeMockFirstCompositeMockSecondCompositeMockObjectInfo.getSubObjects().size());

        SingleObjectInfo thirdCompositeMockFirstCompositeMockSecondCompositeMockFirstCompositeMockObjectInfo =
                (SingleObjectInfo) thirdCompositeMockFirstCompositeMockSecondCompositeMockObjectInfo.getSubObject("firstCompositeMock");
        assertEquals(FirstCompositeMock.class, thirdCompositeMockFirstCompositeMockSecondCompositeMockFirstCompositeMockObjectInfo.getType());
        assertEquals(0, thirdCompositeMockFirstCompositeMockSecondCompositeMockFirstCompositeMockObjectInfo.getGetters().size());
        assertEquals(0, thirdCompositeMockFirstCompositeMockSecondCompositeMockFirstCompositeMockObjectInfo.getSetters().size());
        thirdCompositeMockFirstCompositeMockSecondCompositeMockFirstCompositeMockObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        thirdCompositeMockFirstCompositeMockSecondCompositeMockFirstCompositeMockObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertEquals(0, thirdCompositeMockFirstCompositeMockSecondCompositeMockFirstCompositeMockObjectInfo.getSubObjects().size());


    }

    @Test
    public void testGetArrayObjectInfo(){
        Shape[] shapes = new Shape[2];
        shapes[0] = new Circle("circle1", 1);
        shapes[1] = new Rectangle("rectangle1", 1, 2);
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(shapes);

        assertInstanceOf(ListObjectInfo.class, objectInfo);
        ListObjectInfo listObjectInfo = (ListObjectInfo) objectInfo;
        assertEquals(2, listObjectInfo.getObjectList().size());
        assertEquals(Shape[].class, listObjectInfo.getContainerClass());

        ObjectInfo circleObjectInfo = listObjectInfo.getObjectList().get(0);
        assertInstanceOf(SingleObjectInfo.class, circleObjectInfo);
        SingleObjectInfo circleSingleObjectInfo = (SingleObjectInfo) circleObjectInfo;
        assertEquals("$ROOT_OBJECT$[0]", circleSingleObjectInfo.getName());
        assertEquals(Circle.class, circleSingleObjectInfo.getType());
        assertEquals(0, circleSingleObjectInfo.getGetters().size());
        assertEquals(0, circleSingleObjectInfo.getSetters().size());
        circleSingleObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        assertEquals(0, circleSingleObjectInfo.getSubObjects().size());

        ObjectInfo rectangleObjectInfo = listObjectInfo.getObjectList().get(1);
        assertInstanceOf(SingleObjectInfo.class, rectangleObjectInfo);
        SingleObjectInfo rectangleSingleObjectInfo = (SingleObjectInfo) rectangleObjectInfo;
        assertEquals("$ROOT_OBJECT$[1]", rectangleSingleObjectInfo.getName());
        assertEquals(Rectangle.class, rectangleSingleObjectInfo.getType());
        assertEquals(0, rectangleSingleObjectInfo.getGetters().size());
        assertEquals(0, rectangleSingleObjectInfo.getSetters().size());
        rectangleSingleObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        assertEquals(0, rectangleSingleObjectInfo.getSubObjects().size());
    }

    @Test
    public void testGetListObjectInfoOfListOfList(){
        List<List<Shape>> listOfList = List.of(
                List.of(new Circle("circle1", 1), new Rectangle("rectangle1", 1, 2)),
                List.of(new Circle("circle2", 2), new Rectangle("rectangle2", 2, 3))
        );
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(listOfList);
        assertInstanceOf(ListObjectInfo.class, objectInfo);
        ListObjectInfo listObjectInfo = (ListObjectInfo) objectInfo;
        assertEquals(2, listObjectInfo.getObjectList().size());

        ObjectInfo firstListObjectInfo = listObjectInfo.getObjectList().get(0);
        assertInstanceOf(ListObjectInfo.class, firstListObjectInfo);
        ListObjectInfo firstListObjectInfoListObjectInfo = (ListObjectInfo) firstListObjectInfo;
        assertEquals(2, firstListObjectInfoListObjectInfo.getObjectList().size());

        ObjectInfo firstListFirstObjectInfo = firstListObjectInfoListObjectInfo.getObjectList().get(0);
        assertInstanceOf(SingleObjectInfo.class, firstListFirstObjectInfo);
        SingleObjectInfo firstListFirstObjectInfoSingleObjectInfo = (SingleObjectInfo) firstListFirstObjectInfo;
        assertEquals(Circle.class, firstListFirstObjectInfoSingleObjectInfo.getType());
        assertEquals(0, firstListFirstObjectInfoSingleObjectInfo.getGetters().size());
        assertEquals(0, firstListFirstObjectInfoSingleObjectInfo.getSetters().size());
        firstListFirstObjectInfoSingleObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        assertEquals(0, firstListFirstObjectInfoSingleObjectInfo.getSubObjects().size());

        ObjectInfo firstListSecondObjectInfo = firstListObjectInfoListObjectInfo.getObjectList().get(1);
        assertInstanceOf(SingleObjectInfo.class, firstListSecondObjectInfo);
        SingleObjectInfo firstListSecondObjectInfoSingleObjectInfo = (SingleObjectInfo) firstListSecondObjectInfo;
        assertEquals(Rectangle.class, firstListSecondObjectInfoSingleObjectInfo.getType());
        assertEquals(0, firstListSecondObjectInfoSingleObjectInfo.getGetters().size());
        assertEquals(0, firstListSecondObjectInfoSingleObjectInfo.getSetters().size());
        firstListSecondObjectInfoSingleObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        assertEquals(0, firstListSecondObjectInfoSingleObjectInfo.getSubObjects().size());

        ObjectInfo secondListObjectInfo = listObjectInfo.getObjectList().get(1);
        assertInstanceOf(ListObjectInfo.class, secondListObjectInfo);
        ListObjectInfo secondListObjectInfoListObjectInfo = (ListObjectInfo) secondListObjectInfo;
        assertEquals(2, secondListObjectInfoListObjectInfo.getObjectList().size());

        ObjectInfo secondListFirstObjectInfo = secondListObjectInfoListObjectInfo.getObjectList().get(0);
        assertInstanceOf(SingleObjectInfo.class, secondListFirstObjectInfo);
        SingleObjectInfo secondListFirstObjectInfoSingleObjectInfo = (SingleObjectInfo) secondListFirstObjectInfo;
        assertEquals(Circle.class, secondListFirstObjectInfoSingleObjectInfo.getType());
        assertEquals(0, secondListFirstObjectInfoSingleObjectInfo.getGetters().size());
        assertEquals(0, secondListFirstObjectInfoSingleObjectInfo.getSetters().size());
        secondListFirstObjectInfoSingleObjectInfo.getGetters().forEach(getter -> assertNull(getter.getObjectInfo()));
        secondListFirstObjectInfoSingleObjectInfo.getSetters().forEach(setter -> assertNull(setter.getObjectInfo()));
        assertEquals(0, secondListFirstObjectInfoSingleObjectInfo.getSubObjects().size());

    }

    private void assertGetters(SingleObjectInfo singleObjectInfo){
        singleObjectInfo
                .getGetters()
                .stream()
                .filter(getter -> getter.getObjectInfo() != null)
                .forEach(getter -> assertEquals(singleObjectInfo.getSubObject(getter.getObjectInfo().getName()), getter.getObjectInfo()));
    }

    private void assertSetters(SingleObjectInfo singleObjectInfo) {
        singleObjectInfo
                .getSetters()
                .stream()
                .filter(setter -> setter.getObjectInfo() != null)
                .forEach(setter -> assertEquals(singleObjectInfo.getSubObject(setter.getObjectInfo().getName()), setter.getObjectInfo()));
    }
}
