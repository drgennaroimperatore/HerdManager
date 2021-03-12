package com.ilri.herdmanager.kmz;

import java.util.ArrayList;
import java.util.HashMap;

public class LocationData
{
    private static LocationData mInstance =null;

    private LocationData() {}
    private ArrayList<String> mRegions = new ArrayList<>();
    private HashMap<String, ArrayList<String>> mZonesForRegions = new HashMap<>();
    private HashMap<String, ArrayList<String>> mWordeasForZones = new HashMap<>();
    private HashMap<String, ArrayList<String>> mKebelesForWoredas = new HashMap<>();

    public static LocationData getInstance()
    {
        if (mInstance==null)
            mInstance=new LocationData();
        return mInstance;
    }

    public void setRegions(ArrayList<String> mRegions) {
        this.mRegions = mRegions;
    }

    public void setZonesForRegions(HashMap<String, ArrayList<String>> mZonesForRegions) {
        this.mZonesForRegions = mZonesForRegions;
    }

    public void setWordeasForZones(HashMap<String, ArrayList<String>> mWordeasForZones) {
        this.mWordeasForZones = mWordeasForZones;
    }

    public void setKebelesForWoredas(HashMap<String, ArrayList<String>> mKebelesForWoredas) {
        this.mKebelesForWoredas = mKebelesForWoredas;
    }

    public ArrayList<String> getRegions() {return mRegions;}
    public ArrayList<String> getZonesForRegion(String region){return mZonesForRegions.get(region);}
    public ArrayList<String> getWoredasForZone(String zone) {return mWordeasForZones.get(zone);}
    public ArrayList<String> getKebelesForWoreda(String woreda) {return mKebelesForWoredas.get(woreda);}


}
