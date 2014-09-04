/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.web;

import org.jboss.resteasy.annotations.GZIP;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Service which allows gathering and clearing the profiling information.
 */
@Path("/rest/profile")
public interface ProfilingService {

    /**
     * Get the status of the application.
     *
     * @param clear moeten de tellers terug op nul gezet worden (na resultaat)?
     * @return The status
     */
    @GET
    @GZIP
    @Produces("text/plain")
    String profile(@QueryParam("clear") @DefaultValue("false") boolean clear);

}
