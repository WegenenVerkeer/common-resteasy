/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.util.DateUtil;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Provide a fast thread-safe formatter/parser DateFormat for ISO8601 dates ONLY.
 * It was mainly done to be used with Jackson JSON Processor.
 * <p/>
 * Watch out for clone implementation that returns itself.
 * <p/>
 * All other methods but parse and format and clone are undefined behavior.
 *
 * @see com.fasterxml.jackson.databind.util.ISO8601Utils
 */
public class Iso8601AndOthersDateFormat extends DateFormat {

    private static final long serialVersionUID = 1L;

    private Iso8601NozoneFormat iso8601NozoneFormat = new Iso8601NozoneFormat();

    /**
     * Datumformaten.
     */
    private static final String[] FORMATS = {
            "dd/MM/yyyy",
            "yyyy-MM-dd H:m:s",
            "yyyy-MM-dd H:m",
            DateUtil.PATTERN_RFC1036,
            DateUtil.PATTERN_RFC1123,
            DateUtil.PATTERN_ASCTIME,
            "EEE MMM d HH:mm:ss zzz yyyy",
            "yyyyMMddHHmmss",
    };

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        return iso8601NozoneFormat.format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Date parse(String str, ParsePosition pos) {
        Date date = null;

        if (!StringUtils.isBlank(str)) {
            // try ISO 8601 format first
            try {
                return iso8601NozoneFormat.parse(str, pos);
            } catch (IllegalArgumentException iae) {
                // ignore, try next format
                date = null; // dummy
            }

            // then try a list of formats
            for (String format : FORMATS) {
                DateFormat formatter = new SimpleDateFormat(format, Locale.US);
                try {
                    return formatter.parse(str);
                } catch (ParseException e) {
                    // ignore, try next format
                    date = null; // dummy
                }
            }
            throw new IllegalArgumentException("Could not parse date " + str + " using ISO 8601 or any of the formats "
                    + Arrays.asList(FORMATS) + ".");

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
