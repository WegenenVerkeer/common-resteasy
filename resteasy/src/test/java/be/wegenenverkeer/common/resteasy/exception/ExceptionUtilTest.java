/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.exception;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for ExceptionUtil.
 */
public class ExceptionUtilTest {

    private ExceptionUtil exceptionUtil;

    @Before
    public void setUp() throws Exception {
        Exception cause = new IllegalAccessException("Something \"is\"\t:\nwrong!");
        Exception outer = new IllegalStateException("Oops", cause);
        exceptionUtil = new ExceptionUtil(outer);
    }

    @Test
    public void testGetConcatenatedMessage() throws Exception {
        assertThat(exceptionUtil.getConcatenatedMessage()).isEqualTo("Oops; Something \"is\"\t:\n" +
                "wrong!");
    }

    @Test
    public void testGetEscapedConcatenatedMessage() throws Exception {
        assertThat(exceptionUtil.getEscapedConcatenatedMessage()).isEqualTo("Oops; Something 'is' :\\nwrong!");
    }
}
