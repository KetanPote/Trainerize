package com.lanihuang.simplewebapp.entity;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Entity {

    private LinkedHashMap<String,String> attibutes;

    public HashMap<String, HashMap<String, String>> getCompositeAttibutes() 
    {
        return compositeAttibutes;
    }

    private HashMap<String,HashMap<String,String>> compositeAttibutes;

    public void addAttribute(String key,String value)
    {
        if (attibutes == null) 
        {
            attibutes = new LinkedHashMap<String, String>();
        }
        if (value != null) 
        {
            attibutes.put(key, value);
        }
    }
    public void addMapCompositeAttribute(String key,HashMap<String,String> attr)
    {
        if (compositeAttibutes == null)
        {
            compositeAttibutes = new HashMap<String,HashMap<String,String>>();
        }
        compositeAttibutes.put(key,attr);
    }

    public void addValueCompositeAttribute(String key1,String key2,String value) {
        if (compositeAttibutes == null) {
            compositeAttibutes = new HashMap<String,HashMap<String,String>>();
        }
        if(compositeAttibutes.get(key1) == null) {
            compositeAttibutes.put(key1,new HashMap<String, String>());
        }
        compositeAttibutes.get(key1).put(key2,value);
    }

    public HashMap<String, String> getAttibutes() {
        return attibutes;
    }
    public String getValue(String key) 
    {
        return attibutes.get(key);
    }

    public void setAttibutes(LinkedHashMap<String, String> attibutes) {
        this.attibutes = attibutes;
    }

}
