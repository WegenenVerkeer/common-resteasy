/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
public class Iso8601NozoneLocalDateTimeFormat {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final String PARSE_FAILED = "Failed to parse date ";

    // CHECKSTYLE CLONE: OFF
    @Override
    public Object clone() {
        return this;    // jackson calls clone everytime. We are threadsafe so just returns the instance
    }
    // CHECKSTYLE CLONE: ON

    /**
     * Format date into yyyy-MM-ddTHH:mm:ss[.sss]. It is expected that the timezone on both ends is the
     * same. The timezone is not included.
     *
     * @param date the date to format
     * @return the date formatted as 'yyyy-MM-ddTHH:mm:ssZ'
     */
    public String format(LocalDateTime date) {
        return FORMATTER.format(date);
    }

    /**
     * Parse a date from ISO-8601 formatted string. It expects a format yyyy-MM-ddTHH:mm:ss[.sss][Z|[+-]HH:mm]
     * The timezone is not applied when present. It is expected that the timezone on both ends is the same.
     *
     * @param date ISO string to parse in the appropriate format.
     * @return the parsed date
     * @throws IllegalArgumentException if the date is not in the appropriate format
     */
    // CHECKSTYLE INNER_ASSIGNMENT: OFF
    public LocalDateTime parse(String date) throws IllegalArgumentException {
        int max = date.length();
        try {
            int offset = 0;

            // extract year
            int year = parseInt(date, offset, offset + 4);
            offset += 4;
            checkOffset(date, offset, '-');
            offset += 1;

            // extract month
            int month = parseInt(date, offset, offset + 2);
            offset += 2;
            checkOffset(date, offset, '-');
            offset += 1;

            // extract day
            int day = parseInt(date, offset, offset + 2);
            offset += 2;

            int hour = 0;
            int minutes = 0;
            int seconds = 0;
            int milliseconds = 0; // always use 0 otherwise returned date will include millis of current time

            if (offset < max) { // time can be optional
                checkOffset(date, offset, 'T');
                offset += 1;
                // extract hours, minutes, seconds and milliseconds
                hour = parseInt(date, offset, offset + 2);
                offset += 2;
                checkOffset(date, offset, ':');
                offset += 1;

                minutes = parseInt(date, offset, offset + 2);
                offset += 2;
                checkOffset(date, offset, ':');
                offset += 1;

                seconds = parseInt(date, offset, offset + 2);
                offset += 2;
                // milliseconds can be optional in the format

                if (offset < max) { // milliseconds are be optional
                    if (date.charAt(offset) == '.') {
                        checkOffset(date, offset, '.');
                        offset += 1;
                        milliseconds = parseInt(date, offset, offset + 3);
                    }
                }
            }

            return LocalDateTime.of(year, month, day, hour, minutes, seconds, milliseconds);
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new IllegalArgumentException(PARSE_FAILED + date, e);
        }
    }
    // CHECKSTYLE INNER_ASSIGNMENT: ON

    /**
     * Check if the expected character exist at the given offset of the
     *
     * @param value    the string to check at the specified offset
     * @param offset   the offset to look for the expected character
     * @param expected the expected character
     * @throws IndexOutOfBoundsException if the expected character is not found
     */
    private static void checkOffset(String value, int offset, char expected) throws IndexOutOfBoundsException {
        char found = value.charAt(offset);
        if (found != expected) {
            throw new IndexOutOfBoundsException("Expected '" + expected + "' character but found '" + found + "'");
        }
    }

    /**
     * Parse an integer located between 2 given offsets in a string
     *
     * @param value      the string to parse
     * @param beginIndex the start index for the integer in the string
     * @param endIndex   the end index for the integer in the string
     * @return the int
     * @throws NumberFormatException if the value is not a number
     */
    private static int parseInt(String value, int beginIndex, int endIndex) throws NumberFormatException {
        if (beginIndex < 0 || endIndex > value.length() || beginIndex > endIndex) {
            throw new NumberFormatException(value);
        }
        // use same logic as in Integer.parseInt() but less generic we're not supporting negative values
        int i = beginIndex;
        int result = 0;
        int digit;
        if (i < endIndex) {
            digit = Character.digit(value.charAt(i++), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + value);
            }
            result = -digit;
        }
        while (i < endIndex) {
            digit = Character.digit(value.charAt(i++), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + value);
            }
            result *= 10;
            result -= digit;
        }
        return -result;
    }

}
