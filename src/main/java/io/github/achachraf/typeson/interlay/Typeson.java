package io.github.achachraf.typeson.interlay;

import io.github.achachraf.typeson.aplication.DeserializeService;
import io.github.achachraf.typeson.aplication.SerializerService;

import java.util.List;

public class Typeson {

    private final SerializerService serializerService = new SerializerServiceImpl();

    private final DeserializeService deserializeService = new DeserializerServiceClassGraph(new ConfigProviderMapImpl());

    public <T> T unmarshall(String data, Class<T> clazz) {
        return deserializeService.deserialize(data, clazz);
    }

    public <T> List<? extends T> unmarshallList(String data, Class<T> clazz) {
        return deserializeService.deserializeArray(data, clazz);
    }

    public String marshall(Object object) {
        return serializerService.serialize(object);
    }



}
