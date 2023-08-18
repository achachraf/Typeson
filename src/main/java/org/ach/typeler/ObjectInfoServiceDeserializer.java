package org.ach.typeler;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.lang.reflect.InvocationTargetException;

public class ObjectInfoServiceDeserializer  {

    public ObjectInfo getObjectInfo(String source, Class<?> type) {
        return null;
    }

    public ObjectInfo getObjectInfo(TreeNode treeNode, Class<?> type) {
        if(treeNode.isObject()) {
            JsonNode jsonNode = (JsonNode) treeNode;
            if (!type.isAnnotationPresent(ElementType.class)) {
                throw new IllegalArgumentException("type is not annotated with ElementType");
            }
            ElementType elementType = type.getAnnotation(ElementType.class);
            String typeField = elementType.field();
            String typeName = elementType.name();
            if (!jsonNode.has(typeField) || !jsonNode.get(typeField).isTextual() || !jsonNode.get(typeField).asText().equals(typeName)) {
                throw new IllegalArgumentException("source does not contain textual field: " + typeField);
            }
            return mapObject(jsonNode, type);
        }
        else if(treeNode.isArray()){
            ArrayNode arrayNode = (ArrayNode) treeNode;
            return mapArray(arrayNode, listObjectInfo);
        }
        else{
            throw new IllegalArgumentException("treeNode is not JsonNode or ArrayNode");
        }
    }

    private SingleObjectInfo mapObject(JsonNode jsonNode, Class<?> type) {
        ObjectInfo objectInfo = new SingleObjectInfo();
        objectInfo.setType(type);
        for (Method method : type.getMethods()) {
            if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
                String fieldName = method.getName().substring(3);
                fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                if (jsonNode.has(fieldName)) {
                    try {
                        method.invoke(objectInfo, jsonNode.get(fieldName).asText());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }
}
