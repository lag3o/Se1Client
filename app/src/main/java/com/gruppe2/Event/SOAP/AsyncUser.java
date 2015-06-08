package com.gruppe2.Event.SOAP;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.gruppe2.Event.Exceptions.ParamMissingException;
import com.gruppe2.Event.Objects.Event;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Date;

/**
 * Created by myles on 06.06.15.
 */

