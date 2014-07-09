/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test voor DateConverter.
 */
public class DateConverterTest {
    
    private DateConverter dateConverter;

    private TimeZone originalTimeZone;

    @Before
    public void setup() {
        /*
        originalTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
        */

        dateConverter = new DateConverter();
    }

    /*
    @After
    public void tearDown() {
        TimeZone.setDefault(originalTimeZone);
    }
    */
    
    @Test
    public void testConvertFromString() {
        Date date = new DateMidnight(2013, 12, 11).toDate();
        Date seconds = new DateTime(2013, 12, 11, 5, 35, 22).toDate();
        Date minutes = new DateTime(2013, 12, 11, 5, 35).toDate();

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
    @Ignore // nwe Jenkins valt hierover omdat het op een andere tijdzone zit
    public void testConvertFromString_zoned() {
        Date seconds = new DateTime(2013, 12, 11, 5, 35, 22).toDate();

        assertThat(dateConverter.fromString("Wed Dec 11 05:35:22 CET 2013")).isEqualTo(seconds);
        assertThat(dateConverter.fromString("Wednesday, 11-Dec-2013 05:35:22 CET")).isEqualTo(seconds); // RFC1036
        assertThat(dateConverter.fromString("Wed, 11 Dec 2013 05:35:22 CET")).isEqualTo(seconds); // RFC1123
    }

}
