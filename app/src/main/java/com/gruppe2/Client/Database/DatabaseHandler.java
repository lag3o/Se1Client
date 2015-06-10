package com.gruppe2.Client.Database;
import android.app.Application;
;
import android.content.Context;
/**@author  Myles Sutholt
    Dies ist eine Application Class um die Datenbank während der gesamten Sitzung aktiv zu halten
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
