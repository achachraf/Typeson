package org.ach.typeler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.classgraph.*;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Typeler {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(Typeler.class);
    private static final String PARAMETER_NAME = "name";

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static ScanResult scanResult;

    public <T> T unmarshall(String data, Class<T> clazz) {
        assertFields(data, clazz);

        TypeInfo typeInfo = getTypeInfo(data, clazz);
        if(typeInfo.jsonNode.isArray()){
            throw new TypelerException(data + " is not an object");
        }
        return unmarshallObject(typeInfo.jsonNode, typeInfo.typeFieldName,
                typeInfo.classInfoList);

    }

    public <T> List<? extends T> unmarshallList(String data, Class<T> clazz) {
        assertFields(data, clazz);
        TypeInfo typeInfo = getTypeInfo(data, clazz);
        if(!typeInfo.jsonNode.isArray()){
            throw new TypelerException(data + " is not a list");
        }
        return unmarshallList(typeInfo.jsonNode, typeInfo.typeFieldName,
                typeInfo.keepField, typeInfo.classInfoList, new ArrayList<>());

    }

    public String marshall(Object object) {
        return null;
//        if(object.getClass().isArray() || object instanceof Collection<?>){
//            return marshallList(object);
//        }
//        else if(object.getClass().isPrimitive()){
//            throw new TypelerException("Primitive types are not supported");
//        }
//        return marshallObject(object);
//
//        ElementType elementType = object.getClass().getAnnotation(ElementType.class);
//        if(elementType == null){
//            throw new TypelerException("ElementType annotation not found on class: " + object.getClass().getName());
//        }
//        try {
//            boolean keepField = elementType.keepField();
//
//            if(keepField){
//                String typeFieldName = object.getClass().getAnnotation(ElementType.class).field();
//                if(treeNode.isObject()){
//                    ((ObjectNode) treeNode).put(typeFieldName, elementType.name());
//                }
//                else if(treeNode.isArray()){
//                    for (JsonNode node : treeNode) {
//                        ((ObjectNode) node).put(typeFieldName, elementType.name());
//                    }
//                }
//            }
//            return objectMapper.writeValueAsString(treeNode);
//        } catch (Exception e) {
//            throw new TypelerException("Error while marshalling object: "+object.getClass(), e);
//        }
    }

    private <T> T unmarshallObject(JsonNode jsonNode, String typeFieldName, ClassInfoList classInfoList){
        try {
            JsonNode typeNode = jsonNode.get(typeFieldName);
            if (typeNode == null) {
                throw new TypelerException("Type field not found: " + typeFieldName);
            }
            String type = typeNode.asText();
            ((ObjectNode) jsonNode).remove(typeFieldName);
            Class<?> typeClass = getTypeClass(type, classInfoList);
            T object = (T) typeClass.getDeclaredConstructor().newInstance();
            jsonNode.fields().forEachRemaining(field -> {
                try {
                    String fieldName = field.getKey();
                    JsonNode fieldValue = field.getValue();
                    Method method = typeClass.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), fieldValue.getClass());
                    if(fieldValue.isObject()){
                        method.invoke(object, unmarshallObject(fieldValue, typeFieldName, classInfoList));
                    }
                    else if(fieldValue.isArray()){
                        method.invoke(object, unmarshallList(fieldValue, typeFieldName, false, classInfoList, new ArrayList<>()));
                    }
                    else{
                        method.invoke(object, fieldValue.asText());
                    }
                } catch (Exception e) {
                    throw new TypelerException("Error while setting field: " + field.getKey(), e);
                }
            });
        } catch (Exception e) {
            throw new TypelerException(e);
        }
        return null;
    }

    private <T> List<? extends T> unmarshallList(JsonNode jsonNode, String typeFieldName,boolean keepField, ClassInfoList classInfoList, List<Object> list){
        try {
            for (JsonNode node : jsonNode) {
                if(node.isObject()){
                    list.add(unmarshallObject(node, typeFieldName, classInfoList));
                }
                else if(node.isArray()){
                    list.add(unmarshallList(node, typeFieldName, keepField, classInfoList, list));
                }
                else{
                    list.add(node.asText());
                }
            }
            return (List<? extends T>) list;
        } catch (Exception e) {
            throw new TypelerException("Could not unmarshall array",e);
        }
    }

//    private void injectTypeField(JsonNode jsonNode, Object object){
//        ElementType elementType = object.getClass().getAnnotation(ElementType.class);
//        if(elementType == null){
//            throw new TypelerException("ElementType annotation not found on class: " + object.getClass().getName());
//        }
//        try {
//            boolean keepField = elementType.keepField();
//            if(keepField){
//                String typeFieldName = object.getClass().getAnnotation(ElementType.class).field();
//                if(jsonNode.isObject()){
//                    ((ObjectNode) jsonNode).put(typeFieldName, elementType.name());
//                }
//            }
//        } catch (Exception e) {
//            throw new TypelerException("Error while injecting type field in object: "+object.getClass(), e);
//        }
//    }
//
//    private String marshallObject(Object object){
//        try {
//            JsonNode jsonNode = objectMapper.valueToTree(object);
//            injectTypeField(jsonNode, object);
//            return objectMapper.writeValueAsString(injectTypeField(jsonNode, object));
//        } catch (JsonProcessingException e) {
//            throw new TypelerException("Error while marshalling object: "+object.getClass(),e);
//        }
//    }
//
//    private String marshallList(Object object){
//        ArrayNode arrayNode = objectMapper.valueToTree(object);
//        for (JsonNode jsonNode : arrayNode) {
//
//            injectTypeField(jsonNode, object);
//        }
//    }

    private Class<?> getTypeClass(String type, ClassInfoList classInfoList) throws ClassNotFoundException {
        for (ClassInfo classInfo : classInfoList) {
            AnnotationInfo annotationInfo = classInfo.getAnnotationInfo().get(ElementType.class.getName());
            if(annotationInfo == null){
                logger.error(classInfo.getName() + " is not annotated with ElementType, it will be ignored");
                continue;
            }
            String name = annotationInfo.getParameterValues().getValue(PARAMETER_NAME).toString();
            if (name.equalsIgnoreCase(type)) {
                return Class.forName(classInfo.getName());
            }
        }
        throw new TypelerException("Type not found: " + type);
    }

    private void scanClassPath(){
        scanResult = new ClassGraph()
            .enableAllInfo()
            .scan();
    }

    private <T> TypeInfo getTypeInfo(String data, Class<T> clazz) {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            throw new TypelerException(data+" is not a valid JSON",e);
        }
        if(scanResult == null ){
            scanClassPath();
        }
        ClassInfoList classInfoList = scanResult.getSubclasses(clazz.getName());
        if(classInfoList.isEmpty()){
            logger.warn("No subclasses found for class: " + clazz.getName());
        }
        // add the class itself
        classInfoList.add(scanResult.getClassInfo(clazz.getName()));

        ElementType elementType = clazz.getAnnotation(ElementType.class);
        if(elementType == null){
            throw new TypelerException(clazz.getName() + " is not annotated with @ElementType");
        }
        String typeFieldName = clazz.getAnnotation(ElementType.class).field();
        boolean keepField = clazz.getAnnotation(ElementType.class).keepField();
        return new TypeInfo(jsonNode, classInfoList, typeFieldName, keepField);
    }

    private void assertFields(String data, Class<?> clazz){
        if(data == null){
            throw new IllegalArgumentException("Json String is null");
        }
        if(clazz == null){
            throw new IllegalArgumentException("Class type is null");
        }
    }

    private record TypeInfo(JsonNode jsonNode, ClassInfoList classInfoList, String typeFieldName, boolean keepField) {
    }



}
