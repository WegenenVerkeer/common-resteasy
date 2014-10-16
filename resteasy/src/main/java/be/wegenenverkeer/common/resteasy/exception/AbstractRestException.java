/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.exception;

/**
 * Overkoepelende exception die gegooid wordt telkens er een fout optreedt in een rest service methode.
 */
public abstract class AbstractRestException extends RuntimeException {

    /**
     * No-arguments constructor.
     */
    protected AbstractRestException() {
    }

    /**
     * Constructor with message.
     *
     * @param message message
     */
    protected AbstractRestException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     *
     * @param message message
     * @param cause cause
     */
    protected AbstractRestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with cause.
     *
     * @param cause cause
     */
    protected AbstractRestException(Throwable cause) {
        super(cause);
    }

}
