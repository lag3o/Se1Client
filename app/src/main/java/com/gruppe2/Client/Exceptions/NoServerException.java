package com.gruppe2.Client.Exceptions;

/**
Exception falls der Server nicht erreichbar ist

 @author Myles Sutholt

 */
public class NoServerException extends Exception {
    public NoServerException(String msg){
        super(msg);
    }
}
