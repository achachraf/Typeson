package org.ach.typeler;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

public class ObjectInfoServiceSerializer {

    private static final String ROOT_NAME = "$ROOT_OBJECT$";

    public ObjectInfo getObjectInfo(Object object) {
        if(object.getClass().isArray() && Array.getLength(object) > 0){
            return getObjectInfoForArray((Object[])object);
        }
        if(object instanceof Collection<?> collection && !collection.isEmpty()){
            return getObjectInfoForCollection(collection, ROOT_NAME);
        }
        return getObjectInfoForObject(object, new HashSet<>(), ROOT_NAME);

    }



    private ObjectInfo getObjectInfoForObject(Object object, Set<Object> visitedObjects, String name) {
        if(object == null){
            throw new IllegalArgumentException("Object cannot be null");
        }
        Class<?> type = object.getClass();
        List<Method> getters = getGetters(type);
        List<Accessor> getterAccessors = getAccessors(object, getters, visitedObjects);
        List<Accessor> setterAccessors = getterAccessors.stream()
                .map(accessor -> getSetterAccessor(accessor, type))
                .toList();
        return new SingleObjectInfo()
                .setName(name)
                .setType(type)
                .setGetters(getterAccessors)
                .setSetters(setterAccessors)
                .setSubObjects(getterAccessors.stream()
                        .map(Accessor::getObjectInfo)
                        .filter(Objects::nonNull)
                        .toList())
                ;

    }

    private ObjectInfo getObjectInfoForCollection(Collection<?> object, String name) {
        return getObjectInfoForCollection(object, name, object.getClass());
    }

    private ObjectInfo getObjectInfoForCollection(Collection<?> object, String name, Class<?> type){
        List<ObjectInfo> result = new ArrayList<>();
        int i = 0;
        for (Object obj : object) {
            String elementName = name + "[" + i++ + "]";
            if(obj instanceof Collection<?> collection && !collection.isEmpty()){
                result.add(getObjectInfoForCollection(collection, elementName, type));
            }
            else if (obj.getClass().isArray() && Array.getLength(obj) > 0){
                result.add(getObjectInfoForArray((Object[])obj, elementName, type));
            }
            else{
                result.add(getObjectInfoForObject(obj, new HashSet<>(), name + "[" + i++ + "]"));
            }
        }
        return new ListObjectInfo()
                .setName(name)
                .setType(object.getClass())
                .setObjectList(result)
                .setContainerClass(type)
                ;
    }

    private ObjectInfo getObjectInfoForArray(Object[] object) {
        return getObjectInfoForCollection(Arrays.asList(object), ROOT_NAME, object.getClass());
    }

    private ObjectInfo getObjectInfoForArray(Object[] object, String name, Class<?> type) {
        return getObjectInfoForCollection(Arrays.asList(object), name, type);
    }

    private List<Method> getGetters(Class<?> type) {
        return Arrays.stream(type.getMethods())
                .filter(method -> method.getName().startsWith("get") && !method.getName().equals("getClass"))
                .toList();
    }

    private List<Method> getSetters(Class<?> type) {
        return Arrays.stream(type.getMethods())
                .filter(method -> method.getName().startsWith("set"))
                .toList();
    }

    private Accessor getSetterAccessor(Accessor getterAccessor, Class<?> type) {
        String setterName = getterAccessor.getMethod().getName().replaceFirst("get", "set");
        try {
            return new Accessor()
                    .setMethod(type.getMethod(setterName, getterAccessor.getMethod().getReturnType()))
                    .setObjectInfo(getterAccessor.getObjectInfo());
        } catch (NoSuchMethodException e) {
            throw new TypelerException("Could not find setter: " + setterName, e);
        }
    }
    private List<Accessor> getAccessors(Object object, List<Method> getters, Set<Object> visitedObjects) {
        List<Accessor> result = new ArrayList<>();
        for (Method getter : getters) {
            Accessor accessor = new Accessor()
                    .setMethod(getter);
            result.add(accessor);
            if(isComposite(getter.getReturnType())){
                Object subObject = getObject(object, getter);
                if(subObject != null){
                    if(visitedObjects.contains(subObject)){
                        continue;
                    }
                    visitedObjects.add(subObject);
                    if(subObject instanceof Collection<?> collection){
                        accessor.setObjectInfo(getObjectInfoForCollection(collection, getNameFromGetter(getter), getter.getReturnType()));
                        continue;
                    }
                    accessor.setObjectInfo(getObjectInfoForObject(subObject, visitedObjects, getNameFromGetter(getter)));
                }
            }
        }
        return result;
    }

    private Object getObject(Object object, Method getter) {
        try {
            return getter.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isComposite(Class<?> type) {
        return  !(type.isPrimitive() ||
                type.isAssignableFrom(String.class) ||
                type.isAssignableFrom(Character.class) ||
                type.isAssignableFrom(Boolean.class) ||
                type.isAssignableFrom(Integer.class) ||
                type.isAssignableFrom(Long.class) ||
                type.isAssignableFrom(Double.class) ||
                type.isAssignableFrom(Float.class) ||
                type.isAssignableFrom(Short.class) ||
                type.isAssignableFrom(Void.class) ||
                type.isAssignableFrom(Byte.class));
    }

    private String getNameFromGetter(Method getter) {
        String name = getter.getName();
        return name.substring(3,4).toLowerCase() + name.substring(4);
    }

    private record TwoAccessors(Accessor getter, Accessor setter, ObjectInfo objectInfo){}



}
