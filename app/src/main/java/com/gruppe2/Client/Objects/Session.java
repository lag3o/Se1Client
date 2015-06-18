package com.gruppe2.Client.Objects;

import com.gruppe2.Client.Exceptions.ParamMissingException;
import com.gruppe2.Client.Interfaces.InterSession;

import java.util.Date;

/**
Ein Terminobjekt laut Interfacespezifikation
 *
 *@author  Myles Sutholt
 */
public class Session implements InterSession {
    private Integer id;
    private String name;
    private Date dateStart;
    private Date dateEnd;
    private String location;
    private String plz;
    private String description;

    public Session() {

    }
    public Session  (String name, Date dateStart, Date dateEnd, String location, String plz ,String description) throws ParamMissingException {
        if (controlParameter(name)) throw new ParamMissingException("Name des Termins");
        if (controlParameter(location+plz)) throw new ParamMissingException("Adresse");
        if (location.equals(", ")) throw new ParamMissingException("Adresse");
        this.name = name;
        this.location = location;
        this.description=description;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.plz = plz;

    }
    @Override
    public Integer getSessionID() {
        return id;
    }

    @Override
    public void setSessionID(Integer id) throws ParamMissingException {
        if (controlParameter(((Integer) id).toString())) throw new ParamMissingException("401");
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name)throws ParamMissingException {
        if (controlParameter(name)) throw new ParamMissingException("402");
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }

    @Override
    public Date getDateStart() {

        return dateStart;
    }

    @Override
    public void setDateStart(Date dateStart)throws ParamMissingException {

        if (dateStart == null ){
            throw new ParamMissingException("403");
        }
        else {
            this.dateStart = dateStart;
        }
    }

    @Override
    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd)throws ParamMissingException {

        if (dateEnd == null ){
            throw new ParamMissingException("404");
        }
        else {
            this.dateEnd = dateEnd;
        }
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) throws ParamMissingException {
        if (controlParameter(location)) throw new ParamMissingException("405");
        this.location = location;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }


    public boolean controlParameter (String param){
        //String name,String dateStart, String dateEnd, String location,String description
            if (param.toString().equalsIgnoreCase("")) return true;
            return false;
    }

    @Override
    public String getPlz() {
        return plz;
    }

    @Override
    public void setPlz(String plz) {
        this.plz = plz;
    }
}
