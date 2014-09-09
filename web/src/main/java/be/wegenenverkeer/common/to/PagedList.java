/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.to;

import lombok.Data;

import java.util.List;

/**
 * TO that allows to return paged results.
 *
 * @param <T> transfer object type
 */
@Data
public class PagedList<T> {

    private int total;
    private List<T> items;


    /**
     * No-arguments constructor.
     */
    public PagedList() {
    }

    /**
     * Constructor with total count and list of items in selected page.
     *
     * @param total total count
     * @param items items in current page
     */
    public PagedList(int total, List<T> items) {
        this.total = total;
        this.items = items;
    }

}