package com.gruppe2.Client.Exceptions;

/**@author Myles Sutholt
Exception, wenn Daten falsch eingegeben worden sind
 */
public class WrongDateException extends Exception {
    public WrongDateException(String msg){
        super(msg);
    }
}
