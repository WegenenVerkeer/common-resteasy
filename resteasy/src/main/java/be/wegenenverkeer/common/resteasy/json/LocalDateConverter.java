/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.spi.StringConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import javax.ws.rs.ext.Provider;

/**
 * Dateformatter om verschillende soorten data te kunnen parsen in RestEasy.
 */
@Provider
@Component
public class LocalDateConverter implements StringConverter<LocalDate> {

    private Iso8601AndOthersLocalDateFormat iso8601AndOthers = new Iso8601AndOthersLocalDateFormat();

    @Override
    public LocalDate fromString(String str) {
        LocalDate date = null;

        if (StringUtils.isNotBlank(str)) {
            try {
                return iso8601AndOthers.parse(str);
            } catch (IllegalArgumentException iae) {
                // ignore, try next format
                date = null; // dummy
            }
        }
        return date; // empty string
    }

    @Override
    public String toString(LocalDate value) {
        return iso8601AndOthers.format(value);
    }
}
