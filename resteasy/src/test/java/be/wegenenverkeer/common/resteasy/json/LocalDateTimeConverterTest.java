/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test voor DateConverter.
 */
public class LocalDateTimeConverterTest {
    
    private LocalDateTimeConverter dateConverter;

    private TimeZone originalTimeZone;

    @Before
    public void setup() {
        originalTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("GMT+0500"))); // this probably does not work, but default timezone should not be GMT of test fails

        dateConverter = new LocalDateTimeConverter();
    }

    @After
    public void tearDown() {
        TimeZone.setDefault(originalTimeZone);
    }


    @Test
    public void testConvertFromStringNoTimeZone() {
        LocalDateTime date = LocalDateTime.of(2013, 12, 11, 0, 0);
        LocalDateTime seconds = LocalDateTime.of(2013, 12, 11, 5, 35, 22);
        LocalDateTime minutes = LocalDateTime.of(2013, 12, 11, 5, 35);

        assertThat(dateConverter.fromString("2013-12-11 5:35")).isEqualTo(minutes);
        assertThat(dateConverter.fromString("2013-12-11 05:35:22")).isEqualTo(seconds);
        assertThat(dateConverter.fromString("2013-12-11T05:35:22")).isEqualTo(seconds);
        assertThat(dateConverter.fromString("2013-12-11")).isEqualTo(date);
        assertThat(dateConverter.fromString("11/12/2013")).isEqualTo(date);
        assertThat(dateConverter.fromString("20131211053522")).isEqualTo(seconds);
        assertThat(dateConverter.fromString("Wed Dec 11 05:35:22 2013")).isEqualTo(seconds); // ASCTIME

        assertThat(dateConverter.fromString(null)).isNull();
    }

    @Test
    public void testConvertFromStringIsoWithTimeZone() {
        ZonedDateTime zoned;
        LocalDateTime seconds = LocalDateTime.of(2013, 12, 11, 5, 35, 22);
        zoned = seconds.atZone(ZoneId.of("Z")).withZoneSameInstant(TimeZone.getDefault().toZoneId());
        seconds = LocalDateTime.from(zoned);
        LocalDateTime minutes = LocalDateTime.of(2013, 12, 11, 5, 35);
        zoned = minutes.atZone(ZoneId.of("Z")).withZoneSameInstant(TimeZone.getDefault().toZoneId());
        minutes = LocalDateTime.from(zoned);
        System.out.println(minutes);

        assertThat(dateConverter.fromString("2013-12-11T05:35:22Z")).isEqualTo(seconds);
    }

}
