package com.gruppe2.Client.Helper;

import com.gruppe2.Client.Exceptions.WrongDateException;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Objects.Session;

import java.util.Calendar;

/**@author Myles Sutholt
Validiert das Datum (Enddatum nach Startdatum)
 */
public class Validade {

    public boolean validateDates(Session session) throws WrongDateException {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(session.getDateStart());
        cal2.setTime(session.getDateEnd());
        if (cal1.after(cal2)){
            throw new WrongDateException("406");
        }
        else{
            return true;
        }
    }
    public boolean validateDates(Event event) throws WrongDateException {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(event.getDateStart());
        cal2.setTime(event.getDateEnd());
        if (cal1.after(cal2)){
            throw new WrongDateException("428");
        }
        else{
            return true;
        }
    }

}
