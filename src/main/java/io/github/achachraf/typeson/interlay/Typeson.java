package io.github.achachraf.typeson.interlay;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.achachraf.typeson.TypesonException;
import io.github.achachraf.typeson.aplication.DeserializeService;
import io.github.achachraf.typeson.aplication.SerializerService;

import java.util.Collection;
import java.util.List;

/**
 * Typeson is the main class of the library, used to marshall and unmarshall objects.
 * For Unmarshalling, it uses {@link DeserializeService} and based on the {@link io.github.achachraf.typeson.domain.ElementType} annotation it scans and resolve the type of the object.
 * For Marshalling, it uses {@link SerializerService} and based on the {@link io.github.achachraf.typeson.domain.ElementType} annotation it injects the type information in the json string.
 * @see DeserializeService for more information about unmarshalling
 * @see SerializerService for more information about marshalling
 */
public class Typeson {

    private final SerializerService serializerService;

    private final DeserializeService deserializeService;

    /**
     * Default constructor
     * Uses the default ObjectMapper with all modules registered
     */
    public Typeson() {
        this(new ObjectMapper().findAndRegisterModules());
    }

    /**
     * Constructor with custom ObjectMapper
     * @param objectMapper Underlying ObjectMapper to use for serialization or deserialization.
     */
    public Typeson(ObjectMapper objectMapper) {
        this.deserializeService = new DeserializerServiceClassGraph(objectMapper);
        this.serializerService = new SerializerServiceImpl(objectMapper);
    }

    /**
     * Unmarshall a json string to an object
     * @param data json string to unmarshall
     * @param clazz class of the object to unmarshall
     * @return the unmarshalled object
     * @param <T> type of the object to unmarshall
     * @throws TypesonException @see {@link DeserializeService#deserialize(String, Class)}
     */
    public <T> T unmarshall(String data, Class<T> clazz) {
        try {
            return deserializeService.deserialize(data, clazz);
        }
        catch (Exception e) {
            throw new TypesonException("Error while unmarshalling object: ",e);
        }
    }

    /**
     * Unmarshall a json string to an object
     * @param data json string to unmarshall
     * @param clazz type reference of the object to unmarshall
     * @return the unmarshalled object
     * @param <T> type of the object to unmarshall
     * @throws TypesonException @see {@link DeserializeService#deserializeArray(String, Class)}
     */
    public <T> List<? extends T> unmarshallList(String data, Class<T> clazz) {
        try {
            return deserializeService.deserializeArray(data, clazz);
        }
        catch (Exception e) {
            throw new TypesonException("Error while unmarshalling List: ",e);
        }
    }

    /**
     * Unmarshall a json string to an object
     * @param data json string to unmarshall
     * @param typeReference type reference of the object to unmarshall
     * @return the unmarshalled object
     * @param <T> type of the object to unmarshall
     * @throws TypesonException @see {@link DeserializeService#deserializeArray(String, TypeReference)}
     */
    @SuppressWarnings("rawtypes")
        public <T extends Collection> T unmarshallList(String data, TypeReference<T> typeReference) {
        try {
            return deserializeService.deserializeArray(data, typeReference);
        }
        catch (Exception e) {
            throw new TypesonException("Error while unmarshalling List: ",e);
        }
    }

    /**
     * Marshall an object to a json string
     * @param object object to marshall
     * @return the marshalled json string
     * @throws TypesonException @see {@link SerializerService#serialize(Object)}
     */
    public String marshall(Object object) {
        try {
            return serializerService.serialize(object);
        }
        catch (Exception e) {
            throw new TypesonException("Error while marshalling object: ",e);
        }
    }



}
