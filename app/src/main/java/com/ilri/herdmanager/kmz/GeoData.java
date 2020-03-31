package com.ilri.herdmanager.kmz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class GeoData
{
    private static GeoData mInstance = null;

    public static GeoData getInstance() {

        if(mInstance ==null)
            mInstance = new GeoData();
        return mInstance;
    }

    LinkedList<String> mRegions;
    LinkedHashMap<String, ArrayList<String>> mDistrictsinRegions;
    LinkedHashMap<String, ArrayList<String>> mWoredasinDistricts;

    boolean mIsInitialised = false;

    public void populate(KMZParser.Document document)
    {
        mRegions = new LinkedList<>();
        mDistrictsinRegions = new LinkedHashMap<>();
        mWoredasinDistricts = new LinkedHashMap<>();

        List<KMZParser.Folder> foldersInDocument = document.getmFolders();

        for (KMZParser.Folder f : foldersInDocument)
        {
            List<KMZParser.Placemark> placemarksInDocument = f.getmPlaceMarks();

            for(KMZParser.Placemark p: placemarksInDocument)
            {
                KMZParser.ExtendedData extendedData = p.getExtendedData();
                List<KMZParser.SimpleData> simpleData = extendedData.getSchemaData();


                String region = simpleData.get(1).getText();
                String district = simpleData.get(2).getText();
                String woreda = simpleData.get(3).getText();

                if(!mRegions.contains(region))
                    mRegions.add(region);

                if(mDistrictsinRegions.containsKey(region))
                {
                    if(!mDistrictsinRegions.get(region).contains(district))
                        mDistrictsinRegions.get(region).add(district);
                }
                else
                {
                    ArrayList<String> districsforcurrentRegion = new ArrayList<>();

                    mDistrictsinRegions.put(region, districsforcurrentRegion);
                }

                if(mWoredasinDistricts.containsKey(district))
                {
                    mWoredasinDistricts.get(district).add(woreda);
                }
                else
                {
                    ArrayList<String> woredasForCurrentDistrict = new ArrayList<>();
                    woredasForCurrentDistrict.add(woreda); // we need to add it now otherwise it will be skipped
                    mWoredasinDistricts.put(district, woredasForCurrentDistrict);
                }

            }

        }

        mIsInitialised = true;
    }

    public boolean isInitialised () {return mIsInitialised;}

    public LinkedList<String> getRegions () {return mRegions;}
    public ArrayList<String> getDistricsForRegion(String region)
    {
        return mDistrictsinRegions.get(region);
    }

    public  ArrayList<String> getWoredasForDistrics(String district)
    {
        return mWoredasinDistricts.get(district);
    }

}
