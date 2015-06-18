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
public class DataHelper  {
    private Event event;
    private ArrayList<Event> events;
    public void getDatabase(ApplicationHandler handler){

        int id = handler.getEvent().getEventID();
        //Daten abfragen für favorisierte Sessions
        event = handler.getDatasource().getEvent(id);
        Log.d("Sessions", "Anzahl: " + event.getSessions().size());
    }

    public void deleteEvent(ApplicationHandler handler) {
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

    public void getEvent(ApplicationHandler handler) {

        String method_name = "getEvent";

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
            SoapObject response = (SoapObject) envelope.getResponse();
            java.util.Vector<SoapObject> responseSession = (java.util.Vector<SoapObject>) envelope.getResponse();

            /** lists property count */
            if (response.hasProperty("id")) {
                event.setEventID(Integer.parseInt(response.getPropertyAsString("id")));
            }
            if (response.hasProperty("name")) {
                event.setName(response.getPropertyAsString("name"));
            }

            if (response.hasProperty("dateEnd")) {
                event.setDateEnd((new Parser().StringToTempDate(response.getPropertyAsString("dateEnd"))));
            }

            if (response.hasProperty("description")) {
                event.setDescription(response.getPropertyAsString("description"));
            }

            if (response.hasProperty("dateStart")) {
                event.setDateStart((new Parser().StringToTempDate(response.getPropertyAsString("dateStart"))));
            }
            /** loop */
            if (response != null) {
                for (SoapObject cs : responseSession) {
                    /** temp PhongTro object */
                    Session tempObj = new Session();

                    if (cs.hasProperty("id")) {
                        tempObj.setSessionID(Integer.parseInt(cs.getPropertyAsString("id")));
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
                    if (cs.hasProperty("location")) {
                        tempObj.setLocation((cs.getPropertyAsString("location")));
                    }
                    if (cs.hasProperty("plz")) {
                        tempObj.setLocation((cs.getPropertyAsString("plz")));
                    }
                    Log.d("LOG",cs.getProperty(0).toString());

                    /** Adding temp PhongTro object to list */
                    event.getSessions().add(tempObj);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            /** if an error handled events setting null */
            event = new Event();
            getDatabase(handler);

        }
        finally{
            handler.setEvent(event);
        }

    }
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
            java.util.Vector<SoapObject> response = (java.util.Vector<SoapObject>) envelope.getResponse();

            handler.setEvents(new ArrayList<Event>());
            events = handler.getEvents();
            /** lists property count */

            /** loop */
            if (response != null) {
                for (SoapObject cs : response) {
                    /** temp PhongTro object */
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
                    Log.d("LOG",cs.getProperty(0).toString());

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
    private void createEvent(ApplicationHandler handler){

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
            Log.d("dump Request: " ,androidHttpTransport.requestDump);
            Log.d("dump response: " ,androidHttpTransport.responseDump);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            event.setEventID(Integer.parseInt(response.getValue().toString()));


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
