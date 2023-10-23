package io.github.achachraf.typeson.interlay;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ValueNode;
import io.github.achachraf.typeson.aplication.*;
import io.github.achachraf.typeson.domain.ElementType;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DeserializerServiceClassGraph implements DeserializeService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DeserializerServiceClassGraph.class);

    private static ScanResult scanResult;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ConfigProvider configProvider;

    private final TypingService typingService = new TypingServiceImpl();

    private final Map<Class<?>, Map<String, Method>> propertiesMap = new HashMap<>();

    public DeserializerServiceClassGraph(ConfigProvider configProvider) {
        this.configProvider = configProvider;
    }

    @Override
    public <T> T deserialize(String source, Class<? extends T> type) {
        Objects.requireNonNull(type, "Type cannot be null");
        Objects.requireNonNull(source, "Source cannot be null");
        try {
            JsonNode treeNode = objectMapper.readTree(source);
            if(!treeNode.isObject()){
                throw new IllegalArgumentException("Source is not Json Object: "+source);
            }
            return mapObject(treeNode, type);
        } catch (JsonProcessingException e) {
            throw new DeserializationException("Error when processing Json Object: "+source, e);
        }
    }

    @Override
    public <T extends Collection> T deserializeArray(String source,TypeReference<T> typeReference) {
        Objects.requireNonNull(typeReference, "TypeReference cannot be null");
        Objects.requireNonNull(source, "Source cannot be null");
        Type containerType = typeReference.getType();
        if(containerType instanceof ParameterizedType containerParameterizedType){
            if(containerParameterizedType.getActualTypeArguments().length == 1){
                try {
                    JsonNode treeNode = objectMapper.readTree(source);
                    if(!treeNode.isArray()){
                        throw new IllegalArgumentException("Source is not a Json Array: "+source);
                    }
                    return (T) mapArray((ArrayNode) treeNode, containerParameterizedType);
                } catch (JsonProcessingException e) {
                    throw new DeserializationException("Error when processing Json Array: "+source, e);
                }
            }
            else{
                throw new IllegalArgumentException("Expected 1 generic type for collection, found: "+containerParameterizedType.getActualTypeArguments().length);
            }
        }
        else{
            throw new IllegalArgumentException("Collection not provided with generic type: "+containerType.getTypeName());
        }
    }

    @Override
    public <T> ArrayList<T> deserializeArray(String source, Class<T> type) {
        Objects.requireNonNull(type, "Type cannot be null");
        if(Collection.class.isAssignableFrom(type)){
            throw new IllegalArgumentException("Type cannot be a Collection, use TypeReference instead");
        }
        return deserializeArray(source, typingService.getArrayListTypeReference(type));
    }

    private <T> T mapObject(JsonNode jsonNode, Class<?> type){
        if(type.getTypeParameters().length > 0){
            throw new IllegalArgumentException("Field " + type + " is a generic, and it is not supported yet");
        }

        Class<?> typeClass;
        String typeField;
        if (!type.isAnnotationPresent(ElementType.class)) {
            typeField = null;
            typeClass = type;
            logger.warn("type {} not annotated with ElementType, using provided type as implementation", type.getCanonicalName());
        }
        else {
            ElementType elementType = type.getAnnotation(ElementType.class);
            typeField = elementType.field();

            if (!jsonNode.has(typeField) || !jsonNode.get(typeField).isTextual() ) {
                typeClass = type;
                logger.warn("type field {} not found or not string, using provided type {} as implementation", typeField, type.getCanonicalName());

            }
            else{
                typeClass = getTypeClass(jsonNode.get(typeField).asText(), type);
            }
        }
        Map<String, Method> setters = extractSetters(typeClass);
        setters.remove(typeField); // remove type field from setters
        if(configProvider.getProperty(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)){
            checkUnknownProperties(jsonNode, setters, typeField, typeClass);
        }
        T object ;
        try {
            object = (T) typeClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new DeserializationException("Cannot instantiate object of type: "+typeClass,e);
        }
        for(Map.Entry<String, Method> methodEntry : setters.entrySet()){
            String fieldName = methodEntry.getKey();
            Method method = methodEntry.getValue();
            JsonNode fieldNode = jsonNode.get(fieldName);
            if(fieldName.equals(typeField) || isIgnored(method)){ // escape processing of "type" field && ignored fields
                continue;
            }
            if(fieldNode == null){
                if(configProvider.getProperty(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)){
                    throw new DeserializationException("Null value for primitive field: "+fieldName);
                }
            }
            else{
                mapAnyType(object, fieldNode, fieldName, method);
            }
        }
        return object;

    }

    private <T> void mapAnyType(T object, JsonNode fieldNode, String fieldName, Method method){
        Class<?> fieldType = method.getParameterTypes()[0];
        assertFieldType(fieldNode, fieldType, fieldName);
        try{
            if(fieldNode.isValueNode() && !fieldNode.isNull()){
                ValueNode valueNode = (ValueNode) fieldNode;
                assertValueNodeType(valueNode, fieldType, fieldName);
                method.invoke(object, getFieldNodeValue(valueNode, fieldType));
            }
            else if(fieldNode.isObject()){
                Object value = mapObject(fieldNode, method.getParameterTypes()[0]);
                method.invoke(object, value);
            }
            else if(fieldNode.isArray()){
                ArrayNode arrayNode = (ArrayNode) fieldNode;
                if (arrayNode.size() > 0) {
                    if(Collection.class.isAssignableFrom(fieldType)){
                        System.out.println("field: " +fieldName);
                        System.out.println("method: "+method);
                        System.out.println("Method class: "+method.getDeclaringClass());
                        System.out.println("containerType: "+method.getParameters()[0].getParameterizedType());
                        System.out.println("containerType class: "+method.getParameters()[0].getParameterizedType().getClass());
                        ParameterizedType containerParameterizedType = (ParameterizedType) method.getParameters()[0].getParameterizedType();
                        Collection<?> collection = mapJavaCollection(arrayNode, containerParameterizedType);
                        method.invoke(object, collection);
                    }
                    else if(fieldType.isArray()){
                        Class<?> arrayElementType = fieldType.getComponentType();
                        Object array = mapJavaCollection(arrayNode, getParametrizedTypeContainer(arrayElementType, List.class))
                                .toArray((Object[]) java.lang.reflect.Array.newInstance(arrayElementType, arrayNode.size()));
                        Object[] arguments = new Object[]{array};
                        method.invoke(object, arguments);
                    }
                    else{
                        throw new IllegalArgumentException("containerType is not Collection or Array");
                    }
                }
            }
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            throw new DeserializationException("Cannot set value for field: "+fieldName, e);
        }

    }

    private Collection<Object> mapArray(ArrayNode arrayNode, ParameterizedType containerParameterizedType){
        return  mapJavaCollection(arrayNode, containerParameterizedType);
    }

    private Collection<Object> mapJavaCollection(ArrayNode arrayNode, ParameterizedType containerParameterizedType) {
        Collection<Object> collection = typingService.instantiateContainer((Class<? extends Collection>) containerParameterizedType.getRawType());
        if(!isHomogeneous(arrayNode)){
            throw new IllegalStateException("arrayNode is not homogeneous");
        }
        JsonNodeType jsonNodeType = arrayNode.get(0).getNodeType();
        Type elementType = containerParameterizedType.getActualTypeArguments()[0];
        if(jsonNodeType == JsonNodeType.OBJECT){
            if(!(elementType instanceof Class<?> actualParameterizedType)){
                throw new IllegalStateException("elementType is not Class but a "+elementType.getClass()+", probably due to a nested generic type, please use TypeReference");
            }
            for(JsonNode jsonNode : arrayNode){
                collection.add(mapObject(jsonNode, actualParameterizedType));
            }
        }
        else if(jsonNodeType == JsonNodeType.ARRAY){
            if(elementType instanceof ParameterizedType subParameterizedType
                    && (Collection.class.isAssignableFrom((Class<?>) subParameterizedType.getRawType()))){
                for(JsonNode jsonNode : arrayNode){
                    collection.add(mapJavaCollection((ArrayNode) jsonNode, subParameterizedType));
                }
            }
            else if(elementType instanceof Class<?> actualParameterizedType){
                for(JsonNode jsonNode : arrayNode){
                    collection.add(mapJavaCollection((ArrayNode) jsonNode, getParametrizedTypeContainer(actualParameterizedType.getComponentType(), List.class))
                            .toArray((Object[]) Array.newInstance(actualParameterizedType.getComponentType(), jsonNode.size())));
                }
            }
            else{
                throw new IllegalStateException("actualParameterizedType is not Collection or Array");
            }
        }
        else if(arrayNode.get(0).isValueNode()){
            int i = 0;
            for (JsonNode jsonNode : arrayNode) {
                ValueNode valueNode = (ValueNode) jsonNode;
                assertValueNodeType(valueNode, (Class<?>) elementType, "element "+i+" of array "+arrayNode);
                collection.add(getFieldNodeValue(valueNode, (Class<?>) elementType));
            }

        }

        else{
            throw new IllegalArgumentException("jsonNodeType is not supported: "+jsonNodeType);
        }

        return collection;
    }

    private void checkUnknownProperties(JsonNode jsonNode, Map<String, Method> properties, String typeField, Class<?> typeClass){
        Set<String> unknownProperties = new HashSet<>();
        jsonNode.fieldNames().forEachRemaining(fieldName -> {
            if(!properties.containsKey(fieldName) && !fieldName.equals(typeField)){
                unknownProperties.add(fieldName);
            }
        });
        if(unknownProperties.size() > 0){
            throw new IllegalStateException("Unknown properties: "+unknownProperties+" for type: "+typeClass);
        }
    }

    private ParameterizedType getParametrizedTypeContainer(Class<?> type, Class<?> containerType){
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{type};
            }

            @Override
            public Type getRawType() {
                return containerType;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }


    // Check i and i+1 are of same type
    private boolean isHomogeneous(ArrayNode arrayNode){
        if(arrayNode.size() < 2){
            return true;
        }
        JsonNodeType jsonNodeType = arrayNode.get(0).getNodeType();
        for(int i=1; i<arrayNode.size(); i++){
            if(arrayNode.get(i).getNodeType() != jsonNodeType){
                return false;
            }
        }
        return true;
    }

    private void scanClassPath(){
        scanResult = new ClassGraph()
                .enableAllInfo()
                .scan();
    }


    private Map<String, Method> extractSetters(Class<?> type) {
        if (!propertiesMap.containsKey(type)) {
            Map<String, Method> properties = new HashMap<>();
            List<Method> methods = preferSubClassMethods(type);
            for(Method method : methods){
                if(method.getName().startsWith("set") && method.getParameterCount() == 1){
                    List<String> propertyNames = new ArrayList<>();
                    if (method.isAnnotationPresent(JsonProperty.class)) {
                        JsonProperty jsonProperty = method.getAnnotation(JsonProperty.class);
                        propertyNames.add(jsonProperty.value());
                    }
                    if (method.isAnnotationPresent(JsonAlias.class)) {
                        JsonAlias jsonAlias = method.getAnnotation(JsonAlias.class);
                        propertyNames.addAll(Arrays.asList(jsonAlias.value()));
                    } else {
                        propertyNames.add(method.getName().substring(3, 4).toLowerCase() + method.getName().substring(4));
                    }
                    for (String propertyName : propertyNames) {
                        properties.put(propertyName, method);
                    }
                }
            }

            propertiesMap.put(type, properties);
        }
        return propertiesMap.get(type);
    }

    private List<Method> preferSubClassMethods(Class<?> type){
        Map<String, Method> methodMap = new HashMap<>();
        for(Method method : type.getMethods()){
            if(methodMap.containsKey(method.getName())){
                methodMap.put(method.getName(), keepYoungest(method, methodMap.get(method.getName())));
            }
            else{
                methodMap.put(method.getName(), method);
            }
        }
        return new ArrayList<>(methodMap.values());
    }

    private Method keepYoungest(Method method1, Method method2){
        if(method1.getReturnType().isAssignableFrom(method2.getReturnType())){
            return method2;
        }
        else{
            return method1;
        }
    }

    private Class<?> getTypeClass(String typeName, Class<?> type){
        if(scanResult == null){
            scanClassPath();
        }
        ClassInfoList classInfoList = scanResult.getSubclasses(type);

        // add class itself
        classInfoList.add(scanResult.getClassInfo(type.getName()));

        for(Class<?> aClass : classInfoList.loadClasses()) {
            if(aClass.isAnnotationPresent(ElementType.class)){
                ElementType elementType = aClass.getAnnotation(ElementType.class);
                if(elementType.name().equals(typeName)){
                    return aClass;
                }
            }
        }
        throw new DeserializationException("Cannot find a sub-class of " + type.getCanonicalName() + " annotated with @ElementType and value='"+typeName+"' in classpath");
    }



    private Object getFieldNodeValue(ValueNode valueNode, Class<?> type){
        if(type == String.class){
            return valueNode.asText();
        }
        if(type == Integer.class || type == int.class){
            return valueNode.asInt();
        }
        if(type == Long.class || type == long.class){
            return valueNode.asLong();
        }
        if(type == Double.class || type == double.class){
            return valueNode.asDouble();
        }
        if(type == Boolean.class || type == boolean.class){
            return valueNode.asBoolean();
        }
        if(type == Float.class || type == float.class){
            return valueNode.floatValue();
        }
        if(type == Short.class || type == short.class){
            return valueNode.shortValue();
        }
        if(type == Byte.class || type == byte.class){
            return Base64.getDecoder().decode(valueNode.textValue());
        }
        if(type == Byte[].class || type == byte[].class){
            return Base64.getDecoder().decode(valueNode.textValue());
        }
        if(type == Character.class || type == char.class){
            if (valueNode.textValue().length() != 1) {
                throw new TypingException("source field is not char: " + valueNode.textValue());
            }
            return valueNode.textValue().charAt(0);
        }
        if(type == Date.class || type == LocalDate.class || type == LocalDateTime.class || Enum.class.isAssignableFrom(type)){
            try {
                return objectMapper.readValue(valueNode.traverse(), type);
            } catch (IOException e) {
                throw new TypingException("Could not parse date: ", e);
            }
        }
        if(type == Collection.class ){
            throw new TypingException("Collection type not supported");
        }
        if(type == Object.class){
            throw new TypingException("Object type not supported");
        }
        throw new TypingException("Unknown type: "+type);
    }



    private void assertFieldType(JsonNode fieldNode, Class<?> type, String fieldName){
        if(fieldNode.isNull()){
            return;
        }
        String expectedType = typingService.isValue(type) ? "value" : (typingService.isArray(type) ? "array" : "object");
        String actualType = fieldNode.isValueNode() ? "value" : (fieldNode.isArray() ? "array" : "object");
        if(!expectedType.equals(actualType)){
            if(fieldNode.isValueNode() && fieldNode.getNodeType().equals(JsonNodeType.STRING) && isParsableAs(fieldNode.asText(), type)){
                return;
            }
            throw new UnexpectedFieldException("Field "+fieldName+" expected to be a "+expectedType+" node, found "+actualType+" node: "+fieldNode);
        }
    }

    private boolean isParsableAs(String text, Class<?> type){
        try {
            objectMapper.readValue("\""+text+"\"", type);
            return true;
        }
        catch (IOException e){
            return false;
        }
    }

    private boolean isIgnored(Method method){
        if(method.isAnnotationPresent(JsonIgnore.class)){
            return true;
        }
        Method getter = getGetterFromSetter(method);
        return getter != null && getter.isAnnotationPresent(JsonIgnore.class);
    }

    private Method getGetterFromSetter(Method setter){
        String getterName = "get"+setter.getName().substring(3);
        try {
            return setter.getDeclaringClass().getMethod(getterName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }


    private void assertValueNodeType(ValueNode valueNode, Class<?> type, String fieldName){
        if(valueNode.isNull()){
            return;
        }
        JsonNodeType expectedType = JsonNodeType.MISSING;
        if(type == String.class || type == Character.class ||
                type == char.class || type == byte[].class ||
                type == Byte.class || type == byte.class){
            expectedType = JsonNodeType.STRING;
        }
        if(typingService.isNumeric(type)){
            expectedType = JsonNodeType.NUMBER;
        }
        if(type == Boolean.class || type == boolean.class){
            expectedType = JsonNodeType.BOOLEAN;
        }
        if(type == Void.class || type == void.class){
            expectedType = JsonNodeType.NULL;
        }
        if(valueNode.getNodeType() != expectedType){
            if(valueNode.getNodeType().equals(JsonNodeType.STRING) && isParsableAs(valueNode.asText(), type)){
                return;
            }
            throw new UnexpectedFieldException("Field "+fieldName+" expected to be a "+expectedType+" node, found "+valueNode.getNodeType()+" node: "+valueNode);
        }


    }

}
