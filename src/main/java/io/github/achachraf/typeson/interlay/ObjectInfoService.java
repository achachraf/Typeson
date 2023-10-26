package io.github.achachraf.typeson.interlay;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.achachraf.typeson.domain.ListObjectInfo;
import io.github.achachraf.typeson.domain.ObjectInfo;
import io.github.achachraf.typeson.domain.ObjectInfoAccessor;
import io.github.achachraf.typeson.domain.SingleObjectInfo;
import org.slf4j.Logger;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class ObjectInfoService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(ObjectInfoService.class);

    private static final String ROOT_NAME = "$ROOT_OBJECT$";

    ObjectInfo getObjectInfo(Object object) {
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
        List<Method> getters = getNotIgnoredGetters(type);
        List<ObjectInfoAccessor> getterObjectInfoAccessors = getAccessors(object, getters, visitedObjects);
        List<ObjectInfoAccessor> setterObjectInfoAccessors = getterObjectInfoAccessors.stream()
                .map(accessor -> getSetterAccessor(accessor, type))
                .filter(Objects::nonNull)
                .toList();
        return new SingleObjectInfo()
                .setName(name)
                .setType(type)
                .setGetters(getterObjectInfoAccessors)
                .setSetters(setterObjectInfoAccessors)
                .setSubObjects(getterObjectInfoAccessors.stream()
                        .map(ObjectInfoAccessor::getObjectInfo)
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
            String elementName = name + "[" + i + "]";
            if(obj instanceof Collection<?> collection && !collection.isEmpty()){
                result.add(getObjectInfoForCollection(collection, elementName, type).setIndex(i));
            }
            else if (obj.getClass().isArray() && Array.getLength(obj) > 0){
                result.add(getObjectInfoForArray((Object[])obj, elementName, type).setIndex(i));
            }
            else{
                result.add(getObjectInfoForObject(obj, new HashSet<>(), elementName).setIndex(i));
            }
            i++;
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

    private List<Method> getNotIgnoredGetters(Class<?> type) {
        return Arrays.stream(type.getMethods())
                .filter(method -> method.getName().startsWith("get")
                        && !method.getName().equals("getClass")
                        && !method.isAnnotationPresent(JsonIgnore.class))
                .toList();
    }

    private List<Method> getSetters(Class<?> type) {
        return Arrays.stream(type.getMethods())
                .filter(method -> method.getName().startsWith("set"))
                .toList();
    }

    private ObjectInfoAccessor getSetterAccessor(ObjectInfoAccessor getterObjectInfoAccessor, Class<?> type) {
        String setterName = getterObjectInfoAccessor.getMethod().getName().replaceFirst("get", "set");
        try {
            return new ObjectInfoAccessor()
                    .setMethod(type.getMethod(setterName, getterObjectInfoAccessor.getMethod().getReturnType()))
                    .setObjectInfo(getterObjectInfoAccessor.getObjectInfo());
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    private List<ObjectInfoAccessor> getAccessors(Object object, List<Method> getters, Set<Object> visitedObjects) {
        List<ObjectInfoAccessor> result = new ArrayList<>();
        for (Method getter : getters) {
            if(isComposite(getter.getReturnType())){
                ObjectInfoAccessor objectInfoAccessor = new ObjectInfoAccessor()
                        .setMethod(getter);

                Object subObject = getObject(object, getter);
                if(subObject != null){
                    if(visitedObjects.contains(subObject)){
                        continue;
                    }
                    visitedObjects.add(subObject);
                    if(subObject instanceof Collection<?> collection){
                        if(collection.isEmpty() || !isComposite(collection.iterator().next().getClass())){
                            continue;
                        }
                        objectInfoAccessor.setObjectInfo(getObjectInfoForCollection(collection, getNameFromGetterOrAnnotation(getter), getter.getReturnType()));
                        result.add(objectInfoAccessor);
                        continue;
                    }
                    if(subObject.getClass().isArray()){
                        if(Array.getLength(subObject) == 0 || !isComposite(subObject.getClass().getComponentType())){
                            continue;
                        }
                        objectInfoAccessor.setObjectInfo(getObjectInfoForArray((Object[])subObject, getNameFromGetterOrAnnotation(getter), getter.getReturnType()));
                        result.add(objectInfoAccessor);
                        continue;
                    }
                    objectInfoAccessor.setObjectInfo(getObjectInfoForObject(subObject, visitedObjects, getNameFromGetterOrAnnotation(getter)));
                    result.add(objectInfoAccessor);
                }

            }
        }
        return result;
    }

    private Object getObject(Object object, Method getter) {
        try {
            return getter.invoke(object);
        } catch (Exception e) {
            logger.error("Error while invoking getter " + getter.getName() + " on object " + object.getClass().getName());
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
                type.isAssignableFrom(Byte.class) ||
                type.isAssignableFrom(Date.class) ||
                type.isAssignableFrom(LocalDate.class) ||
                type.isAssignableFrom(LocalDateTime.class) ||
                type.isAssignableFrom(LocalTime.class) ||
                Enum.class.isAssignableFrom(type)

        );
    }

    private String getNameFromGetterOrAnnotation(Method getter) {
        String name = getter.getName();
        return name.substring(3,4).toLowerCase() + name.substring(4);
    }




}
