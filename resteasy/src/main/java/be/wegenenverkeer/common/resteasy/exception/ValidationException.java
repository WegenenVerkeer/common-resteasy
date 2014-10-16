/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exception which is thrown in case of a validation exception.
 * <p/>
 * In principle validation problems are caused by invalid user input (though not necessarily from user input
 * which is passed in this call).
 * <p/>
 * This can capture several validation problems, each marked with a key (field) which failed validation.
 * Several validation messages can be given for each key.
 */
public class ValidationException extends AbstractRestException {

    /* key: field name, value: exception message */
    private final Map<String, List<String>> exceptions =
            Collections.synchronizedMap(new HashMap<String, List<String>>());


    /**
     * No-arguments constructor.
     */
    public ValidationException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param field Het veld waarop de boodschap betrekking heeft
     * @param boodschap De boodschap
     */
    public ValidationException(String field, String boodschap) {
        super();
        addException(field, boodschap);
    }

    /**
     * Voeg een foutboodschap toe aan deze exception.
     *
     * @param field Het veld waarop de boodschap betrekking heeft
     * @param boodschap De boodschap
     */
    public void addException(String field, String boodschap) {
        if (!exceptions.containsKey(field)) {
            exceptions.put(field, new ArrayList<String>());
        }

        exceptions.get(field).add(boodschap);
    }

    /**
     * Geeft de exceptions terug.
     *
     * @return  de exceptions
     */
    public Map<String, List<String>> getExceptions() {
        return Collections.unmodifiableMap(exceptions);
    }

    /**
     * Voegt alle exceptions in de gegeven exception toe aan deze ValidationException.
     * Voeg de map met exceptions toe aan deze exception.
     * @param ve De toe te voegen exceptions
     */
    public void addAll(ValidationException ve) {
        if (ve != null) {
            exceptions.putAll(ve.getExceptions());
        }
    }

    /**
     * Ga na of dit object errors bevat.
     *
     * @return True indien dit object errors bevat. Anders false
     */
    public boolean hasErrors() {
        return !exceptions.isEmpty();
    }

    @Override
    public String getMessage() {
        return toString();
    }

    @Override
    public String toString() {
        return "ValidationException {" +
                "exceptions=" + exceptions +
                '}';
    }

}
