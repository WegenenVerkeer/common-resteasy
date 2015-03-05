/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Jackson serializer for LocalDate.
 */
public class LocalDateDeserializer extends FromStringDeserializer<LocalDate> {

    private Iso8601AndOthersLocalDateFormat iso8601AndOthers = new Iso8601AndOthersLocalDateFormat();

    /** Constructor. */
    public LocalDateDeserializer() {
        super(LocalDate.class);
    }

    // CHECKSTYLE METHOD_NAME: OFF
    @Override
    protected LocalDate _deserialize(String value, DeserializationContext ctxt) throws IOException {
        if (StringUtils.isNotBlank(value)) {
                return iso8601AndOthers.parse(value);
        }
        return null; // empty string
    }
    // CHECKSTYLE METHOD_NAME: ON

}