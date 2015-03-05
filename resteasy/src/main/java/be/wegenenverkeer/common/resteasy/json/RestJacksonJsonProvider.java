/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

/**
 * Register Jackson JSON provider for use by RESTEasy.
 */
@Provider
@Component
@Consumes({ MediaType.APPLICATION_JSON, "application/*+json", "text/json" })
@Produces({ MediaType.APPLICATION_JSON, "application/*+json", "text/json" })
public class RestJacksonJsonProvider extends JacksonJsonProvider {

    /**
     * No-arguments constructor.
     */
    public RestJacksonJsonProvider() {
        this(new RestJsonMapper());
    }

    /**
     * Constructor.
     *
     * @param annotationsToUse annotations to use
     */
    public RestJacksonJsonProvider(Annotations... annotationsToUse) {
        this(new RestJsonMapper(), annotationsToUse);
    }

    /**
     * Constructor.
     *
     * @param mapper object mapper to use - overwritten by our object mapper.
     */
    public RestJacksonJsonProvider(ObjectMapper mapper) {
        super(new RestJsonMapper());
    }

    /**
     * Constructor.
     *
     * @param mapper object mapper to use - overwritten by our object mapper.
     * @param annotationsToUse annotations to use
     */
    public RestJacksonJsonProvider(ObjectMapper mapper, Annotations[] annotationsToUse) {
        super(new RestJsonMapper(), annotationsToUse);
    }

}
