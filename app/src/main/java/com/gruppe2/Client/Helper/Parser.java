package com.gruppe2.Client.Helper;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *Parsed Datumsobjekte zu String und Strings zu Datumsobjekten
 *
 *@author  Myles Sutholt
 */
public class Parser {

    Date date;
    public Date StringToDate(String strDate){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            simpleDateFormat.setLenient(false);
            date = simpleDateFormat.parse(strDate);
        }
        catch (ParseException e){
            return null;
        }
        return date;
    }
    public String DateToString(Date date){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate = df.format(date);
        return (df.format(date));
    }
    public String DateToStringTime(Date date){
        DateFormat df = new SimpleDateFormat("HH:mm");
        String strDate = df.format(date);
        return (df.format(date));
    }
    public String DateToStringDate(Date date){
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
    public Date DateToTime(Date date){
        try {
            DateFormat df = new SimpleDateFormat("HH:mm");
            return df.parse(df.format(date));
        }
        catch(ParseException e){
            return null;
        }
    }
    public Date StringToTempDate(String date){
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            df.setLenient(false);
            Date newdate = df.parse(date);
            Date finDate = DateToDate(newdate);
            Log.d("Test","");
            return finDate;
        }
        catch(ParseException e){
            return null;
        }
    }
    public Date DateToDate(Date date){
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            return df.parse(df.format(date));
        }
        catch(ParseException e){
            return null;
        }
    }
}
