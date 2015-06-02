package com.se1.gruppe2.projecto;

import java.util.ArrayList;

/**
 * Created by myles on 20.05.15.
 */
public class Event {
    private Integer id;
    private String name;
    private String endDate;
    private String startDate;
    private String description;
    private ArrayList<Session> sessions;

    public Event(String name, String endDate, String startDate, String description){
        this.name = name;
        this.endDate = endDate;
        this.startDate = startDate;
        this.description = description;
        this.sessions = new ArrayList<Session>();
    }
    public Event(){

    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }
    public void addSessions(Session session) {
        this.sessions.add(session);
    }
}
