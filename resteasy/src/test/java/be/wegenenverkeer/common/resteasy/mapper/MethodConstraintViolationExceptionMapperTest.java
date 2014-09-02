/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.mapper;

import be.wegenenverkeer.common.resteasy.json.RestJsonMapper;
import be.wegenenverkeer.common.resteasy.logging.PreProcessLoggingInterceptor;
import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import static be.eliwan.jfaker.mockito.FakerMock.withFields;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MethodConstraintViolationExceptionMapperTest {

    @InjectMocks
    private MethodConstraintViolationExceptionMapper mapper;

    @Spy
    private RestJsonMapper jsonMapper = new RestJsonMapper();

    @Mock
    private PreProcessLoggingInterceptor preProcessLoggingInterceptor;

    @Mock
    private HttpServletRequest request;

    @Test
    public void testGetJsonString() throws Exception {
        MethodConstraintViolationException oopsie = mock(MethodConstraintViolationException.class);
        Set<MethodConstraintViolation<?>> violations = new HashSet<MethodConstraintViolation<?>>(
                (List<MethodConstraintViolation<?>>) (List) Arrays.asList(
                        withFields(MethodConstraintViolation.class,
                                "message", "A problem."
                                ),
                        withFields(MethodConstraintViolation.class,
                                "message", "Something else."
                                )
        ));
        when(oopsie.getConstraintViolations()).thenReturn(violations);

        Response res = mapper.toResponse(oopsie);

        assertThat(res.getStatus()).isEqualTo(Response.Status.PRECONDITION_FAILED.getStatusCode());

        // beide volgende resultaten zijn mogelijk, dus test moet een beetje anders
        //assertThat(res.getEntity()).isEqualTo("{ \"error\" : {\"\":[\"Something else.\",\"A problem.\"]}}");
        //assertThat(res.getEntity()).isEqualTo("{ \"error\" : {\"\":[\"A problem.\",\"Something else.\"]}}");
        assertThat((String) res.getEntity()).startsWith("{ \"error\" : {\"\":[");
        assertThat((String) res.getEntity()).endsWith("]}}");
        assertThat((String) res.getEntity()).containsOnlyOnce("\"A problem.\"");
        assertThat((String) res.getEntity()).containsOnlyOnce("\"Something else.\"");

        verify(preProcessLoggingInterceptor).postProcessError(oopsie,
                "Applicatie keerde terug met een (verwachtte) ConstraintViolation:");
    }

}
