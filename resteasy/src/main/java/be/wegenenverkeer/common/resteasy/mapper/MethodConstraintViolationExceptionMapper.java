/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.mapper;

import be.wegenenverkeer.common.resteasy.exception.ExceptionUtil;
import be.wegenenverkeer.common.resteasy.json.RestJsonMapper;
import be.wegenenverkeer.common.resteasy.logging.PreProcessLoggingInterceptor;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * ExceptionMapper for ValidationException.
 */
@Provider
@Component
public class MethodConstraintViolationExceptionMapper implements ExceptionMapper<MethodConstraintViolationException> {

    @Autowired
    private RestJsonMapper jsonMapper;

    @Autowired
    private PreProcessLoggingInterceptor preProcessLoggingInterceptor;

    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(MethodConstraintViolationException exception) {
        preProcessLoggingInterceptor.postProcessError(exception,
                "Applicatie keerde terug met een (verwachtte) ConstraintViolation:");
        try {
            return Response.status(Response.Status.PRECONDITION_FAILED).entity(getJsonString(exception))
                    .header("Access-Control-Allow-Origin", request.getHeader("Origin"))
                    .header("Access-Control-Allow-Credentials", true)
                    .build();
        } catch (IOException e) {
            ExceptionUtil eu = new ExceptionUtil(exception);
            return Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity("{ \"error\" : {\"validatie\":[\"" + eu.getEscapedConcatenatedMessage() + "\"]}}")
                    .header("Access-Control-Allow-Origin", request.getHeader("Origin"))
                    .header("Access-Control-Allow-Credentials", true)
                    .build();
        }
    }

    /**
     * Get String with exception as JSON.
     *
     * @param exception exception to convert
     * @return exception converted to JSON
     * @throws IOException oops
     */
    public String getJsonString(MethodConstraintViolationException exception) throws IOException {
        return "{ \"error\" : " + jsonMapper.writeValueAsString(getViolations(exception)) + "}";
    }

    private Map<String, List<String>> getViolations(MethodConstraintViolationException mcve) {
        Map<String, List<String>> res = new HashMap<String, List<String>>();
        for (ConstraintViolation cv : mcve.getConstraintViolations()) {
            String path = "";
            if (null != cv.getPropertyPath()) {
                path = cv.getPropertyPath().toString();
            }
            List<String> msgs = res.get(path);
            if (null == msgs) {
                msgs = new ArrayList<String>();
                res.put(path, msgs);
            }
            msgs.add(cv.getMessage());
        }
        return res;
    }

}
