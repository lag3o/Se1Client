package com.gruppe2.Client.Exceptions;

/**
Exception falls Parameter bei der Erzeugung fehlen


 @author Myles Sutholt
 */
public class ParamMissingException extends Exception {
    public ParamMissingException(String param){
        super("Angabe: '" + param + "' fehlt");
    }

}
