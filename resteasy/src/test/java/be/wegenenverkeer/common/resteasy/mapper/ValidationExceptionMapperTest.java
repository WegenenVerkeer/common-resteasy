/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.mapper;

import be.wegenenverkeer.common.resteasy.exception.ValidationException;
import be.wegenenverkeer.common.resteasy.json.RestJsonMapper;
import be.wegenenverkeer.common.resteasy.logging.PreProcessLoggingInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ValidationExceptionMapperTest {

    @InjectMocks
    private ValidationExceptionMapper mapper;

    @Mock
    private PreProcessLoggingInterceptor preProcessLoggingInterceptor;

    @Spy
    private RestJsonMapper jsonMapper = new RestJsonMapper();

    @Mock
    private HttpServletRequest request;

    @Test
    public void testToResponse() throws Exception {
        ValidationException oopsie = new ValidationException();
        oopsie.addException("veld", "boodschap");
        oopsie.addException("nogEenVeld", "Eerste boodschap.");
        oopsie.addException("nogEenVeld", "Extra boodschap voor veld.");

        Response res = mapper.toResponse(oopsie);

        System.out.println(res);

        assertThat(res.getStatus()).isEqualTo(Response.Status.PRECONDITION_FAILED.getStatusCode());
        assertThat( // grr, order of elements can change
                res.getEntity().equals("{ \"error\" : {\"nogEenVeld\":[\"Eerste boodschap.\",\"Extra boodschap voor veld.\"],\"veld\":[\"boodschap\"]}}")
                ||
                res.getEntity().equals("{ \"error\" : {\"veld\":[\"boodschap\"],\"nogEenVeld\":[\"Eerste boodschap.\",\"Extra boodschap voor veld.\"]}}")
                ||
                res.getEntity().equals("{ \"error\" : {\"nogEenVeld\":[\"Extra boodschap voor veld.\",\"Eerste boodschap.\"],\"veld\":[\"boodschap\"]}}")
                ||
                res.getEntity().equals("{ \"error\" : {\"veld\":[\"boodschap\"],\"nogEenVeld\":[\"Extra boodschap voor veld.\",\"Eerste boodschap.\"]}}")
        ).isTrue();
        verify(preProcessLoggingInterceptor).postProcessError(eq(oopsie),
                startsWith("Applicatie gaf een (verwachtte) ValidationException:"));

    }
}
