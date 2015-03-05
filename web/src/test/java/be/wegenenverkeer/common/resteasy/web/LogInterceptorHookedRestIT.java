/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.web;

import com.jayway.restassured.RestAssured;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test which verifies proper functioning of the
 * {@link be.wegenenverkeer.common.resteasy.logging.PreProcessLoggingInterceptor}.
 *
 * We do this in a roundabout way by verifying that the profile information is gathered.
 */
public class LogInterceptorHookedRestIT {

    static {
        RestAssured.baseURI = "http://localhost";
        int port = 8123;
        String altPort = System.getProperty("cr.server.port");
        if (StringUtils.isNotBlank(altPort)) {
            port = Integer.parseInt(altPort);
        }
        System.out.println("Connecting to back-end on port " + port);
        RestAssured.port = port;
        RestAssured.basePath = "/common-resteasy";
    }

    @Test
    public void testProfileWired() throws Exception {
        String response;

        // clean profile info

        response = given().header("accept", "text/plain").when().get("/rest/profile?clear=true").asString();

        assertThat(response).contains("++++ REST calls:");

        // verify expectations to prevent false positives

        response = given().header("accept", "text/plain").when().get("/rest/profile?clear=true").asString();

        assertThat(response).contains("++++ REST calls:");
        assertThat(response).doesNotContain("SampleServiceImpl:sample");

        // do actual "testing" call

        response= given().header("accept", "text/plain").when().get("/rest/sample").asString();

        assertThat(response).contains("done at");

        // verify that the testing call was registered

        response = given().header("accept", "text/plain").when().get("/rest/profile").asString();

        assertThat(response).contains("++++ REST calls:");
        assertThat(response).contains("SampleServiceImpl:sample");
    }

}
