package com.gruppe2.Event.Helper;

import com.gruppe2.Event.Exceptions.WrongDateException;
import com.gruppe2.Event.Objects.Event;
import com.gruppe2.Event.Objects.Session;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by myles on 04.06.15.
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
        cal1.setTime(event.getStartDate());
        cal2.setTime(event.getEndDate());
        if (cal1.after(cal2)){
            throw new WrongDateException("428");
        }
        else{
            return true;
        }
    }

}
