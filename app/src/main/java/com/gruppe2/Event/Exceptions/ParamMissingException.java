package com.gruppe2.Event.Exceptions;

/**
 * Created by myles on 03.06.15.
 */
public class ParamMissingException extends Exception {
    public ParamMissingException(String param){
        super("Angabe: '" + param + "' fehlt");
    }

}
