/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.exception;

/**
 * Exception which can be thrown if a resource if not found.
 * <p/>
 * Guideline is to use this exception when an object with is references through a non-optional parameter cannot be
 * accessed. This could be because the object does not exist or because of insufficient authorization. The non-optional
 * parameter could be a path parameter, a query parameter or a required field in the body.
 */
public class NotFoundException extends AbstractRestException {

    /**
     * No-arguments constructor.
     */
    public NotFoundException() {
    }

    /**
     * Constructor with message.
     *
     * @param message message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     *
     * @param message message
     * @param cause cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with cause.
     *
     * @param cause cause
     */
    public NotFoundException(Throwable cause) {
        super(cause);
    }

}
