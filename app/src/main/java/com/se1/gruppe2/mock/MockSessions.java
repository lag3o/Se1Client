package com.se1.gruppe2.mock;

import java.util.ArrayList;
import java.util.HashMap;

import static com.se1.gruppe2.projecto.Constants.FIRST_COLUMN;
import static com.se1.gruppe2.projecto.Constants.SECOND_COLUMN;
import static com.se1.gruppe2.projecto.Constants.THIRD_COLUMN;
/**
 * Created by myles on 17.05.15.
 */
public class MockSessions {
    private ArrayList<HashMap<String,String>> al;
    private HashMap<String,String> hm;
    private int id;
    public MockSessions(Integer param){

        id = param;
        String parameter = param.toString();
        hm = new HashMap<String,String>();
        al = new ArrayList<HashMap<String,String>>();

        al.add(new HashMap<String, String>());
        al.get(0).put(FIRST_COLUMN, "10:00");
        al.get(0).put(SECOND_COLUMN, "How to fight hangover - Malte");
        al.get(0). put(THIRD_COLUMN, parameter);

        al.add(new HashMap<String, String>());
        al.get(1).put(FIRST_COLUMN, "12:00");
        al.get(1).put(SECOND_COLUMN, "Heia machen für Fortgeschrittene - Myles");
        al.get(1). put(THIRD_COLUMN, parameter);

        al.add(new HashMap<String, String>());
        al.get(2).put(FIRST_COLUMN, "16:00");
        al.get(2).put(SECOND_COLUMN, "Bier nach 4 - Jonathan");
        al.get(2). put(THIRD_COLUMN, parameter);


    }

    public ArrayList<HashMap<String,String>> getAl(){
        return al;
    }
}
