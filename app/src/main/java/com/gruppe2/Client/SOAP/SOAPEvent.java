package com.gruppe2.Client.SOAP;

import android.util.Log;

import com.gruppe2.Client.Objects.Event;

/**
 * Created by myles on 06.06.15.
 */


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Date;

import static com.gruppe2.Client.Helper.Constants.NAMESPACE;
import static com.gruppe2.Client.Helper.Constants.SOAP_ACTION;
import static com.gruppe2.Client.Helper.Constants.URL;

public class SOAPEvent {

    public void getEvent(int id) {

        String method_name = "getEvent";

        SoapObject request = new SoapObject(NAMESPACE, method_name);

        PropertyInfo pi = new PropertyInfo();

        request.addProperty("eventID", id);

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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }








    public Event sendEvent(Event event){
        String method_name = "";

        SoapObject Request = new SoapObject(NAMESPACE, method_name);


        /*
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi = new PropertyInfo();
        pi.setName("Event");
        pi.setValue(event);
        pi.setType(event.getClass());
        Request.addProperty(pi);

        /*
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);
        envelope.addMapping(NAMESPACE, "Event", new Event().getClass());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        /*
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.getResponse();
            event.setID(Integer.parseInt(response.getProperty(0).toString()));
            event.setName(response.getProperty(1).toString());
            event.setDateStart((Date) response.getProperty((2)));
            event.setDateEnd((Date) response.getProperty((3)));
            event.setDescription((String) response.getProperty(4).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }
}
