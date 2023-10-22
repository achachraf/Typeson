package io.github.achachraf.typeson.aplication;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings({"rawtypes"})
public interface DeserializeService {

    /**
     * Deserialize a json string to an object of type T
     * @param source json string
     * @param type type of the object to deserialize
     * @return an object of type T
     * @param <T> type of the object to deserialize
     * @throws IllegalArgumentException if:
     * <ul>
     *     <li>The source is not a valid json object string</li>
     *     <li>A field is a generic type</li>
     *     <li>A JSON array is provided but the type is not a collection</li>
     *     <li>Unsupported JSON type is provided (e.g. value)</li>
     * </ul>
     * @throws TypingException if a type violation occurs
     * @throws UnexpectedFieldException if a field is not found in the provided type
     * @throws DeserializationException if the deserialization fails
     */
    <T> T deserialize(String source, Class<? extends T> type);

    /**
     * Deserialize a json string to an object of type T
     * @param source json string
     * @param typeReference type of the object to deserialize
     * @return an object of type T that is a collection
     * @param <T> type of the object to deserialize
     * @throws IllegalArgumentException if
     * <ul>
     *     <li>TypeReference is null or source is null or not a valid json array string</li>
     *     <li>Provided type reference collection has more or less than one type parameter</li>
     * </ul>
     * @throws IllegalStateException if
     * <ul>
     *  <li>JSON array is not homogeneous</li>
     *  <li>Unexpected error during generics handling</li>
     * </ul>
     * @throws DeserializationException if the deserialization fails
     * @see #deserialize(String, Class) for more possible exceptions
     */
    <T extends Collection> T deserializeArray(String source, TypeReference<T> typeReference);

    /**
     * Deserialize a json string to an object of type T
     * @param source json string
     * @param type type of the object to deserialize
     * @return an {@link ArrayList} of type T
     * @param <T> type of the object to deserialize
     * @throws IllegalArgumentException if type is null or is an instance of {@link Collection}
     * @see #deserializeArray(String, TypeReference) for more possible exceptions
     */
    <T> ArrayList<T> deserializeArray(String source, Class<T> type);
}
