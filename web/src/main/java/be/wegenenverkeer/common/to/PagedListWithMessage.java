/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.to;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * {@link PagedList} with additional message.
 *
 * @param <T> transfer object type
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PagedListWithMessage<T> extends PagedList<T> {

    private String message;

    /**
     * Constructor with total count and list of items in selected page.
     *
     * @param total total count
     * @param items items in current page
     */
    public PagedListWithMessage(int total, List<T> items) {
        super(total, items);
    }

    /**
     * Constructor.
     *
     * @param total total count
     * @param items items in current page
     * @param message message
     */
    public PagedListWithMessage(int total, List<T> items, String message) {
        super(total, items);
        setMessage(message);
    }

}