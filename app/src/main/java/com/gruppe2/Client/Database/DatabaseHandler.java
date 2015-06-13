package com.gruppe2.Client.Database;
import android.app.Application;
;
import android.content.Context;

import com.gruppe2.Client.Helper.Push;
import com.gruppe2.Client.Objects.Event;

/**@author  Myles Sutholt
    Dies ist eine Application Class um die Datenbank während der gesamten Sitzung aktiv zu halten
 */
public class DatabaseHandler extends Application  {
    private EventsDataSource datasource;
    private Push push;
    private Event event;

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
        push = new Push();
        event = null;
    }
    public void pushRegister(String alias, Context context){
        push.registerDeviceOnPushServer(alias, context);
    }
    public Push getPush(){return push;}
    public EventsDataSource getDatasource(){
        return datasource;
    }

    public Event getEvent() {
        return event;
    }

    public void resetEvent(){
        event = null;
    }
}
