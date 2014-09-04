/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.web;

import be.eliwan.profiling.service.ProfilingContainer;
import be.wegenenverkeer.common.resteasy.logging.PreProcessLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Service which allows gathering and clearing the profiling information.
 */
@Path("/rest")
@Service
public class SampleServiceImpl implements SampleService {

    private static final Logger LOG = LoggerFactory.getLogger(SampleServiceImpl.class);

    @Autowired
    @Qualifier("gatewayMethodProfiling")
    private ProfilingContainer gatewayMethodProfilingContainer;

    @Autowired
    @Qualifier("gatewayServiceProfiling")
    private ProfilingContainer gatewayServiceProfilingContainer;

    private Random random = new Random(System.currentTimeMillis());


    @Override
    public String sample(@QueryParam("q") String query) {
        return sample(query, "gateway");
    }

    @Override
    public String other(@QueryParam("q") String query) {
        return sample(query, "starship");
    }

    private String sample(String query, String gatewayMethod) {
        waitALittle();
        if (null == query || (query.length() % 2) == 0) {
            // do a dummy gateway call
            callGateway(gatewayMethod);
        }
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        return "done at " + dateFormat.format(new Date());
    }


    private void waitALittle() {
        try {
            Thread.sleep(random.nextInt(2000));
        } catch (InterruptedException ie) {
            LOG.debug("Sleep was interrupted", ie);
        }
    }

    private void callGateway(String gatewayMethod) {
        long start = System.currentTimeMillis();
        try {
            waitALittle();
        } finally {
            long durationMillis = System.currentTimeMillis() - start;
            gatewayMethodProfilingContainer.register(gatewayMethod, durationMillis);
            gatewayServiceProfilingContainer.register(PreProcessLoggingInterceptor.PROFILE_GROUP.get(), durationMillis);
        }
    }


}
