package com.gruppe2.Client.Database;
import android.app.Application;
;
import android.content.Context;

import com.gruppe2.Client.Activity.MainActivity;
import com.gruppe2.Client.Exceptions.ParamMissingException;
import com.gruppe2.Client.Helper.NotifyingHandler;
import com.gruppe2.Client.Helper.Push;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Objects.Session;

import org.jboss.aerogear.android.unifiedpush.RegistrarManager;

import java.util.ArrayList;

/**
Dies ist eine Application Class, die bestimmte Mechanismen während der gesamten Sitzung aktiv hält

 @author  Myles Sutholt
 */
public class ApplicationHandler extends Application  {
    private EventsDataSource datasource;
    private Push push;
    private Event event;
    private ArrayList<Event> events;
    private final NotifyingHandler NHANDLER = new NotifyingHandler();
    private int userID;

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
        events = new ArrayList<Event>();
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

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    public boolean isAdmin(){
        if(this.userID == event.getUserID()){
            return true;
        }
        return false;
    }
}
