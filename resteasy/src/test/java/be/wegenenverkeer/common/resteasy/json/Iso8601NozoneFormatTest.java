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
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test voor Iso8601NozoneFormat, ISO formaat zonder rekening te houden met tijdzone.
 */
public class Iso8601NozoneFormatTest {

    private Iso8601NozoneFormat format;

    @Before
    public void setUp() throws Exception {
        format = new Iso8601NozoneFormat();
    }

    @Test
    public void testFormat() throws Exception {
        Date date = new DateTime(2013, 11, 2, 3, 4, 5).toDate();
        assertThat(format.format(date)).isEqualTo("2013-11-02T03:04:05");
        assertThat(format.format(date)).isEqualTo("2013-11-02T03:04:05");
    }

    @Test
    public void testParse() throws Exception {
        Date date;
        date = new DateTime(2013, 11, 2, 3, 4, 5).toDate();
        assertThat(format.parse("2013-11-02T03:04:05.000")).isEqualTo(date);
        assertThat(format.parse("2013-11-02T03:04:05")).isEqualTo(date);
        date = new DateMidnight(2013, 11, 2).toDate();
        assertThat(format.parse("2013-11-02")).isEqualTo(date);
    }

    @Test
    public void testClone() throws Exception {
        assertThat(format.clone()).isEqualTo(format);
    }

}
