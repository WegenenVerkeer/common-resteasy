/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * Provide a fast thread-safe formatter/parser DateFormat for ISO8601 dates ONLY.
 * It was mainly done to be used with Jackson JSON Processor.
 * <p/>
 * Watch out for clone implementation that returns itself.
 * <p/>
 * All other methods but parse and format and clone are undefined behavior.
 *
 * @see com.fasterxml.jackson.map.util.ISO8601Utils
 */
public class Iso8601AndOthersLocalDateFormat {

    private Iso8601NozoneLocalDateTimeFormat iso8601NozoneFormat = new Iso8601NozoneLocalDateTimeFormat();

    /**
     * Datumformaten.
     */
    private static final DateTimeFormatter[] FORMATS = {
            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd H:m:s"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd H:m"),
            DateTimeFormatter.ofPattern(DateUtil.PATTERN_ASCTIME),
            DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss zzz yyyy"),
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss"),
    };


    /**
     * Format date.
     *
     * @param date date to format
     * @return formatted string
     */
    public String format(LocalDate date) {
        return FORMATS[0].format(date);
    }

    /**
     * Parse string to date.
     *
     * @param str string to parse
     * @return date
     */
    public LocalDate parse(String str) {
        LocalDate date = null;

        if (!StringUtils.isBlank(str)) {
            // try ISO 8601 format first
            try {
                return LocalDate.from(iso8601NozoneFormat.parse(str));
            } catch (IllegalArgumentException | DateTimeParseException ex) {
                // ignore, try next format
                date = null; // dummy
            }

            // then try a list of formats
            for (DateTimeFormatter formatter : FORMATS) {
                try {
                    return LocalDate.from(formatter.parse(str));
                } catch (IllegalArgumentException | DateTimeParseException e) {
                    // ignore, try next format
                    date = null; // dummy
                }
            }
            throw new IllegalArgumentException("Could not parse date " + str + " using ISO 8601 or any of the formats " + Arrays.asList(FORMATS) + ".");

        }
        return date; // empty string
    }

    // CHECKSTYLE CLONE: OFF
    @Override
    public Object clone() {
        return this;    // jackson calls clone everytime. We are threadsafe so just returns the instance
    }
    // CHECKSTYLE CLONE: ON

}
