package com.gruppe2.Client.Helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**@author: Myles Sutholt
Parsed Datumsobjekte zu String und Strings zu Datumsobjekten
 */
public class Parser {

    Date date;
    public Date StringToDate(String strDate){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            simpleDateFormat.setLenient(false);
            date = simpleDateFormat.parse(strDate);
        }
        catch (ParseException e){
            return null;
        }
        return date;
    }
    public Date StringToTime(String strDate){

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            simpleDateFormat.setLenient(false);
            date = simpleDateFormat.parse(strDate);
            return date;
        }
        catch (ParseException e){
            return null;
        }
    }

    public String TimeToString(Date date){
        DateFormat df = new SimpleDateFormat("HH:mm");

        return (df.format(date));
    }
    public String DateToString(Date date){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = df.format(date);
        return (df.format(date));
    }
    public Date DatetoDate(Date date){
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.parse(df.format(date));
        }
        catch(ParseException e){
            return null;
        }
    }
}
