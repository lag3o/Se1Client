package com.gruppe2.Client.Exceptions;

/**@author Myles Sutholt
Exception falls der Server nicht erreichbar ist
 */
public class NoServerException extends Exception {
    public NoServerException(String msg){
        super(msg);
    }
}
