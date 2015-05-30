package com.se1.gruppe2.projecto;

/**
 * Created by myles on 20.05.15.
 */
        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_EVENTS = "events";
    public static final String EVENT_ID = "_id";
    public static final String EVENT_NAME = "name";

    public static final String TABLE_SESSIONS = "sessions";
    public static final String SESSION_ID = "_id";
    public static final String SESSION_NAME = "name";
    public static final String SESSION_DATE_START = "start_date";
    public static final String SESSION_DATE_END = "start_end";
    public static final String SESSION_LOCATION = "location";
    public static final String SESSION_DESCRIPTION ="description";

    private static final String DATABASE_NAME = "names.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_EVENTS + "(" + EVENT_ID
            + " integer primary key autoincrement, " + EVENT_NAME
            + " text not null); create table "+ TABLE_SESSIONS +"(" + SESSION_ID
            + " integer primary key autoincrement, " + SESSION_NAME
            + " text not null)," + SESSION_DATE_START
            + " text not null),"+ SESSION_DATE_END + SESSION_LOCATION
            + " text not null),"+ SESSION_DESCRIPTION + ";";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

}