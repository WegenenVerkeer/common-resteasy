/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.stereotype.Component;

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

        this.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        this.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.setDateFormat(new Iso8601AndOthersDateFormat());
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
