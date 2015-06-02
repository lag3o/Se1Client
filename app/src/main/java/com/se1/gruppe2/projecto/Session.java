package com.se1.gruppe2.projecto;

/**
 * Created by myles on 30.05.15.
 */
public class Session {
    private Integer id;
    private String name;
    private String dateStart;
    private String dateEnd;
    private String location;
    private String description;

    public Session(){

    }
    public Session (String name,String dateStart, String dateEnd, String location,String description){
        this.name= name;
        this.dateEnd =dateEnd;
        this.dateStart=dateStart;
        this.location=location;
        this.description=description;
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

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
