/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.logging;

/**
 * SPI for capturing profiling data.
 */
public interface ProfilingRegistrar {

    /**
     * Add a registration for a group.
     *
     * @param group group name
     * @param duration duration
     */
    void register(String group, long duration);

}
