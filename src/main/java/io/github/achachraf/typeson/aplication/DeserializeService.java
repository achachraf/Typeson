package io.github.achachraf.typeson.aplication;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings({"rawtypes"})
public interface DeserializeService {

    <T> T deserialize(String source, Class<? extends T> type);

    <T extends Collection> T deserializeArray(String source, TypeReference<T> typeReference);

    <T> ArrayList<T> deserializeArray(String source, Class<T> type);
}
