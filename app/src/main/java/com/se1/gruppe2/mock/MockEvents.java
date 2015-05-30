package com.se1.gruppe2.mock;

import java.util.ArrayList;

/**
 * Created by myles on 16.05.15.
 */
public class MockEvents {
    private ArrayList<String> al;

    public MockEvents (){
        al = new ArrayList<String>();
        al.add("O-Woche WS15");
        al.add("Bufak WS15");
        al.add("Erstifahrt WS15");
        al.add("Weltherrschaft übernehmen");

    }
    public ArrayList<String> getAl(){
        return al;
    }



}
