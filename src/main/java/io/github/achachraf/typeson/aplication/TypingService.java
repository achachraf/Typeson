package io.github.achachraf.typeson.aplication;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Collection;

public interface TypingService {


    boolean isValue(Class<?> type);

    boolean isListOfValues(Object object);

    boolean isArray(Class<?> type);

    boolean isNumeric(Class<?> clazz);

    <T> TypeReference<ArrayList<T>> getArrayListTypeReference(Class<T> clazz);

    Collection<Object> instantiateContainer(Class<? extends Collection> containerClass);
}
