package com.ilri.herdmanager.managers;

import android.content.Context;

import com.ilri.herdmanager.database.entities.AnimalMovementsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DeathsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.DynamicEvent;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.ProductivityEvent;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;

import java.util.Date;
import java.util.List;

public class HerdVisitManager {
    private static final HerdVisitManager ourInstance = new HerdVisitManager();

   public static HerdVisitManager getInstance() {
        return ourInstance;
    }

    private HealthEvent mAssociatedHealthEvent;
    private DynamicEvent mAssociatedDynamicEvent;
    private ProductivityEvent mAssociatedProductivityEvent;



    private HerdVisitManager() {
    }

    public boolean checkEventsAssociatedWithVisits()
    {
        return false;
    }

    public int getNumberOfVisitsAssociatedWithHerd(Context context,  int herdID)
    {
        return HerdDatabase.getInstance(context).getHerdDao().getAllHerdVisitsByHerdId(herdID).size();

    }

    private long createHealthEventForVisit(Context context, int herdVisitID,
                                           List<DiseasesForHealthEvent> diseasesForHealthEvent, List<SignsForHealthEvent> signsForHealthEvents)
    {

        HealthEvent healthEvent = new HealthEvent();
        healthEvent.herdVisitID = herdVisitID;

       long healthEventID = HerdDatabase.getInstance(context).getHerdDao().InsertHealthEvent(healthEvent);

       for(DiseasesForHealthEvent dhe: diseasesForHealthEvent )
       {
            dhe.healthEventID = (int)healthEventID;
           HerdDatabase.getInstance(context).getHerdDao().InsertDiseaseForHealthEvent(dhe);
       }
       for(SignsForHealthEvent she: signsForHealthEvents)
       {
           she.healthEventID = (int)healthEventID;
           HerdDatabase.getInstance(context).getHerdDao().InsertSignForHealthEvent(she);

       }


       return healthEventID;

    }

    public long createDynamicEventForVisit(Context context, int herdVisitID, AnimalMovementsForDynamicEvent movements, List<DeathsForDynamicEvent> deathsForDynamicEvent)
    {
        DynamicEvent dynamicEvent = new DynamicEvent();
        dynamicEvent.herdVisitID= herdVisitID;

        long dynamicEventID = HerdDatabase.getInstance(context).getHerdDao().InsertDynamicEvent(dynamicEvent);

        movements.dynamicEventID = (int) dynamicEventID;

        HerdDatabase.getInstance(context).getHerdDao().InsertAnimalMovementForDynamicEvent(movements);

        for (DeathsForDynamicEvent dde: deathsForDynamicEvent)
        {
            dde.dynamicEventID =(int)dynamicEventID;
            HerdDatabase.getInstance(context).getHerdDao().InsertDeathForDynamicEvent(dde);

        }

        return dynamicEventID;
    }

    public void addVisitToHerd(Context context,
                               int herdID,
                               Date whenitoccured,
                               List<DiseasesForHealthEvent> diseasesForHealthEvent,
                               List<SignsForHealthEvent> signsForHealthEvents,AnimalMovementsForDynamicEvent movements,
                               List<DeathsForDynamicEvent> deathsForDynamicEvent)
    {
        HerdVisit herdVisit = new HerdVisit();
        herdVisit.HerdID = herdID;
        herdVisit.HerdVisitDate = whenitoccured;
        herdVisit.herdSizeAtVisit =0;

       long herdVisitID = HerdDatabase.getInstance(context).getHerdDao().InsertHerdVisit(herdVisit);

        //create a HealthEvent for this visit
        createHealthEventForVisit(context, (int)herdVisitID, diseasesForHealthEvent,signsForHealthEvents);

        //create Productivity event for this visit

        //create a dynamic event for this visit
        createDynamicEventForVisit(context, (int)herdVisitID,movements,deathsForDynamicEvent);


    }



    public List<HerdVisit> getVisitsForHerd(Context context, int herdID)
    {
        return HerdDatabase.getInstance(context).getHerdDao().getAllHerdVisitsByHerdId(herdID);
    }
}
