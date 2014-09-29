/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate that the class should be logged using the {@link Object#toString()} in the PreProcessLoggingInterceptor.
 * <p/>
 * Note: only works when annotated at the top-level, being the type for the body or response object.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogUsingToString {
}
