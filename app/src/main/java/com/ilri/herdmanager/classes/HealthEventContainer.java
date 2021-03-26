package com.ilri.herdmanager.classes;

import com.ilri.herdmanager.database.entities.BodyConditionForHealthEvent;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.HealthInterventionForHealthEvent;
import com.ilri.herdmanager.database.entities.SignsForDiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HealthEventContainer
{
  public  List<DiseasesForHealthEvent> mDhes = new ArrayList<>();
   public  List<SignsForHealthEvent> mShes = new ArrayList<>();
   public List<BodyConditionForHealthEvent> mBChes = new ArrayList<>();
   public List<HealthInterventionForHealthEvent> mHIhes = new ArrayList<>();
   public ArrayList<ArrayList<SignsForDiseasesForHealthEvent>> mSFDFGE = new ArrayList<>();
    public HealthEventContainer() {}
}
