package com.ilri.herdmanager.managers;

import android.content.ContentValues;
import android.content.Context;

import com.ilri.herdmanager.database.entities.AnimalMovementsForDynamicEvent;
import com.ilri.herdmanager.database.entities.BirthsForProductivityEvent;
import com.ilri.herdmanager.database.entities.BodyConditionForHealthEvent;
import com.ilri.herdmanager.database.entities.DeathsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DiseasesForHealthEvent;
import com.ilri.herdmanager.database.entities.DynamicEvent;
import com.ilri.herdmanager.database.entities.Farmer;
import com.ilri.herdmanager.database.entities.HealthEvent;
import com.ilri.herdmanager.database.entities.HealthInterventionForHealthEvent;
import com.ilri.herdmanager.database.entities.Herd;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;
import com.ilri.herdmanager.database.entities.ProductivityEvent;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;
import com.ilri.herdmanager.database.entities.SyncStatus;

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
        return HerdDatabase.getInstance(context).getHerdDao().getAllHerdVisitsByHerdID(herdID).size();

    }

    private long createHealthEventForVisit(Context context, int herdVisitID,
                                           List<DiseasesForHealthEvent> diseasesForHealthEvent,
                                           List<SignsForHealthEvent> signsForHealthEvents,
                                           List<BodyConditionForHealthEvent> bodyConditionForHealthEvents,
                                           List<HealthInterventionForHealthEvent> healthInterventionForHealthEvents)
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

       for(BodyConditionForHealthEvent bche: bodyConditionForHealthEvents)
       {
           bche.healthEventID = (int)healthEventID;
           HerdDatabase.getInstance(context).getHerdDao().InsertBodyConditionForHealthEvent(bche);

       }

       for (HealthInterventionForHealthEvent hihe: healthInterventionForHealthEvents)
       {
           hihe.healthEventID = (int)healthEventID;
           HerdDatabase.getInstance(context).getHerdDao().InsertHealthInterventionForHealthEvent(hihe);
       }


       return healthEventID;

    }


    public void editSignForHealthEvent(Context context, SignsForHealthEvent signsForHealthEvent)
    {
        HerdDatabase.getInstance(context).getHerdDao().UpdateSignForHealthEvent(signsForHealthEvent);
    }

    public void addSignToExistingVisit(Context context, SignsForHealthEvent newSign, int herdVisitID)
    {
       HealthEvent healthEvent = HerdDatabase.getInstance(context).getHerdDao().getHealthEventForVisit(herdVisitID).get(0);
       newSign.healthEventID = healthEvent.ID;
       HerdDatabase.getInstance(context).getHerdDao().InsertSignForHealthEvent(newSign);
    }



    private long createProductivityEventForVisit (Context context, int herdVisitID, MilkProductionForProductivityEvent milkProductionForProductivityEvent, BirthsForProductivityEvent birthsForProductivityEvent)
    {
        ProductivityEvent productivityEvent = new ProductivityEvent();
        productivityEvent.HerdVisitID = herdVisitID;

        long productivityEventID = HerdDatabase.getInstance(context).getHerdDao().InsertProductivityEvent(productivityEvent);

        milkProductionForProductivityEvent.productivityEventID = (int)productivityEventID;
        HerdDatabase.getInstance(context).getHerdDao().InsertMilkProductionForProductivityEvent(milkProductionForProductivityEvent);
        birthsForProductivityEvent.productivityEventID = (int) productivityEventID;
        HerdDatabase.getInstance(context).getHerdDao().InsertBirthsForProductivityEvent(birthsForProductivityEvent);

        return productivityEventID;
    }

    private void editProductivityEventForVisit()
    {

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

    private void editDynamicEventForVisit()
    {

    }

    public void addVisitToHerd(Context context,
                               int herdID,
                               Date whenitoccured,
                               int nBabies,int nYoung, int nOld,
                               List<DiseasesForHealthEvent> diseasesForHealthEvent,
                               List<SignsForHealthEvent> signsForHealthEvents,
                               List<BodyConditionForHealthEvent> bodyConditionForHealthEvents,
                               List<HealthInterventionForHealthEvent> healthInterventionForHealthEvents,
                               MilkProductionForProductivityEvent milkProductionForProductivityEvent,
                               BirthsForProductivityEvent birthsForProductivityEvent,
                               AnimalMovementsForDynamicEvent movements,
                               List<DeathsForDynamicEvent> deathsForDynamicEvent,
                               String comments)
    {
        HerdVisit herdVisit = new HerdVisit();
        herdVisit.HerdID = herdID;
        herdVisit.HerdVisitDate = whenitoccured;

        herdVisit.babiesAtVisit = nBabies;
        herdVisit.youngAtVisit= nYoung;
        herdVisit.oldAtVisit= nOld;
        herdVisit.comments = comments;

       long herdVisitID = HerdDatabase.getInstance(context).getHerdDao().InsertHerdVisit(herdVisit);

        //create a HealthEvent for this visit
        createHealthEventForVisit(context, (int)herdVisitID, diseasesForHealthEvent,signsForHealthEvents, bodyConditionForHealthEvents, healthInterventionForHealthEvents);

        //create Productivity event for this visit
        createProductivityEventForVisit(context, (int) herdVisitID, milkProductionForProductivityEvent,birthsForProductivityEvent);

        //create a dynamic event for this visit
        createDynamicEventForVisit(context, (int)herdVisitID,movements,deathsForDynamicEvent);

        Herd affectedHerd = HerdDatabase.getInstance(context).getHerdDao().getHerdByID(herdID).get(0);
        if(affectedHerd.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
            affectedHerd.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
        HerdDatabase.getInstance(context).getHerdDao().UpdateHerd(affectedHerd);

        Farmer affectedFarmer =  HerdDatabase.getInstance(context).getHerdDao().getFarmerByID(affectedHerd.farmerID).get(0);
        if(affectedFarmer.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
            affectedFarmer.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
        HerdDatabase.getInstance(context).getHerdDao().UpdateFarmer(affectedFarmer);

    }



    public List<HerdVisit> getVisitsForHerd(Context context, int herdID)
    {
        return HerdDatabase.getInstance(context).getHerdDao().getAllHerdVisitsByHerdID(herdID);
    }
}
