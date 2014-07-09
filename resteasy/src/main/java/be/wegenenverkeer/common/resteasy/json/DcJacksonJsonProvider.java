/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.json;

import org.codehaus.jackson.jaxrs.Annotations;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.ws.rs.ext.Provider;

/**
 * Register Jackson JSON provider for use by RESTEasy.
 */
@Provider
@Component
public class DcJacksonJsonProvider extends JacksonJsonProvider {

    /**
     * No-arguments constructor.
     */
    public DcJacksonJsonProvider() {
        this(new DcJsonMapper());
    }

    /**
     * Constructor.
     *
     * @param annotationsToUse annotations to use
     */
    public DcJacksonJsonProvider(Annotations... annotationsToUse) {
        this(new DcJsonMapper(), annotationsToUse);
    }

    /**
     * Constructor.
     *
     * @param mapper object mapper to use - overwritten by our object mapper.
     */
    public DcJacksonJsonProvider(ObjectMapper mapper) {
        super(new DcJsonMapper());
    }

    /**
     * Constructor.
     *
     * @param mapper object mapper to use - overwritten by our object mapper.
     * @param annotationsToUse annotations to use
     */
    public DcJacksonJsonProvider(ObjectMapper mapper, Annotations[] annotationsToUse) {
        super(new DcJsonMapper(), annotationsToUse);
    }

}
