package org.ach.typeson.mock;

import java.util.Collection;
import java.util.Iterator;

public class CollectionMultipleTypes<T,R extends T> implements Collection<T> {

    private Collection<T> collection;

    private Collection<R> clones;

    public CollectionMultipleTypes() {
        this.collection = new CustomCollection<>();
        this.clones = new CustomCollection<>();
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return collection.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return collection.iterator();
    }

    @Override
    public Object[] toArray() {
        return collection.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return collection.toArray(a);
    }

    @Override
    public boolean add(T t) {
        clones.add((R) t);
        return collection.add(t);
    }

    @Override
    public boolean remove(Object o) {
        clones.remove(o);
        return collection.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return collection.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        clones.addAll((Collection<? extends R>) c);
        return collection.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        clones.removeAll(c);
        return collection.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }


}
