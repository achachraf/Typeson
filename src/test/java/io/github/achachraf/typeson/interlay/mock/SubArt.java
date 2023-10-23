package io.github.achachraf.typeson.interlay.mock;

import io.github.achachraf.typeson.domain.ElementType;

import java.util.List;

@ElementType(name = "subArt", field = "type")
public class SubArt extends Art{

    private int price;

    private List<String> strings;

    public int getPrice() {
        return price;
    }

    public SubArt setPrice(int price) {
        this.price = price;
        return this;
    }

    public List<String> getStrings() {
        return strings;
    }

    public SubArt setStrings(List<String> strings) {
        this.strings = strings;
        return this;
    }

    @Override
    public SubArt setShapes(List<Shape> shapes) {
        super.setShapes(shapes);
        return this;
    }

    @Override
    public String toString() {
        return "SubArt{" +
                "price=" + price +
                "strings=" + strings +
                "'} "+super.toString();
    }
}
