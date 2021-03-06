package com.gruppe2.Client.Database;


        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;
/**    Diese Klasse erzeugt eine SQLite Datenbank und stellt Namensflags zur Verfügung *
 *
 @author  Myles Sutholt
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_EVENTS = "events";
    public static final String EVENT_ID = "eventID";
    public static final String EVENT_NAME = "name";
    public static final String EVENT_DATE_START = "start_date";
    public static final String EVENT_DATE_END = "start_end";
    public static final String EVENT_DESCRIPTION ="description";

    public static final String TABLE_SESSIONS = "sessions";
    public static final String SESSION_ID = "session_id";
    public static final String SESSION_NAME = "name";
    public static final String SESSION_DATE_START = "start_date";
    public static final String SESSION_DATE_END = "end_date";
    public static final String SESSION_LOCATION = "location";
    public static final String SESSION_DESCRIPTION ="description";
    public static final String SESSION_PLZ= "plz";

    public static final String TABLE_MESSAGES = "messages";
    public static final String MESSAGE_ID = "id";
    public static final String MESSAGE_MSG = "message";
    public static final String MESSAGE_DATE = "date";

    private static final String DATABASE_NAME = "event.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_EVENT = "create table "
            + TABLE_EVENTS + "(" + EVENT_ID
            + " integer primary key, " + EVENT_NAME
            + " text not null," + EVENT_DATE_START
            + " text not null,"+ EVENT_DATE_END
            + " text not null,"+ EVENT_DESCRIPTION
            + " text not null);";
    private static final String DATABASE_SESSION = " create table "
            + TABLE_SESSIONS +"(" + SESSION_ID
            + " integer primary key autoincrement, " + EVENT_ID
            + " text not null," + SESSION_NAME
            + " text not null," + SESSION_DATE_START
            + " text not null,"+ SESSION_DATE_END
            + " text not null," + SESSION_LOCATION
            + " text not null," + SESSION_PLZ
            + " text,"+ SESSION_DESCRIPTION + " text);";
    private static final String DATABASE_MESSAGE = " create table "
            + TABLE_MESSAGES+"(" + MESSAGE_ID
            + " integer primary key autoincrement, " + EVENT_ID
            + " text not null," + MESSAGE_MSG
            + " text not null," + MESSAGE_DATE
            + " timestamp)";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Erstellt die Datenbank mit entsprechenden Tabellen
        database.execSQL(DATABASE_EVENT);
        database.execSQL(DATABASE_SESSION);
        database.execSQL(DATABASE_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_MESSAGE);
        onCreate(db);
    }

}