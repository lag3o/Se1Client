package com.gruppe2.Client.Helper;

import android.util.Log;

import com.gruppe2.Client.Database.ApplicationHandler;
import com.gruppe2.Client.Objects.Event;
import com.gruppe2.Client.Objects.Session;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

import static com.gruppe2.Client.Helper.Constants.NAMESPACE;
import static com.gruppe2.Client.Helper.Constants.SOAP_ACTION;
import static com.gruppe2.Client.Helper.Constants.URL;

/**
 * Diese Klasse beinhaltet die tatsächlichen Serverconnections und Abfragen sowie die Speicherung/Weitergabe und Verarbeitung der
 * empfangenen Daten.
 *
 *
 @author  Myles Sutholt
 */
public class DataHelper {
    private Event event;
    private ArrayList<Event> events;
    private ApplicationHandler handler;

    public void getDatabase(ApplicationHandler handler) {

        //Datenbankabfrage zu bestimmter Veranstaltung
        int id = handler.getEvent().getEventID();
        //Daten abfragen für favorisierte Sessions
        event = handler.getDatasource().getEvent(id);
        Log.d("Sessions", "Anzahl: " + event.getSessions().size());
    }

    //Löscht eine Veranstaltung vom Server
    public void deleteEvent(ApplicationHandler handler) {
        this.handler = handler;
        String method_name = "deleteEvent";

        SoapObject request = new SoapObject(NAMESPACE, method_name);

        PropertyInfo pi = new PropertyInfo();

        request.addProperty("eventID", handler.getEvent().getEventID());

        /*
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        /*
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try {
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope, null);
            Log.d("dump Request: ", androidHttpTransport.requestDump);
            Log.d("dump response: ", androidHttpTransport.responseDump);
        } catch (Exception e) {

        }
    }

    //Fragt eine Veranstaltung vom Server ab
    public void getEvent(ApplicationHandler handler, int id) {
        this.handler = handler;


        SoapObject request = new SoapObject(NAMESPACE, "getEvent");

        event = new Event();
        request.addProperty("eventID", id);
        /*
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);

        SoapSerializationEnvelope envelope2 = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        /*
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try {
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope, null);
            Log.d("dump Request Event: ", androidHttpTransport.requestDump);
            Log.d("dump response Event: ", androidHttpTransport.responseDump);


            SoapObject response = (SoapObject) envelope.getResponse();
//            java.util.Vector<SoapObject> responseSession = (java.util.Vector<SoapObject>) response.getProperty("sessions");

            int count = 0;
            /** lists property count */
            if (response.hasProperty("id")) {
                event.setEventID(Integer.parseInt(response.getPropertyAsString("id")));
                count++;
            }
            if (response.hasProperty("name")) {
                event.setName(response.getPropertyAsString("name"));
                count++;
            }

            if (response.hasProperty("dateEnd")) {
                event.setDateEnd((new Parser().StringToTempDate(response.getPropertyAsString("dateEnd"))));
                count++;
            }

            if (response.hasProperty("description")) {
                event.setDescription(response.getPropertyAsString("description"));
                count++;
            }

            if (response.hasProperty("dateStart")) {
                event.setDateStart((new Parser().StringToTempDate(response.getPropertyAsString("dateStart"))));
                count++;
            }
            if (response.hasProperty("sessions")){
                for (int i = count; i< response.getPropertyCount(); i++){
                    SoapObject cs = (SoapObject) response.getProperty(i);
                    /** temp PhongTro object */
                    Session tempObj = new Session();

                    if (cs.hasProperty("name")) {
                        tempObj.setName(cs.getPropertyAsString("name"));
                    }

                    if (cs.hasProperty("dateEnd")) {
                        tempObj.setDateEnd((new Parser().StringToTempDate(cs.getPropertyAsString("dateEnd"))));
                    }

                    if (cs.hasProperty("description")) {
                        tempObj.setDescription(cs.getPropertyAsString("description"));
                    }

                    if (cs.hasProperty("dateStart")) {
                        tempObj.setDateStart((new Parser().StringToTempDate(cs.getPropertyAsString("dateStart"))));
                    }
                    if (cs.hasProperty("location")) {
                        tempObj.setLocation((cs.getPropertyAsString("location")));
                    }
                    if (cs.hasProperty("plz")) {
                        tempObj.setPlz((cs.getPropertyAsString("plz")));
                    }
                    Log.d("getSessions", cs.getProperty(5).toString() + "-" + cs.getPropertyAsString("name"));
                    /** Adding temp PhongTro object to list */
                    event.getSessions().add(tempObj);
                    Log.d("getSessions", event.getSessions().get(i-count).getName() + "-" + event.getSessions().get(i-count).getDateStart());

                    /** Adding temp PhongTro object to list */
                }
            }
            //getSessions(envelope2);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Async GetEvent", handler.getEvent().getEventID() + " " + request.toString());
            /** if an error handled events setting null */
            event = new Event();
            getDatabase(handler);

        } finally {
            handler.updateEvent(event);
        }

    }

    //Fragt vorhandene Termine zur Veranstaltung vom Server ab
    public void getSessions(SoapSerializationEnvelope envelope) {
        /** contains all events objects */
        ArrayList<Session> sessions = new ArrayList<Session>();
        try {
            //java.util.Vector<SoapObject> response = (java.util.Vector<SoapObject>)
            SoapObject response = (SoapObject) envelope.getResponse();

            /** lists property count */

            /** loop */
            if (response != null) {
                //for (SoapObject cs : response) {
                for (int i = 0; i< response.getPropertyCount(); i++){
                    /** temp PhongTro object */
                    SoapObject cs = (SoapObject) response.getProperty(i);
                    /** temp PhongTro object */
                    Session tempObj = new Session();

                    if (cs.hasProperty("name")) {
                        tempObj.setName(cs.getPropertyAsString("name"));
                    }

                    if (cs.hasProperty("dateEnd")) {
                        tempObj.setDateEnd((new Parser().StringToTempDate(cs.getPropertyAsString("dateEnd"))));
                    }

                    if (cs.hasProperty("description")) {
                        tempObj.setDescription(cs.getPropertyAsString("description"));
                    }

                    if (cs.hasProperty("dateStart")) {
                        tempObj.setDateStart((new Parser().StringToTempDate(cs.getPropertyAsString("dateStart"))));
                    }
                    if (cs.hasProperty("location")) {
                        tempObj.setLocation((cs.getPropertyAsString("location")));
                    }
                    if (cs.hasProperty("plz")) {
                        tempObj.setPlz((cs.getPropertyAsString("plz")));
                    }
                    Log.d("getSessions", cs.getProperty(5).toString() + "-" + cs.getPropertyAsString("name"));

                    /** Adding temp PhongTro object to list */
                    event.getSessions().add(tempObj);
                    Log.d("getSessions", event.getSessions().get(i).getName() + "-" + event.getSessions().get(i).getDateStart());
                }
            }
        } catch (Exception e) {
            event.setSessions(new ArrayList<Session>());
        }
        finally {
           event.setSessions(sessions);
        }
    }

    private SoapSerializationEnvelope getEnvelope(String method_name) {

        SoapObject request = new SoapObject(NAMESPACE, method_name);

        PropertyInfo pi = new PropertyInfo();

        request.addProperty("eventID", handler.getEvent().getEventID());
        /*
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        /*
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try {
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope, null);
            Log.d("dump Request: ", androidHttpTransport.requestDump);
            Log.d("dump response: ", androidHttpTransport.responseDump);
        } catch (Exception e) {
        }
        return envelope;
    }

    //Fragt alle Veranstaltungen vom Server ab
    public void getEvents(ApplicationHandler handler) {

        String method_name = "getEvents";

        SoapObject request = new SoapObject(NAMESPACE, method_name);
        /*
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        /*
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try {
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope);
            Log.d("dump Request: ", androidHttpTransport.requestDump);
            Log.d("dump response: ", androidHttpTransport.responseDump);

            /** contains all events objects */
            //java.util.Vector<SoapObject> response = (java.util.Vector<SoapObject>) envelope.getResponse();
            SoapObject response = (SoapObject) envelope.getResponse();

            handler.setEvents(new ArrayList<Event>());
            events = handler.getEvents();
            /** lists property count */

            /** loop */
            if (response != null) {
                //for (SoapObject cs : response) {
                for (int i = 0; i< response.getPropertyCount(); i++){
                    /** temp PhongTro object */
                    SoapObject cs = (SoapObject) response.getProperty(i);
                    Event tempObj = new Event();

                    if (cs.hasProperty("id")) {
                        tempObj.setEventID(Integer.parseInt(cs.getPropertyAsString("id")));
                    }
                    if (cs.hasProperty("name")) {
                        tempObj.setName(cs.getPropertyAsString("name"));
                    }

                    if (cs.hasProperty("dateEnd")) {
                        tempObj.setDateEnd((new Parser().StringToTempDate(cs.getPropertyAsString("dateEnd"))));
                    }

                    if (cs.hasProperty("description")) {
                        tempObj.setDescription(cs.getPropertyAsString("description"));
                    }

                    if (cs.hasProperty("dateStart")) {
                        tempObj.setDateStart((new Parser().StringToTempDate(cs.getPropertyAsString("dateStart"))));
                    }
                    Log.d("LOG", cs.getProperty(0).toString());

                    /** Adding temp PhongTro object to list */
                    events.add(tempObj);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            /** if an error handled events setting null */
            events = new ArrayList<Event>();
        }
    }

    //Legt eine Veranstaltung auf dem Server an
    private void createEvent(ApplicationHandler handler) {

        String method_name = "addUser";
        event = handler.getEvent();

        SoapObject request = new SoapObject(NAMESPACE, method_name);

        PropertyInfo pi = new PropertyInfo();
        pi.setName("Event");
        pi.setType(Event.class);
        pi.setValue(event);
        request.addProperty(pi);

        /*
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        /*
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try {
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope, null);
            Log.d("dump Request: ", androidHttpTransport.requestDump);
            Log.d("dump response: ", androidHttpTransport.responseDump);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            event.setEventID(Integer.parseInt(response.getValue().toString()));


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //Sendet eine Pushnachricht
    public void sendMessage(String message, ApplicationHandler handler) {
        String method_name = "sendPushMessage";

        SoapObject request = new SoapObject(NAMESPACE, method_name);

        PropertyInfo pi = new PropertyInfo();
        Log.d("Message", message + " ");
        //request.addProperty("userID", handler.getUserID());
        request.addProperty("msg", message);
        request.addProperty("eventID", handler.getEvent().getEventID());

        /*
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        /*
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try {
            androidHttpTransport.debug = true;
            androidHttpTransport.call(SOAP_ACTION, envelope, null);
            Log.d("dump Request: ", androidHttpTransport.requestDump);
            Log.d("dump response: ", androidHttpTransport.responseDump);
        } catch (Exception e) {

        }
    }
}

