package com.se1.gruppe2.projecto;

/**
 * Created by myles on 20.05.15.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public Event createEvent(String name) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.EVENT_NAME, name);
        long insertId = database.insert(MySQLiteHelper.TABLE_EVENTS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumns, MySQLiteHelper.EVENT_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Event newEvent = cursorToEvent(cursor);
        cursor.close();
        return newEvent;
    }

    public void deleteEvent(Event event) {
        long id = event.getId();
        System.out.println("Name deleted with id: " + id);
        database.delete(MySQLiteHelper.EVENT_NAME, MySQLiteHelper.EVENT_ID
                + " = " + id, null);
    }

    public HashMap<Integer, String> getAllNames() {
        HashMap<Integer, String> events = new HashMap<Integer, String>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String eventName = cursorToEvent(cursor).getName();
            Integer id = cursorToEvent(cursor).getId();
            events.put(id, eventName);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return events;
    }

    private Event cursorToEvent(Cursor cursor) {
        Event event = new Event();
        event.setId(cursor.getInt(0));
        event.setName(cursor.getString(1));
        return event;
    }
}

