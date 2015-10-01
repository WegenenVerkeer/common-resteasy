/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.exception;

/**
 * Exception that is thrown in case of a conflict.
 */
public class ConflictException extends AbstractRestException {

    /**
     * Constructor.
     * @param message A message explaining the nature of the error.
     */
    public ConflictException(String message) {
        super(message);
    }
}
