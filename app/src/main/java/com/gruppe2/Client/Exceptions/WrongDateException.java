package com.gruppe2.Client.Exceptions;

/**
Exception, wenn Daten falsch eingegeben worden sind

 @author Myles Sutholt

 */
public class WrongDateException extends Exception {
    public WrongDateException(String msg){
        super(msg);
    }
}
