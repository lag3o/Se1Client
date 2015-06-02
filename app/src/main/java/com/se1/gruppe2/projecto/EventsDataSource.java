package com.se1.gruppe2.projecto;

/**
 * Created by myles on 20.05.15.
 */
import java.util.ArrayList;
import java.util.HashMap;
import static com.se1.gruppe2.projecto.Constants.NAME;
import static com.se1.gruppe2.projecto.Constants.START;
import static com.se1.gruppe2.projecto.Constants.END;
import static com.se1.gruppe2.projecto.Constants.LOC;
import static com.se1.gruppe2.projecto.Constants.DESCR;
import static com.se1.gruppe2.projecto.Constants.ID;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
public class EventsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.EVENT_ID,
            MySQLiteHelper.EVENT_NAME };

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
        values.put(MySQLiteHelper.EVENT_NAME, event.getName());
        values.put(MySQLiteHelper.EVENT_DATE_START, event.getStartDate());
        values.put(MySQLiteHelper.EVENT_DATE_END, event.getEndDate());
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
        values.put(MySQLiteHelper.SESSION_DATE_START, session.getDateStart());
        values.put(MySQLiteHelper.SESSION_DATE_END, session.getDateEnd());
        values.put(MySQLiteHelper.SESSION_LOCATION, session.getLocation());
        values.put(MySQLiteHelper.SESSION_DESCRIPTION, session.getDescription());

        long insertId = database.insert(MySQLiteHelper.TABLE_SESSIONS, null,
                values);
        session.setId((int) insertId);
    }

    public void deleteEvent(Event event) {
        long id = event.getId();
        System.out.println("Event deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_EVENTS, MySQLiteHelper.EVENT_ID
                + " = " + id, null);
        database.delete(MySQLiteHelper.TABLE_SESSIONS, MySQLiteHelper.EVENT_ID
                + " = " + id, null);
    }

    public ArrayList<HashMap<String, String>> getAllNames() {
        ArrayList<HashMap<String, String>> events = new ArrayList<HashMap<String, String>>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HashMap<String, String> event = new HashMap<String, String>();
            String eventName = cursorToEvent(cursor).getName();
            String id = cursorToEvent(cursor).getId().toString();
            String start = cursorToEvent(cursor).getStartDate();
            event.put(NAME, eventName);
            event.put(ID, id);
            event.put(START, start);

            events.add(event);

            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }
    public ArrayList<HashMap<String, String>> getSessions(int id){
        Cursor cursor = database.rawQuery("Select * FROM " + MySQLiteHelper.TABLE_SESSIONS +" WHERE "+
                MySQLiteHelper.EVENT_ID + " = " + id, null);
        cursor.moveToFirst();
        ArrayList<HashMap<String, String>> sessions = new ArrayList<HashMap<String, String>>();
        while (!cursor.isAfterLast()) {
            HashMap<String, String> session = new HashMap<String, String>();
            session.put(NAME, cursorToSession(cursor).getName());
            session.put(START, cursorToSession(cursor).getDateStart());
            session.put(END, cursorToSession(cursor).getDateEnd());
            session.put(LOC,cursorToSession(cursor).getLocation());
            session.put(DESCR,cursorToSession(cursor).getDescription());
            sessions.add(session);
            cursor.moveToNext();
        }

        return sessions;
    }

    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event();
        event.setId(cursor.getInt(0));
        event.setName(cursor.getString(1));
        return event;
    }
    private Session cursorToSession(Cursor cursor){
        Session session = new Session();
        session.setName(cursor.getString(2));
        session.setDateStart(cursor.getString(3));
        session.setDateEnd(cursor.getString(4));
        session.setLocation(cursor.getString(5));
        session.setDescription(cursor.getString(6));
        return session;
    }
}

