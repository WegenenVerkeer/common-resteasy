/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.mapper;


import be.wegenenverkeer.common.resteasy.exception.AuthException;
import be.wegenenverkeer.common.resteasy.exception.ExceptionUtil;
import be.wegenenverkeer.common.resteasy.exception.NotFoundException;
import be.wegenenverkeer.common.resteasy.exception.ServiceException;
import be.wegenenverkeer.common.resteasy.exception.ValidationException;
import be.wegenenverkeer.common.resteasy.logging.PreProcessLoggingInterceptor;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Deze exception mapper vangt elke mogelijke exception op, logt deze (en flushed de rest van de service log naar file)
 * en packaged de boodschap van de exception in een json zodat de gebruiker hiervan een boodschap kan krijgen.
 */
@Provider
@Component
public class UnhandledExceptionMapper implements ExceptionMapper<Exception> {

    @Autowired
    private ServiceExceptionMapper serviceExceptionMapper;

    @Autowired
    private ValidationExceptionMapper validationExceptionMapper;

    @Autowired
    private NotFoundExceptionMapper notFoundExceptionMapper;

    @Autowired
    private AuthExceptionMapper authExceptionMapper;

    @Autowired
    private ResteasyNotFoundExceptionMapper resteasyNotFoundExceptionMapper;

    @Autowired
    private AuthenticationExceptionMapper authenticationExceptionMapper;

    @Autowired
    private ConstraintViolationExceptionMapper constraintViolationExceptionMapper;

    @Autowired
    private MethodConstraintViolationExceptionMapper methodConstraintViolationExceptionMapper;

    @Autowired
    private PreProcessLoggingInterceptor preProcessLoggingInterceptor;

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(Exception exception) {
        if (exception.getCause() != null && exception.getCause() instanceof ServiceException) {
            ServiceException se = (ServiceException) exception.getCause();
            return serviceExceptionMapper.toResponse(se);
        } else if (exception.getCause() != null && exception.getCause() instanceof ValidationException) {
            ValidationException ve = (ValidationException) exception.getCause();
            return validationExceptionMapper.toResponse(ve);
        } else if (exception instanceof NotFoundException) {
            NotFoundException nfe = (NotFoundException) exception;
            return notFoundExceptionMapper.toResponse(nfe);
        } else if (exception instanceof AuthException) {
            AuthException ae = (AuthException) exception;
            return authExceptionMapper.toResponse(ae);
        } else if (exception instanceof org.jboss.resteasy.spi.NotFoundException) {
            org.jboss.resteasy.spi.NotFoundException nfe = (org.jboss.resteasy.spi.NotFoundException) exception;
            return resteasyNotFoundExceptionMapper.toResponse(nfe);
        } else if (exception instanceof AuthenticationException) {
            AuthenticationException ae = (AuthenticationException) exception;
            return authenticationExceptionMapper.toResponse(ae);
        } else if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) exception;
            return constraintViolationExceptionMapper.toResponse(cve);
        } else if (exception instanceof MethodConstraintViolationException) {
            MethodConstraintViolationException mcve = (MethodConstraintViolationException) exception;
            return methodConstraintViolationExceptionMapper.toResponse(mcve);
        } else {
            preProcessLoggingInterceptor.postProcessError(exception, "Kritische fout gedetecteerd:");
            ExceptionUtil eu = new ExceptionUtil(exception);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{ \"error\" : {\"\":[\"" + eu.getEscapedConcatenatedMessage() + "\"]}}")
                    .header("Access-Control-Allow-Origin", request.getHeader("Origin"))
                    .header("Access-Control-Allow-Credentials", true)
                    .build();
        }
    }

}

