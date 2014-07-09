/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;
import java.io.InputStream;

/**
 * Serializer voor InputStreams die gebruikt wordt bij logging.
 * <p/>
 * In de logging willen we niet de volledige stream uitschrijven.
 */
//@Provider
//@Component
public class InputStreamSerializer extends JsonSerializer<InputStream> {

    @Override
    public void serialize(InputStream value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeString("<Not serialized InputStream>");
    }

}
