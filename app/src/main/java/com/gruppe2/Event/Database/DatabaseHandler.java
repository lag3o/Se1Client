package com.gruppe2.Event.Database;
import android.app.Application;
;
import android.content.Context;
/**
 * Created by myles on 01.06.15.
 */
public class DatabaseHandler extends Application  {
    public EventsDataSource datasource;
    public DatabaseHandler(){}

    public DatabaseHandler(Context context){
        datasource = new EventsDataSource(context);
        datasource.open();
    }
    @Override
    public void onCreate(){
        super.onCreate();
        datasource = new EventsDataSource(this);
        datasource.open();
    }
    public EventsDataSource getDatasource(){
        return datasource;
    }
}
