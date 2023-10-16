package org.ach.typeson.interlay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ach.typeson.aplication.SerializerService;
import org.ach.typeson.domain.ElementType;
import org.ach.typeson.domain.ListObjectInfo;
import org.ach.typeson.domain.ObjectInfo;
import org.ach.typeson.domain.SingleObjectInfo;
import org.slf4j.Logger;

public class SerializerServiceImpl implements SerializerService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SerializerServiceImpl.class);

    private final ObjectInfoService objectInfoService = new ObjectInfoService();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String serialize(Object object){
        ObjectInfo objectInfo = objectInfoService.getObjectInfo(object);
        JsonNode jsonNode = objectMapper.valueToTree(object);
        insertType(jsonNode, objectInfo);
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertType(JsonNode jsonNode, ObjectInfo objectInfo){
        if(objectInfo instanceof SingleObjectInfo singleObjectInfo){
            insertTypeInObject((ObjectNode) jsonNode, singleObjectInfo);
        }
        else if(objectInfo instanceof ListObjectInfo listObjectInfo){
            insertTypeInArray((ArrayNode) jsonNode, listObjectInfo);
        }
        else{
            throw new IllegalArgumentException("objectInfo is not SingleObjectInfo or ListObjectInfo");
        }
    }

    private void insertTypeInObject(ObjectNode objectNode, SingleObjectInfo singleObjectInfo){
        if(!objectNode.isObject()){
            throw new IllegalArgumentException("jsonNode is not ObjectNode");
        }
        Class<?> type = singleObjectInfo.getType();
        if(!type.isAnnotationPresent(ElementType.class)){
            logger.warn("type is not annotated with ElementType, not inserting the type field for type: {}", type.getCanonicalName());
        }
        else {
            ElementType elementType = type.getAnnotation(ElementType.class);
            String typeField = elementType.field();
            String typeName = elementType.name();
            objectNode.put(typeField, typeName);
        }
        for (ObjectInfo objectInfo : singleObjectInfo.getSubObjects()){
            if(!objectNode.has(objectInfo.getName())){
                throw new IllegalArgumentException("jsonNode does not contain field: "+objectInfo.getName());
            }
            JsonNode subJsonNode = objectNode.get(objectInfo.getName());
            insertAnyType(objectInfo, subJsonNode);
        }
    }

    private void insertTypeInArray(ArrayNode arrayNode, ListObjectInfo listObjectInfo){
        if(!arrayNode.isArray()){
            throw new IllegalArgumentException("jsonNode is not ArrayNode");
        }
        for(ObjectInfo objectInfo : listObjectInfo.getObjectList()){
            int index = objectInfo.getIndex();
            if(index == -1){
                throw new IllegalArgumentException("not allowed context, index is -1");
            }
            if(index >= arrayNode.size()){
                throw new IllegalArgumentException("index is out of bounds");
            }
            JsonNode subJsonNode = arrayNode.get(index);
            insertAnyType(objectInfo, subJsonNode);

        }

    }

    private void insertAnyType(ObjectInfo objectInfo, JsonNode subJsonNode){
        if(objectInfo instanceof SingleObjectInfo subSingleObjectInfo){
            if(!subJsonNode.isObject()){
                throw new IllegalArgumentException("jsonNode is not ObjectNode");
            }
            insertTypeInObject((ObjectNode) subJsonNode, subSingleObjectInfo);
        }
        else if(objectInfo instanceof ListObjectInfo subListObjectInfo){
            if(!subJsonNode.isArray()){
                throw new IllegalArgumentException("jsonNode is not ArrayNode");
            }
            insertTypeInArray((ArrayNode) subJsonNode, subListObjectInfo);
        }
        else{
            throw new IllegalArgumentException("objectInfo is not SingleObjectInfo or ListObjectInfo");
        }
    }
}
