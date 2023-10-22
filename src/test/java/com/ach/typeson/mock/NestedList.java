package com.ach.typeson.mock;

import java.util.List;
import java.util.Queue;
import java.util.Set;

public class NestedList {

    private List<List<Shape>> listListShapes;

    private List<Shape[]> listArrayShapes;

    private List<Set<Queue<Shape>>> allShapes;

    private Shape[][] arrayArrayShapes;

    public List<List<Shape>> getListListShapes() {
        return listListShapes;
    }

    public NestedList setListListShapes(List<List<Shape>> listListShapes) {
        this.listListShapes = listListShapes;
        return this;
    }

    public List<Shape[]> getListArrayShapes() {
        return listArrayShapes;
    }

    public NestedList setListArrayShapes(List<Shape[]> listArrayShapes) {
        this.listArrayShapes = listArrayShapes;
        return this;
    }

    public List<Set<Queue<Shape>>> getAllShapes() {
        return allShapes;
    }

    public NestedList setAllShapes(List<Set<Queue<Shape>>> allShapes) {
        this.allShapes = allShapes;
        return this;
    }

    public Shape[][] getArrayArrayShapes() {
        return arrayArrayShapes;
    }

    public NestedList setArrayArrayShapes(Shape[][] arrayArrayShapes) {
        this.arrayArrayShapes = arrayArrayShapes;
        return this;
    }
}
