package com.gruppe2.Event.Database;

/**
 * Created by myles on 20.05.15.
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import static com.gruppe2.Event.Helper.Constants.NAME;
import static com.gruppe2.Event.Helper.Constants.START;
import static com.gruppe2.Event.Helper.Constants.END;
import static com.gruppe2.Event.Helper.Constants.LOC;
import static com.gruppe2.Event.Helper.Constants.DESCR;
import static com.gruppe2.Event.Helper.Constants.ID;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.gruppe2.Event.Exceptions.ParamMissingException;
import com.gruppe2.Event.Helper.Parser;
import com.gruppe2.Event.Objects.Event;
import com.gruppe2.Event.Objects.Session;

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
        values.put(MySQLiteHelper.EVENT_ID, event.getId());
        values.put(MySQLiteHelper.EVENT_NAME, event.getName());
        values.put(MySQLiteHelper.EVENT_DATE_START, (new Parser().DateToString(event.getStartDate())));
        values.put(MySQLiteHelper.EVENT_DATE_END, (new Parser().DateToString(event.getEndDate())));
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
        values.put(MySQLiteHelper.SESSION_DATE_START, (new Parser().TimeToString(session.getDateStart())));
        values.put(MySQLiteHelper.SESSION_DATE_END, (new Parser().TimeToString(session.getDateEnd())));
        values.put(MySQLiteHelper.SESSION_LOCATION, session.getLocation());
        values.put(MySQLiteHelper.SESSION_PLZ, session.getPlz());
        values.put(MySQLiteHelper.SESSION_DESCRIPTION, session.getDescription());

        long insertId = database.insert(MySQLiteHelper.TABLE_SESSIONS, null,
                values);
        try {
            session.setId((int) insertId);
        }
        catch (ParamMissingException exception){

        }
    }

    public void deleteEvent(Event event) {
        long id = event.getId();
        System.out.println("Event deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_EVENTS, MySQLiteHelper.EVENT_ID
                + " = " + id, null);
        database.delete(MySQLiteHelper.TABLE_SESSIONS, MySQLiteHelper.EVENT_ID
                + " = " + id, null);
    }

    public ArrayList<Event> getAllNames() {
        ArrayList<Event> events = new ArrayList<Event>();

        Cursor cursor = database.rawQuery("Select * FROM " + MySQLiteHelper.TABLE_EVENTS, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Event event = new Event();
            String eventName = cursorToEvent(cursor).getName();
            int id = cursorToEvent(cursor).getId();
            Date start = (cursorToEvent(cursor).getStartDate());
            try {
                event.setName(eventName);
                event.setID(id);
                event.setStartDate(start);
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
                MySQLiteHelper.EVENT_ID + " = " + id, null);
        cursor.moveToFirst();
        ArrayList<Session> sessions = new ArrayList<Session>();
        while (!cursor.isAfterLast()) {
            HashMap<String, String> session = new HashMap<String, String>();
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
            event.setID(cursor.getInt(0));
            event.setName(cursor.getString(1));
            event.setStartDate(new Parser().StringToDate(cursor.getString(2)));
            event.setEndDate(new Parser().StringToDate(cursor.getString(3)));
            event.setDescription(cursor.getString(4));
        }
        catch (ParamMissingException exception){

        }
        return event;
    }
    private Session cursorToSession(Cursor cursor){
        Session session = new Session();
        try {
            session.setId(cursor.getInt(0));
            session.setName(cursor.getString(2));
            String str =cursor.getString(3);
            session.setDateStart(new Parser().StringToTime(str));
            session.setDateEnd(new Parser().StringToTime(cursor.getString(4)));
            session.setLocation(cursor.getString(5));
            session.setDescription(cursor.getString(7));
            session.setPlz(cursor.getString(6));
        }
        catch (ParamMissingException exception){

        }
        return session;
    }
}

