package io.github.achachraf.typeson.interlay.mock;

public class PrimitivesHolder {

    private int[] ints;

    private Integer[] integers;

    private String[] strings;

    private byte[] bytes;

    private Byte[] bytesObjects;

    public int[] getInts() {
        return ints;
    }

    public PrimitivesHolder setInts(int[] ints) {
        this.ints = ints;
        return this;
    }

    public Integer[] getIntegers() {
        return integers;
    }

    public PrimitivesHolder setIntegers(Integer[] integers) {
        this.integers = integers;
        return this;
    }

    public String[] getStrings() {
        return strings;
    }

    public PrimitivesHolder setStrings(String[] strings) {
        this.strings = strings;
        return this;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public PrimitivesHolder setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    public Byte[] getBytesObjects() {
        return bytesObjects;
    }

    public PrimitivesHolder setBytesObjects(Byte[] bytesObjects) {
        this.bytesObjects = bytesObjects;
        return this;
    }
}
