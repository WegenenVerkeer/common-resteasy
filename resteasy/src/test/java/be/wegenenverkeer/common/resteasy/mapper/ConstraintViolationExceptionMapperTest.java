/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.mapper;

import be.wegenenverkeer.common.resteasy.json.RestJsonMapper;
import be.wegenenverkeer.common.resteasy.logging.PreProcessLoggingInterceptor;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.PathImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Test voor ConstraintViolationExceptionMapper.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConstraintViolationExceptionMapperTest {

    @InjectMocks
    private ConstraintViolationExceptionMapper mapper;

    @Mock
    private PreProcessLoggingInterceptor preProcessLoggingInterceptor;

    @Mock
    private HttpServletRequest request;

    @Spy
    private RestJsonMapper jsonMapper = new RestJsonMapper();

    @Test
    public void testGetJsonString() throws Exception {
        Object bean = new Object();
        Set<ConstraintViolation<?>> violations = new HashSet<ConstraintViolation<?>>();
        violations.add(new ConstraintViolationImpl("tmpl", "oopsie msg", Object.class,
                bean, bean, bean, PathImpl.createPathFromString("var1"), null, null));
        violations.add(new ConstraintViolationImpl("Grote boodschap", "failure", Object.class,
                bean, bean, bean, PathImpl.createPathFromString("var2"), null, null));
        violations.add(new ConstraintViolationImpl("Grote boodschap", "imsg", Object.class,
                bean, bean, bean, PathImpl.createPathFromString("var2"), null, null));
        ConstraintViolationException cve = new ConstraintViolationException("problems", violations);

        Response res = mapper.toResponse(cve);

        assertThat(res.getStatus()).isEqualTo(Response.Status.PRECONDITION_FAILED.getStatusCode());
        assertThat( // grr, order of elements can change
                res.getEntity().equals("{ \"error\" : {\"var1\":[\"oopsie msg\"],\"var2\":[\"failure\",\"imsg\"]}}")
                ||
                res.getEntity().equals("{ \"error\" : {\"var1\":[\"oopsie msg\"],\"var2\":[\"imsg\",\"failure\"]}}")
                ||
                res.getEntity().equals("{ \"error\" : {\"var2\":[\"failure\",\"imsg\"],\"var1\":[\"oopsie msg\"]}}")
                ||
                res.getEntity().equals("{ \"error\" : {\"var2\":[\"imsg\",\"failure\"],\"var1\":[\"oopsie msg\"]}}")
        ).isTrue();
        verify(preProcessLoggingInterceptor).postProcessError(cve,
                "Applicatie keerde terug met een (verwachtte) ConstraintViolation:");
    }

}
