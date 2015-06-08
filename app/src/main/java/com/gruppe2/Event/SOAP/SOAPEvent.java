package com.gruppe2.Event.SOAP;

import com.gruppe2.Event.Exceptions.ParamMissingException;
import com.gruppe2.Event.Objects.Event;

import javax.xml.namespace.QName;

/**
 * Created by myles on 06.06.15.
 */


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Date;
import java.util.Iterator;
import java.net.URL;

public class SOAPEvent {

    private final String NAMESPACE = "http://eventmanagement.management.de";
    private final String URL = "http://seprojekt-fhmuenster.rhcloud.com/10_EventManagementSystem/EventManagement/ManagementWebService?wsdl";
    private final String SOAP_ACTION = "http://eventmanagement.management.de/EventManagement";


    public Event getEvent(int ID) {
        String method_name = "";

        SoapObject Request = new SoapObject(NAMESPACE, method_name);

        /*
         * Create Category with Id to be passed as an argument
         *
         * */
        Event event = new Event();

        try {
            event.setID(ID);

        } catch (ParamMissingException ex) {
        }

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
            event.setStartDate((Date) response.getProperty((2)));
            event.setEndDate((Date) response.getProperty((3)));
            event.setDescription((String) response.getProperty(4).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
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
            event.setStartDate((Date) response.getProperty((2)));
            event.setEndDate((Date) response.getProperty((3)));
            event.setDescription((String) response.getProperty(4).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return event;
    }
}
