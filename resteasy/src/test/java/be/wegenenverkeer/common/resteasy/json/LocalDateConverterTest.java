/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test voor DateConverter.
 */
public class LocalDateConverterTest {
    
    private LocalDateConverter dateConverter;

    @Before
    public void setup() {
        dateConverter = new LocalDateConverter();
    }

    @Test
    public void testConvertFromString() {
        LocalDate date = LocalDate.of(2013, 12, 11);

        assertThat(dateConverter.fromString("2013-12-11 5:35")).isEqualTo(date);
        assertThat(dateConverter.fromString("2013-12-11 05:35:22")).isEqualTo(date);
        assertThat(dateConverter.fromString("2013-12-11T05:35:22")).isEqualTo(date);
        assertThat(dateConverter.fromString("2013-12-11")).isEqualTo(date);
        assertThat(dateConverter.fromString("11/12/2013")).isEqualTo(date);
        assertThat(dateConverter.fromString("20131211053522")).isEqualTo(date);
        assertThat(dateConverter.fromString("Wed Dec 11 05:35:22 2013")).isEqualTo(date); // ASCTIME

        assertThat(dateConverter.fromString(null)).isNull();
    }

}
