package io.github.achachraf.typeson.interlay;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.achachraf.typeson.aplication.TypingException;
import io.github.achachraf.typeson.aplication.TypingService;
import org.slf4j.Logger;

import java.lang.reflect.*;
import java.util.*;

public class TypingServiceImpl implements TypingService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TypingServiceImpl.class);

    @Override
    public boolean isValue(Class<?> type) {
        return type.isPrimitive() ||
                type.isAssignableFrom(String.class) ||
                type.isAssignableFrom(Character.class) ||
                type.isAssignableFrom(Boolean.class) ||
                type.isAssignableFrom(Integer.class) ||
                type.isAssignableFrom(Long.class) ||
                type.isAssignableFrom(Double.class) ||
                type.isAssignableFrom(Float.class) ||
                type.isAssignableFrom(Short.class) ||
                type.isAssignableFrom(Void.class) ||
                type.isAssignableFrom(Byte.class);
    }

    @Override
    public boolean isListOfValues(Object object) {
        if(object instanceof Collection<?> collection){
            return !collection.isEmpty() && isValue(collection.iterator().next().getClass());
        }
        if(object.getClass().isArray()){
            return Array.getLength(object) != 0 && isValue(object.getClass().getComponentType());
        }
        return false;
    }


    private boolean isArrayOfValues(Class<?> type) {
        return type.isArray() && isValue(type.getComponentType());
    }

    @Override
    public boolean isArray(Class<?> type) {
        return type.isArray() || Collection.class.isAssignableFrom(type);
    }

    @Override
    public boolean isNumeric(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz) ||
                clazz == byte.class ||
                clazz == short.class ||
                clazz == int.class ||
                clazz == long.class ||
                clazz == float.class ||
                clazz == double.class;
    }

    @Override
    public <T> TypeReference<ArrayList<T>> getArrayListTypeReference(Class<T> clazz) {
        return new ArrayListTypeReference<>(clazz);
    }

    @Override
    public Collection<Object> instantiateContainer(Class<? extends Collection> containerClass) {
        if(!Modifier.isAbstract(containerClass.getModifiers()) && !Modifier.isInterface(containerClass.getModifiers())
                && Collection.class.isAssignableFrom(containerClass)){
            try {
                return (Collection<Object>) containerClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error("cannot instantiate containerClass: "+containerClass+" using default implementation", e);
            }
        }
        if(List.class.isAssignableFrom(containerClass)){
            return new ArrayList<>();
        }
        if(Set.class.isAssignableFrom(containerClass)){
            return new HashSet<>();
        }
        if(Queue.class.isAssignableFrom(containerClass)){
            return new LinkedList<>();
        }
        if(Deque.class.isAssignableFrom(containerClass)){
            return new ArrayDeque<>();
        }
        throw new TypingException("containerClass is not supported: "+containerClass);

    }

    private static class ArrayListTypeReference<T> extends TypeReference<ArrayList<T>> {

        private final Type type;

        public ArrayListTypeReference(Type type) {
            this.type = type;
        }

        @Override
        public Type getType() {
            return new ParameterizedType() {
                @Override
                public Type[] getActualTypeArguments() {
                    return new Type[]{type};
                }

                @Override
                public Type getRawType() {
                    return ArrayList.class;
                }

                @Override
                public Type getOwnerType() {
                    return null;
                }
            };
        }
    }
}
