/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.logging;

import be.eliwan.profiling.api.ProfilingSink;
import be.wegenenverkeer.common.resteasy.exception.AbstractRestException;
import be.wegenenverkeer.common.resteasy.exception.ExceptionUtil;
import be.wegenenverkeer.common.resteasy.exception.ServiceException;
import be.wegenenverkeer.common.resteasy.json.InputStreamSerializer;
import be.wegenenverkeer.common.resteasy.json.RestJsonMapper;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.MessageBodyReaderContext;
import org.jboss.resteasy.spi.interception.MessageBodyReaderInterceptor;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;


/**
 * Deze klasse is een resteasy interceptor die zichzelf tevens exposed als een spring-bean. De interceptor haakt
 * zichzelf voor en na elke call en realiseert degelijke logging van elke call waarbij getracht wordt data van
 * verschillende stadia van de executie van eenzelfde request samen te houden in het kader van concurrent logs. De
 * interceptor kan gebruikt worden als logger voor servicecode: de logging van de gebruiker wordt dan mee in de output
 * van deze logger gestopt.
 */
@Provider
@Component("loggerInterceptor")
@ServerInterceptor
public class PreProcessLoggingInterceptor
        implements InitializingBean, PreProcessInterceptor, MessageBodyReaderInterceptor, PostProcessInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(PreProcessLoggingInterceptor.class);
    private static final RestJsonMapper MAPPER = new RestJsonMapper();
    private static final String NEWLINE = "\n";
    private static final String INDENT = "\n\t";
    private static final String ARROW = " -> ";
    private static final ThreadLocal<StringBuilder> STRING_BUILDER = new ThreadLocal<>();

    /**
     * String indicating the grouping for the profiling. Each service handled independently..
     */
    public static final ThreadLocal<String> PROFILE_GROUP = new ThreadLocal<>();

    /**
     * Service request URL.
     */
    public static final ThreadLocal<Long> START_MOMENT = new ThreadLocal<>();

    @Autowired(required = false)
    @Qualifier("restProfilingRegistrar")
    private ProfilingSink profilingContainer = (group, duration) -> {
        // do nothing
    };


    @Override
    public void afterPropertiesSet() throws Exception {
        MAPPER.addClassSerializer(InputStream.class, new InputStreamSerializer());
    }

    /**
     * Implementatie van de PreProcessInterceptor interface.
     *
     * @param request De request
     * @param method De method
     * @return De server response
     * @throws Failure De failure exception
     * @throws WebApplicationException De web application exception
     */
    @Override
    public ServerResponse preProcess(HttpRequest request, ResourceMethodInvoker method)
            throws Failure, WebApplicationException {
        START_MOMENT.set(System.currentTimeMillis());
        PROFILE_GROUP.set(method.getMethod().getDeclaringClass().getSimpleName() + ":" + method.getMethod().getName());
        STRING_BUILDER.set(new StringBuilder("Service: "));
        StringBuilder sb = STRING_BUILDER.get();
        sb.append(request.getHttpMethod());
        sb.append(' ');
        sb.append(request.getUri().getAbsolutePath().toASCIIString());

        // log HTTP request headers
        sb.append("\nHTTP request headers:");
        for (Map.Entry<String, List<String>> entry : request.getHttpHeaders().getRequestHeaders().entrySet()) {
            sb.append("\n    ").append(entry.getKey()).append(": ");
            String sep = "";
            for (String s : entry.getValue()) {
                sb.append(sep);
                sep = ", ";
                sb.append(s);
            }
        }

        if (null != method.getConsumes()) {
            sb.append("\nRequest types");
            for (MediaType mediaType : method.getConsumes()) {
                sb.append(' ').append(mediaType.toString());
            }
        }
        if (null != method.getProduces()) {
            sb.append("\nResponse types");
            for (MediaType mediaType : method.getProduces()) {
                sb.append(' ').append(mediaType.toString());
            }
        }
        sb.append("\nCookies: ");
        Map<String, Cookie> cookies = request.getHttpHeaders().getCookies();
        for (Map.Entry<String, Cookie> entry : cookies.entrySet()) {
            sb.append(INDENT);
            sb.append(entry.getKey());
            sb.append(ARROW);
            sb.append(entry.getValue());
        }
        sb.append("\nQuery Parameters: ");
        MultivaluedMap<String, String> params = request.getUri().getQueryParameters();
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            sb.append(INDENT);
            sb.append(entry.getKey());
            sb.append(ARROW);
            sb.append(entry.getValue());
        }
        sb.append("\nPath parameters: ");
        MultivaluedMap<String, String> pathParams = request.getUri().getPathParameters();
        for (Map.Entry<String, List<String>> entry : pathParams.entrySet()) {
            sb.append(INDENT);
            sb.append(entry.getKey());
            sb.append(ARROW);
            sb.append(entry.getValue());
        }
        return null;
    }

    /**
     * Implementatie van de MessageBodyReaderInterceptor interface.
     *
     * @param context de service context
     * @return deze methode geeft gewoon het antwoord van de volgende reader in de chain terug
     * @throws IOException indien de vorige reader deze exception gooit
     */
    @Override
    public Object read(MessageBodyReaderContext context) throws IOException {
        Object result = context.proceed();
        StringBuilder sb = STRING_BUILDER.get();
        sb.append("\nDocument body type: ").append(result.getClass().toString());
        sb.append("\nDocument content:\n");
        if (result.getClass().isAnnotationPresent(DoNotLog.class)) {
            sb.append("<Not serialized " + result.getClass().toString() + ">");
        } else if (result.getClass().isAnnotationPresent(LogUsingToString.class)) {
            sb.append(result.toString());
        } else {
            sb.append(MAPPER.writeValueAsString(result));
        }
        return result;
    }


    /**
     * Implementatie van de PostProcessInterceptor interface.
     *
     * @param response server response
     */
    @Override
    public void postProcess(ServerResponse response) {
        StringBuilder sb = STRING_BUILDER.get();
        if (null == sb) {
            sb = new StringBuilder();
            STRING_BUILDER.set(sb);
        }
        Object result = response.getEntity();
        if (result != null) {
            sb.append("\nReply type: ");
            sb.append(result.getClass().toString());
            sb.append("\nOutput document:\n");

            try {
                if (result.getClass().isAnnotationPresent(DoNotLog.class)) {
                    sb.append("<Not serialized " + result.getClass().toString() + ">");
                } else if (contains(response.getAnnotations(), DoNotLogResponse.class)) {
                    sb.append(String.format("<Not serialized response from method '%s>",
                            PROFILE_GROUP.get()));
                } else if (result.getClass().isAnnotationPresent(LogUsingToString.class)) {
                    sb.append(result.toString());
                } else if (result instanceof String) {
                    sb.append(result);
                } else {
                    String output = MAPPER.writeValueAsString(result);
                    sb.append(output);
                }
            } catch (IOException e) {
                LOG.warn("JSON probleem met " + result, e);
            }
        }
        finishCall(false);
    }

    /**
     * Afsluitende logging in geval van een error.
     *
     * @param exception te loggen fout
     * @param msg boodschap
     */
    public void postProcessError(Exception exception, String msg) {
        StringBuilder sb = STRING_BUILDER.get();
        if (null == sb) {
            sb = new StringBuilder();
            STRING_BUILDER.set(sb);
        }
        sb.append("\nOOPS: ").append(msg).append(NEWLINE);
        ExceptionUtil eu = new ExceptionUtil(exception);
        if (exception instanceof AbstractRestException && !(exception instanceof ServiceException)) {
            // no stack trace, log at info level
            finishCall(false);
        } else {
            sb.append(eu.getStackTrace());
            finishCall(true);
        }
    }

    private void finishCall(boolean isError) {
        StringBuilder sb = STRING_BUILDER.get();
        long now = System.currentTimeMillis();
        Long start = START_MOMENT.get();
        if (null != start) {
            long time = now - start;
            profilingContainer.register(PROFILE_GROUP.get(), time);
            sb.append(String.format("%nDuur: %.3fs", time / 1000.0));
        } else {
            sb.append("\nDuur: Onbekend, kan starttijd niet bepalen.");
        }
        if (isError) {
            LOG.error(sb.toString());
        } else {
            LOG.info(sb.toString());
        }

        PROFILE_GROUP.remove();
        START_MOMENT.remove();
        STRING_BUILDER.remove();
    }

    private boolean contains(Annotation[] list, Class annotation) {
        for (Annotation test : list) {
            System.out.println(test);
            if (test.getClass().isAssignableFrom(annotation)) {
                return true;
            }
        }
        return false;
    }

}
