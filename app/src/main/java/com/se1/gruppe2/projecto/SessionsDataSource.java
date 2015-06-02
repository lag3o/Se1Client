package com.se1.gruppe2.projecto;

/**
 * Created by myles on 20.05.15.
 */
import java.util.HashMap;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
public class SessionsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.EVENT_ID,
            MySQLiteHelper.EVENT_NAME };

    public SessionsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Session createSession(String name) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.TABLE_SESSIONS, name);
        long insertId = database.insert(MySQLiteHelper.TABLE_SESSIONS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_SESSIONS,
                allColumns, MySQLiteHelper.SESSION_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Session newSession = cursorToSession(cursor);
        cursor.close();
        return newSession;
    }

    public void deleteSession(Session session) {
        long id = session.getId();
        System.out.println("Session deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_SESSIONS, MySQLiteHelper.SESSION_ID
                + " = " + id, null);
    }

    public HashMap<Integer, String> getAllNames() {
        HashMap<Integer, String> sessions = new HashMap<Integer, String>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SESSIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String sessionName = cursorToSession(cursor).getName();
            Integer id = cursorToSession(cursor).getId();
            sessions.put(id, sessionName);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return sessions;
    }


    private Session cursorToSession(Cursor cursor){
        Session session = new Session();
        session.setId(cursor.getInt(0));
        session.setName(cursor.getString(1));
        session.setDateStart(cursor.getString(2));
        session.setDateEnd(cursor.getString(3));
        session.setLocation(cursor.getString(4));
        session.setDescription(cursor.getString(5));
        return session;
    }
}

