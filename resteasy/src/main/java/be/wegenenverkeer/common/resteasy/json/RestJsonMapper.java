/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Main entry for the Json serializer/deserializer.
 */
@Component
public class RestJsonMapper extends ObjectMapper {

    /**
     * No-arguments constructor.
     */
    public RestJsonMapper() {
        super();

        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.setDateFormat(new Iso8601AndOthersDateFormat());

        addClassSerializer(LocalDate.class, new LocalDateSerializer());
        addClassSerializer(LocalDateTime.class, new LocalDateTimeSerializer());

        SimpleModule testModule = new SimpleModule("jsr310", new Version(1, 0, 0, "", "be.wegenenverkeer.common", "common-resteasy"));
        testModule.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        testModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        this.registerModule(testModule);
    }

    /**
     * Add a custom serializer.
     *
     * @param classToMap class to map
     * @param classSerializer serializer
     * @param <T> class to map
     */
    public <T> void addClassSerializer(Class<? extends T> classToMap, JsonSerializer<T> classSerializer) {
        SimpleModule testModule = new SimpleModule("MyModule", new Version(1, 0, 0, null));
        testModule.addSerializer(classToMap, classSerializer);
        this.registerModule(testModule);
    }

}
