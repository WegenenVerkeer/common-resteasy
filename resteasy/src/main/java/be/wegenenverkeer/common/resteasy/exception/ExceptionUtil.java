/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.exception;

import java.io.InvalidClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * ExceptionUtil allows you to get more information about an exception, without having to investigate the causes.
 * <p/>
 * Originally from equanda, http://equanda.svn.sourceforge.net/, class org/equanda/util/ExceptionUtil.java.
 */
public class ExceptionUtil {

    private static final String[] FILTER_PREFIX = {
            "java.lang.reflect.Method",
            "org.apache.catalina",
            "org.eclipse.jetty",
            "org.springframework.aop",
            "org.springframework.cglib",
            "org.springframework.security",
            "org.springframework.transaction",
            "org.springframework.web",
            "sun.reflect",
            "net.sf.cglib",
    };
    private static final String[] FILTER_CONTAINS = {
            "ByCGLIB$$",
    };


    private boolean isRetryable;
    private boolean isRecoverable;
    private String shortMessage;
    private String concatenatedMessage;
    private Throwable originalException;


    /**
     * Construct instance which allows investigating the exception.
     *
     * @param originalException exception to investigate
     */
    public ExceptionUtil(Throwable originalException) {
        this.originalException = originalException;
        isRetryable = false;
        isRecoverable = true;
        Throwable exc = originalException;
        Throwable lastCause = exc;
        StringBuilder concat = new StringBuilder();

        if (exc instanceof InvocationTargetException && null != exc.getCause()) {
            exc = exc.getCause();
        }

        for (Throwable c = exc; c != null; c = getCause(c)) {
            if (concat.length() > 0) {
                concat.append("; ");
            }
            concat.append(getMessage(c));

            String cn = c.getClass().getName().toLowerCase(Locale.ENGLISH);
            String em = c.getMessage();
            if (em == null) {
                em = "";
            }
            em = em.toLowerCase(Locale.ENGLISH);
            if (c instanceof java.rmi.UnmarshalException || c instanceof InvalidClassException ||
                    c instanceof ClassNotFoundException || c instanceof LinkageError ||
                    c instanceof VirtualMachineError || c instanceof IllegalAccessException) {
                isRecoverable = false;
            } else {
                if (cn.contains("deadlock") || em.contains("deadlock")
                        || cn.contains("staleobjectexception") || em.contains("staleobjectexception")
                        || cn.contains("sockettimeoutexception") || em.contains("sockettimeoutexception")
                        || cn.contains("communicationexception") || em.contains("communicationexception")
                        || cn.contains("concurrentmodificationexception")) {
                    isRetryable = true;
                }
            }
        }

        concatenatedMessage = concat.toString();
        shortMessage = getMessage(lastCause);
    }

    /**
     * Is the exception recoverable?
     *
     * @return is it likely that the error can be recovered from, or should we exit?
     */
    public boolean isRecoverable() {
        return isRecoverable;
    }

    /**
     * Can the operation which caused the exception be retried?
     *
     * @return would it be useful to retry the operation?
     */
    public boolean isRetryable() {
        return isRetryable;
    }

    /**
     * Get the compact error message.
     *
     * @return compact error message
     */
    public String getMessage() {
        return shortMessage;
    }

    /**
     * Get concatenated message, a sequence of the (short) messages for the exception and the causes.
     *
     * @return concatenated messages for the exception and causes.
     */
    public String getConcatenatedMessage() {
        return concatenatedMessage;
    }

    /**
     * Get concatenated message, a sequence of the (short) messages for the exception and the causes.
     * <p/>
     * The response is usable inside double quotes.
     *
     * @return concatenated messages for the exception and causes.
     */
    public String getEscapedConcatenatedMessage() {
        return getConcatenatedMessage().
                replace('"', '\'').
                replace('\t', ' ').
                replace("\n", "\\n").
                replace("\r", "\\r");
    }

    /**
     * Get the stack trace as a string. Filters framework lines from the trace.
     *
     * @return stack trace
     */
    public String getStackTrace() {
        StringBuilder sb = new StringBuilder();
        Throwable exception = originalException;

        while (null != exception) {
            int filteredCount = 0;
            sb.append(exception.getMessage()).append('\n');
            for (StackTraceElement ste : exception.getStackTrace()) {
                if (shouldDisplay(ste.getClassName())) {
                    sb.append("    ");
                    while (filteredCount > 0) {
                        sb.append(',');
                        filteredCount--;
                    }
                    sb.append(ste.toString()).append('\n');
                } else {
                    filteredCount++;
                }
            }
            exception = exception.getCause();
        }
        return sb.toString();
    }

    private boolean shouldDisplay(String className) {
        for (String filter : FILTER_PREFIX) {
            if (className.startsWith(filter)) {
                return false;
            }
        }
        for (String filter : FILTER_CONTAINS) {
            if (className.contains(filter)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Extract a "sensible", reasonably compact message.
     *
     * @param exc exception to get message for
     * @return error message
     */
    private String getMessage(Throwable exc) {
        String msg;

        // see if there is a "getShortMessage" method
        Class clazz = exc.getClass();
        try {
            Method method = clazz.getMethod("getShortMessage");
            msg = (String) method.invoke(exc);
        } catch (Exception ex) {
            msg = null; /*ignore*/
        }

        // fallback to normal message or else class name
        if (msg == null || "null".equals(msg)) {
            msg = exc.getMessage();
        }
        if (msg == null || "null".equals(msg)) {
            msg = exc.toString();
        }

        return msg;
    }

    /**
     * Return the cause exception.
     *
     * @param exc exception to get message for
     * @return cause exception if any
     */
    private Throwable getCause(Throwable exc) {
        Throwable cause = exc.getCause();
        // use the roundabout way to figure out if there is a cause exception if none found easily
        // some classes, like EJBException need to be java 1.3 compatible and have a getCausedByException method
        if (cause == null) {
            Class clazz = exc.getClass();
            try {
                Method method = clazz.getMethod("getCausedByException");
                cause = (Throwable) method.invoke(exc);
            } catch (Exception ex) {
                cause = null; /*ignore*/
            }
        }
        return cause;
    }

}