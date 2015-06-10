package com.gruppe2.Client.Exceptions;

/**@author Myles Sutholt
Exception falls Parameter bei der Erzeugung fehlen
 */
public class ParamMissingException extends Exception {
    public ParamMissingException(String param){
        super("Angabe: '" + param + "' fehlt");
    }

}
