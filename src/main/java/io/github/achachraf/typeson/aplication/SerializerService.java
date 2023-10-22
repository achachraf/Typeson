package io.github.achachraf.typeson.aplication;

public interface SerializerService {

    /**
     * Serialize an object to a string and inject the type information<br>
     * The type information is injected as a field based on the annotation {@link io.github.achachraf.typeson.domain.ElementType}
     * @param object the object to serialize
     * @return the serialized object as json string
     * @throws SerializationException if
     * <ul>
     *     <li>The object cannot be serialized</li>
     *     <li>Type name is not specified in ElementType</li>
     *     <li>Type field not present in JSON</li>
     *     <li>Unexpected error during serialization</li>
     * </ul>
     * @throws IllegalArgumentException if
     * <ul>
     *     <li>Object is null</li>
     *     <li>Object is an instance of {@link java.util.Collection}</li>
     *     <li>Unexpected ObjectInfo Service error</li>
     * </ul>
     */
    String serialize(Object object);

}
