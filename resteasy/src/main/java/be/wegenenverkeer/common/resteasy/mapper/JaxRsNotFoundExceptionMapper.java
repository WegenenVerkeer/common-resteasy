/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.mapper;


import be.wegenenverkeer.common.resteasy.exception.ExceptionUtil;
import be.wegenenverkeer.common.resteasy.logging.PreProcessLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for NotFoundException.
 */
@Provider
@Component
public class JaxRsNotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Autowired
    private PreProcessLoggingInterceptor preProcessLoggingInterceptor;

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(NotFoundException exception) {
        preProcessLoggingInterceptor.postProcessError(exception, "De resource werd niet gevonden:");
        ExceptionUtil eu = new ExceptionUtil(exception);
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{ \"error\" : {\"\":[\"" + eu.getEscapedConcatenatedMessage() + "\"]}}")
                .header("Access-Control-Allow-Origin", request.getHeader("Origin"))
                .header("Access-Control-Allow-Credentials", true)
                .build();
    }

}

