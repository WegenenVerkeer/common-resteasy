/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.web;

import org.jboss.resteasy.annotations.GZIP;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Service which allows gathering and clearing the profiling information.
 */
@Path("/rest")
@Service
public interface SampleService {

    /**
     * Sample call.
     * Does a dummy gateway call when the length of the query is even.
     *
     * @param query optional query string, just for logging
     * @return The status
     */
    @GET
    @GZIP
    @Produces("text/plain")
    @Path("sample")
    String sample(@QueryParam("q") String query);

    /**
     * Another sample call.
     * Does a dummy gateway call when the length of the query is even.
     *
     * @param query optional query string, just for logging
     * @return The status
     */
    @GET
    @GZIP
    @Produces("text/plain")
    @Path("other")
    String other(@QueryParam("q") String query);

}
