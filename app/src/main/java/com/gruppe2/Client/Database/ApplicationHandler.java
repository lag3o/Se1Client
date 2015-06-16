package com.gruppe2.Client.Database;
import android.app.Application;
;
import android.content.Context;

import com.gruppe2.Client.Exceptions.ParamMissingException;
import com.gruppe2.Client.Helper.Push;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Objects.Session;

/**@author  Myles Sutholt
    @description Dies ist eine Application Class, die bestimmte Mechanismen während der gesamten Sitzung aktiv hält
 */
public class ApplicationHandler extends Application  {
    private EventsDataSource datasource;
    private Push push;
    private Event event;

    public ApplicationHandler(){}

    public ApplicationHandler(Context context){
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
    public void updateEvent(Event event) {
        this.event = event;

        datasource.deleteEvent(event);
        datasource.createEvent(event);
    }
    public void setEvent(Event event){
        this.event = event;
    }
    public void setEventID(int id) {
        try {
            event.setEventID(id);
        } catch (ParamMissingException e) {
        }
    }
}
