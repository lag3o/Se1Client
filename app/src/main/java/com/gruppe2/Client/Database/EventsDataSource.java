package com.gruppe2.Client.Database;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.gruppe2.Client.Exceptions.ParamMissingException;
import com.gruppe2.Client.Helper.Parser;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Objects.Session;
/**@author  Myles Sutholt
    Diese Klasse persistiert die Daten lokal
 */
public class EventsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public EventsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createEvent(Event event) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.EVENT_ID, event.getEventID());
        values.put(MySQLiteHelper.EVENT_NAME, event.getName());
        values.put(MySQLiteHelper.EVENT_DATE_START, (new Parser().DateToString(event.getDateStart())));
        values.put(MySQLiteHelper.EVENT_DATE_END, (new Parser().DateToString(event.getDateEnd())));
        values.put(MySQLiteHelper.EVENT_DESCRIPTION, event.getDescription());

        long insertId = database.insert(MySQLiteHelper.TABLE_EVENTS, null,
                values);
        for (int i = 0; i<event.getSessions().size(); i++){
            createSession(event.getSessions().get(i), insertId);
        }
    }


    private void createSession(Session session, long eventId) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.EVENT_ID, eventId);
        values.put(MySQLiteHelper.SESSION_NAME, session.getName());
        values.put(MySQLiteHelper.SESSION_DATE_START, (new Parser().DateToString(session.getDateStart())));
        values.put(MySQLiteHelper.SESSION_DATE_END, (new Parser().DateToString(session.getDateEnd())));
        values.put(MySQLiteHelper.SESSION_LOCATION, session.getLocation());
        values.put(MySQLiteHelper.SESSION_PLZ, session.getPlz());
        values.put(MySQLiteHelper.SESSION_DESCRIPTION, session.getDescription());

        long insertId = database.insert(MySQLiteHelper.TABLE_SESSIONS, null,
                values);
        try {
            session.setSessionID((int) insertId);
        }
        catch (ParamMissingException exception){

        }
    }

    public void deleteEvent(Event event) {
        try {
            long id = event.getEventID();

            System.out.println("Event deleted with id: " + id);
            database.delete(MySQLiteHelper.TABLE_EVENTS, MySQLiteHelper.EVENT_ID
                    + " = " + id, null);
            database.delete(MySQLiteHelper.TABLE_SESSIONS, MySQLiteHelper.EVENT_ID
                    + " = " + id, null);
        }
        catch (Exception e){
            Log.i("Create Session Local", "Event nicht vorhanden");
        }
    }

    public ArrayList<Event> getAllNames() {
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.rawQuery("Select * FROM " + MySQLiteHelper.TABLE_EVENTS, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = new Event();
            String eventName = cursorToEvent(cursor).getName();
            int id = cursorToEvent(cursor).getEventID();
            Date start = (cursorToEvent(cursor).getDateStart());
            try {
                event.setName(eventName);
                event.setEventID(id);
                event.setDateStart(start);
            }
            catch (ParamMissingException e){

            }

            events.add(event);

            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }



    public ArrayList<Session> getSessions(int id){
        Cursor cursor = database.rawQuery("Select * FROM " + MySQLiteHelper.TABLE_SESSIONS +" WHERE "+
                MySQLiteHelper.EVENT_ID + " = " + id + " Order by "+ MySQLiteHelper.SESSION_DATE_START, null);
        cursor.moveToFirst();
        ArrayList<Session> sessions = new ArrayList<Session>();
        while (!cursor.isAfterLast()) {
            Session tmpSession = cursorToSession(cursor);
            sessions.add(tmpSession);
            cursor.moveToNext();
        }

        cursor.close();
        return sessions;
    }


    public boolean isEmpty(){
        Cursor cursor = database.rawQuery("Select Count(*) FROM " + MySQLiteHelper.TABLE_EVENTS, null);
        cursor.moveToFirst();
        if(cursor.getInt(0) > 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public int countEvents(){
        Cursor cursor = database.rawQuery("Select Count(*) FROM " + MySQLiteHelper.TABLE_EVENTS, null);
        cursor.moveToFirst();
        int i = cursor.getInt(0);
        cursor.close();
        return i;
    }
    public Event getEvent(int id){
        Cursor cursor = database.rawQuery("Select * FROM " + MySQLiteHelper.TABLE_EVENTS + " Where " +
                MySQLiteHelper.EVENT_ID + " = " + id, null);
        cursor.moveToFirst();
        Event event = cursorToEvent(cursor);
        cursor.close();
        event.setSessions(getSessions(event.getEventID()));
        return event;
    }
    public Integer isActive(){
        Cursor cursor = database.rawQuery("Select * FROM " + MySQLiteHelper.TABLE_EVENTS, null);
        cursor.moveToFirst();
        Date date = new Date();
        Date date2 = (new Parser().StringToDate(cursor.getString(2)));
        Date date3 = (new Parser().StringToDate(cursor.getString(3)));
        boolean b = date.compareTo((new Parser().StringToDate(cursor.getString(2)))) >= 0;
        boolean b2 = date.compareTo((new Parser().StringToDate(cursor.getString(3)))) <= 0;
        if (!cursor.isAfterLast()) {
            if (date.compareTo((new Parser().StringToDate(cursor.getString(2)))) >= 0
                    && date.compareTo((new Parser().StringToDate(cursor.getString(3)))) <= 0){

                    Integer i = (Integer) cursor.getInt(0);
                    cursor.close();
                    return i;
                }
            }

        cursor.close();
        return null;
    }
    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event();
        try {
            event.setEventID(cursor.getInt(0));
            event.setName(cursor.getString(1));
            event.setDateStart(new Parser().StringToDate(cursor.getString(2)));
            event.setDateEnd(new Parser().StringToDate(cursor.getString(3)));
            event.setDescription(cursor.getString(4));
            //event.setSessions(getSessions(cursor.getInt(0)));
        }
        catch (ParamMissingException exception){

        }
        return event;
    }
    private Session cursorToSession(Cursor cursor){
        Session session = new Session();
        try {
            session.setSessionID(cursor.getInt(0));
            session.setName(cursor.getString(2));
            session.setDateStart(new Parser().StringToDate(cursor.getString(3)));
            session.setDateEnd(new Parser().StringToDate(cursor.getString(4)));
            session.setLocation(cursor.getString(5));
            session.setDescription(cursor.getString(7));
            session.setPlz(cursor.getString(6));
        }
        catch (ParamMissingException exception){

        }
        return session;
    }
}

