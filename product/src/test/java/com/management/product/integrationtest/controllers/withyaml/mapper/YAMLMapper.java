package com.management.product.integrationtest.controllers.withyaml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

public class YAMLMapper implements io.restassured.mapper.ObjectMapper {

    private final ObjectMapper mapper;
    protected final TypeFactory typeFactory;

    public YAMLMapper() {
        this.mapper = new ObjectMapper(new YAMLFactory())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.typeFactory = TypeFactory.defaultInstance();
    }

    @Override
    public Object deserialize(ObjectMapperDeserializationContext context) {
        var content = context.getDataToDeserialize().asString();
        Class<?> type = (Class<?>) context.getType();
        try {
            return mapper.readValue(content, typeFactory.constructType(type));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error deserializing YAML content", e);
        }
    }

    @Override
    public Object serialize(ObjectMapperSerializationContext context) {
        try {
            return mapper.writeValueAsString(context.getObjectToSerialize());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error serializing YAML content", e);
        }
    }
}
