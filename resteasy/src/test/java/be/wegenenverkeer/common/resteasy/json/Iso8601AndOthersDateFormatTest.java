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
 * Test voor Iso8601AndOthersDateFormat.
 */
public class Iso8601AndOthersDateFormatTest {
    
    private Iso8601AndOthersDateFormat format;

    private TimeZone originalTimeZone;

    @Before
    public void setup() {
        /*
        originalTimeZone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("CET"));
        */

        format = new Iso8601AndOthersDateFormat();
    }

    /*
    @After
    public void tearDown() {
        TimeZone.setDefault(originalTimeZone);
    }
    */

    @Test
    public void testFormat() throws Exception {
        Date date = new DateTime(2013, 11, 2, 3, 4, 5).toDate();
        assertThat(format.format(date)).isEqualTo("2013-11-02T03:04:05");
        assertThat(format.format(date)).isEqualTo("2013-11-02T03:04:05");
    }

    @Test
    public void testParse() throws Exception {
        Date date = new DateMidnight(2013, 12, 11).toDate();
        Date seconds = new DateTime(2013, 12, 11, 5, 35, 22).toDate();
        Date minutes = new DateTime(2013, 12, 11, 5, 35).toDate();

        assertThat(format.parse("2013-12-11 5:35")).isEqualTo(minutes);
        assertThat(format.parse("2013-12-11 05:35:22")).isEqualTo(seconds);
        assertThat(format.parse("2013-12-11T05:35:22")).isEqualTo(seconds);
        assertThat(format.parse("2013-12-11T05:35:22.000")).isEqualTo(seconds);
        assertThat(format.parse("2013-12-11")).isEqualTo(date);
        assertThat(format.parse("11/12/2013")).isEqualTo(date);
        assertThat(format.parse("20131211053522")).isEqualTo(seconds);
        assertThat(format.parse("Wed Dec 11 05:35:22 2013")).isEqualTo(seconds); // ASCTIME
    }

    @Test
    @Ignore // nwe Jenkins valt hierover omdat het op een andere tijdzone zit
    public void testParse_zoned() throws Exception {
        Date seconds = new DateTime(2013, 12, 11, 5, 35, 22).toDate();

        assertThat(format.parse("Wed Dec 11 05:35:22 CET 2013")).isEqualTo(seconds);
        assertThat(format.parse("Wednesday, 11-Dec-2013 05:35:22 CET")).isEqualTo(seconds); // RFC1036
        assertThat(format.parse("Wed, 11 Dec 2013 05:35:22 CET")).isEqualTo(seconds); // RFC1123
    }

    @Test
    public void testClone() throws Exception {
        assertThat(format.clone()).isEqualTo(format);
    }
    
}
