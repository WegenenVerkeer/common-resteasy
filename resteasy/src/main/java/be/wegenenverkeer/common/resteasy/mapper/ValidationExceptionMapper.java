/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.mapper;

import be.wegenenverkeer.common.resteasy.exception.ExceptionUtil;
import be.wegenenverkeer.common.resteasy.exception.ValidationException;
import be.wegenenverkeer.common.resteasy.json.DcJsonMapper;
import be.wegenenverkeer.common.resteasy.logging.PreProcessLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * ExceptionMapper for ValidationException.
 */
@Provider
@Component
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Autowired
    private DcJsonMapper jsonMapper;

    @Autowired
    private PreProcessLoggingInterceptor preProcessLoggingInterceptor;

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(ValidationException exception) {
        try {
            StringBuilder msg = new StringBuilder("Applicatie gaf een (verwachtte) ValidationException:");
            for (Map.Entry<String, List<String>> entry : exception.getExceptions().entrySet()) {
                msg.append('\n').append(entry.getKey()).append(": ");
                String indent = "";
                for (String m : entry.getValue()) {
                    msg.append(indent).append(m);
                    indent = "\n    ";
                }
            }
            preProcessLoggingInterceptor.postProcessError(exception, msg.toString());
            return Response.status(Response.Status.PRECONDITION_FAILED).entity("{ \"error\" : " +
                    jsonMapper.writeValueAsString(exception.getExceptions()) + "}")
                    .header("Access-Control-Allow-Origin", request.getHeader("Origin"))
                    .header("Access-Control-Allow-Credentials", true)
                    .build();
        } catch (IOException e) {
            ExceptionUtil eu = new ExceptionUtil(exception);
            return Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity("{ \"error\" : {\"validatie\",\"" + eu.getEscapedConcatenatedMessage() + "\"}")
                    .header("Access-Control-Allow-Origin", request.getHeader("Origin"))
                    .header("Access-Control-Allow-Credentials", true)
                    .build();
        }
    }
}
