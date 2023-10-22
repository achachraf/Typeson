package com.ach.typeson.interlay;

import com.ach.typeson.aplication.DeserializeService;
import com.ach.typeson.aplication.SerializerService;

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
