/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.exception;

/**
 * Exception which can be thrown if authentication or authorization failed.
 * <p/>
 * Note that from a security point of view, it is better to use {@link NotFoundException} when the trying is not
 * authorized to access a particular resource. For a normal user this distinction is not important. For a malignant
 * user an {@link AuthException} can be a carrot to find ways to access the resource.
 */
public class AuthException extends AbstractRestException {

    /**
     * No-arguments constructor.
     */
    public AuthException() {
    }

    /**
     * Constructor with message.
     *
     * @param message message
     */
    public AuthException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause.
     *
     * @param message message
     * @param cause cause
     */
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with cause.
     *
     * @param cause cause
     */
    public AuthException(Throwable cause) {
        super(cause);
    }

}
