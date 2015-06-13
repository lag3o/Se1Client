package com.gruppe2.Client.Objects;

import com.gruppe2.Client.Exceptions.ParamMissingException;
import com.gruppe2.Client.Interfaces.InterEvent;

import java.util.ArrayList;
import java.util.Date;

/**@author Myles Sutholt
Ein Veranstaltungsobjekt
 */
public class Event implements InterEvent {
    private Integer id;
    private String name;
    private Date dateEnd;
    private Date dateStart;
    private String description;
    private ArrayList<Session> sessions;
    private int userID;

    public Event(String name, Date startDate, Date endDate, String description)throws ParamMissingException {

        if (controlParameter(name)) throw new ParamMissingException("Veranstaltungsname");
        this.name = name;
        this.dateEnd = endDate;
        this.dateStart = startDate;
        this.description = description;
        this.sessions = new ArrayList<Session>();
    }
    public Event(){
        this.sessions = new ArrayList<Session>();
    }
    @Override
    public Integer getEventID() {
        return id;
    }

    @Override
    public void setEventID(Integer id) throws ParamMissingException {
        if (controlParameter(((Integer) id).toString())) throw new ParamMissingException("424");
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) throws ParamMissingException {
        if (controlParameter(name)) throw new ParamMissingException("425");
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }

    @Override
    public Date getDateEnd() {
        return dateEnd;
    }

    @Override
    public void setDateEnd(Date endDate) throws ParamMissingException {

        if (endDate == null ){
            throw new ParamMissingException("427");
        }
        else
        {
            this.dateEnd = endDate;
        }
    }

    @Override
    public Date getDateStart() {
        return dateStart;
    }

    @Override
    public void setDateStart(Date startDate) throws ParamMissingException {

        if (startDate == null ){
            throw new ParamMissingException("426");
        }
        else {
            this.dateStart = startDate;
        }
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public ArrayList<Session> getSessions() {
        return sessions;
    }

    @Override
    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }
    @Override
    public void addSessions(Session session) {
        this.sessions.add(session);
    }
    public boolean controlParameter (String param){
        //String name,String location,String description
        if (param.toString().equalsIgnoreCase("")) return true;
        return false;
    }

}
