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
import com.ilri.herdmanager.database.entities.HerdDao;
import com.ilri.herdmanager.database.entities.HerdDatabase;
import com.ilri.herdmanager.database.entities.HerdVisit;
import com.ilri.herdmanager.database.entities.MilkProductionForProductivityEvent;
import com.ilri.herdmanager.database.entities.ProductivityEvent;
import com.ilri.herdmanager.database.entities.SignsForHealthEvent;
import com.ilri.herdmanager.database.entities.SyncStatus;

import java.net.ConnectException;
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
      //  HealthEvent associatedHealthEvent =   HerdDatabase.getInstance(context).getHerdDao().gethea
        if(signsForHealthEvent.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
            signsForHealthEvent.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
        HerdDatabase.getInstance(context).getHerdDao().UpdateSignForHealthEvent(signsForHealthEvent);
        updateSyncStatusOfHealthEvent(context,signsForHealthEvent.healthEventID);
    }

    public void deleteSignForHealthEvent(Context context, SignsForHealthEvent signsForHealthEvent)
    {
        HerdDatabase.getInstance(context).getHerdDao().DeleteSignsForHealthEvent(signsForHealthEvent);
    }

    public void addSignToExistingVisit(Context context, SignsForHealthEvent newSign, int herdVisitID)
    {
       HealthEvent healthEvent = HerdDatabase.getInstance(context).getHerdDao().getHealthEventForVisit(herdVisitID).get(0);
       newSign.healthEventID = healthEvent.ID;
       HerdDatabase.getInstance(context).getHerdDao().InsertSignForHealthEvent(newSign);
       updateSyncStatusOfHealthEvent(context,healthEvent.ID);
    }

    public void addHealthInterventionToExistingVisit(Context context, HealthInterventionForHealthEvent healthInterventionForHealthEvent, int herdVisitID)
    {
        HealthEvent healthEvent = HerdDatabase.getInstance(context).getHerdDao().getHealthEventForVisit(herdVisitID).get(0);
        healthInterventionForHealthEvent.healthEventID = healthEvent.ID;
        HerdDatabase.getInstance(context).getHerdDao().InsertHealthInterventionForHealthEvent(healthInterventionForHealthEvent);
        updateSyncStatusOfHealthEvent(context,healthInterventionForHealthEvent.healthEventID);

    }

    public void addBodyConditionsToHealthEvent(Context context, List<BodyConditionForHealthEvent> bodyConditionForHealthEvents, int herdVisitID)
    {
        HealthEvent healthEvent = HerdDatabase.getInstance(context).getHerdDao().getHealthEventForVisit(herdVisitID).get(0);
        for(BodyConditionForHealthEvent bc: bodyConditionForHealthEvents)
        {
            bc.healthEventID = healthEvent.ID;
            HerdDatabase.getInstance(context).getHerdDao().InsertBodyConditionForHealthEvent(bc);
        }

    }

    public void editHealthInterventionForExistingVisit(Context context, HealthInterventionForHealthEvent healthInterventionForHealthEvent)
    {
        if(healthInterventionForHealthEvent.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
          healthInterventionForHealthEvent.syncStatus =SyncStatus.PARTIALLY_SYNCHRONISED.toString();

        HerdDatabase.getInstance(context).getHerdDao().UpdateHealthInterventionForHealthEvent(healthInterventionForHealthEvent);
        updateSyncStatusOfHealthEvent(context, healthInterventionForHealthEvent.healthEventID);
    }

    public void deleteHealthInterventionForHealthEventForExistingVisit(Context context, HealthInterventionForHealthEvent healthInterventionForHealthEvent)
    {
        HerdDatabase.getInstance(context).getHerdDao().DeleteHealthInterventionForHealthEvent(healthInterventionForHealthEvent);
        updateSyncStatusOfHealthEvent(context,healthInterventionForHealthEvent.healthEventID);
    }

    public void editBodyConditionForHealthEventForExistingVisit(Context context, BodyConditionForHealthEvent bche)
    {
        HerdDatabase.getInstance(context).getHerdDao().UpdateBodyConditionForHealthEvent(bche);
        updateSyncStatusOfHealthEvent(context,bche.healthEventID);
    }



    private long createProductivityEventForVisit (Context context, int herdVisitID, MilkProductionForProductivityEvent milkProductionForProductivityEvent, BirthsForProductivityEvent birthsForProductivityEvent)
    {
        ProductivityEvent productivityEvent = new ProductivityEvent();
        productivityEvent.HerdVisitID = herdVisitID;

        long productivityEventID = HerdDatabase.getInstance(context).getHerdDao().InsertProductivityEvent(productivityEvent);

        milkProductionForProductivityEvent.productivityEventID = (int)productivityEventID;

        if(HerdDatabase.getInstance(context).getHerdDao().getMilkProductionForProductivityEvent((int)productivityEventID).size()==0)
            //we need this line to ensure no more than one is created....
            HerdDatabase.getInstance(context).getHerdDao().InsertMilkProductionForProductivityEvent(milkProductionForProductivityEvent);

        birthsForProductivityEvent.productivityEventID = (int) productivityEventID;
        if(HerdDatabase.getInstance(context).getHerdDao().getBirthsForProductivityEvent((int)productivityEventID).size()==0)
            HerdDatabase.getInstance(context).getHerdDao().InsertBirthsForProductivityEvent(birthsForProductivityEvent);

        return productivityEventID;
    }

    private void editProductivityEventForExistingHealthEvent()
    {

    }


    public void editMilkProductionForExistingProductivityEvent(Context context, MilkProductionForProductivityEvent mpe, int herdVisitID)
    {
        ProductivityEvent productivityEvent = HerdDatabase.getInstance(context).getHerdDao().getProductivityEventForVisit(herdVisitID).get(0);
        if(HerdDatabase.getInstance(context).getHerdDao().getMilkProductionForProductivityEvent(productivityEvent.ID).size()==0)
        {
            mpe.productivityEventID = productivityEvent.ID;
            mpe.ID = (int)HerdDatabase.getInstance(context).getHerdDao().InsertMilkProductionForProductivityEvent(mpe);
        }
        else {
          MilkProductionForProductivityEvent oldMpe=  HerdDatabase.getInstance(context).getHerdDao().getMilkProductionForProductivityEvent(productivityEvent.ID).get(0);
          mpe.ID = oldMpe.ID;
          mpe.productivityEventID = oldMpe.productivityEventID;
          if(mpe.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
              mpe.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
          HerdDatabase.getInstance(context).getHerdDao().UpdateMilkForProductivityEvent(mpe);
        }
        updateSyncStatusOfProductivityElement(context, productivityEvent.ID);

    }

    public void editBirthsForExistingProductivityEvent(Context context, BirthsForProductivityEvent bpe, int herdVisitID)
    {
        ProductivityEvent productivityEvent = HerdDatabase.getInstance(context).getHerdDao().getProductivityEventForVisit(herdVisitID).get(0);
        if(HerdDatabase.getInstance(context).getHerdDao().getBirthsForProductivityEvent(herdVisitID).size()==0)
        {
            bpe.ID = (int)HerdDatabase.getInstance(context).getHerdDao().InsertBirthsForProductivityEvent(bpe);
            bpe.productivityEventID = (int)productivityEvent.ID;
            bpe.syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
        }

        else {
           BirthsForProductivityEvent oldBpe = HerdDatabase.getInstance(context).getHerdDao().getBirthsForProductivityEvent((int)productivityEvent.ID).get(0);
           bpe.ID = oldBpe.ID;
           bpe.syncStatus = oldBpe.syncStatus;
           bpe.productivityEventID =oldBpe.productivityEventID;

           if(bpe.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
               bpe.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            HerdDatabase.getInstance(context).getHerdDao().UpdateBirthsForProductivityEvent(bpe);
        }
        updateSyncStatusOfProductivityElement(context,productivityEvent.ID);
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

    public void editAnimalMovementsForExistingDynamicEvent(Context context, AnimalMovementsForDynamicEvent amde)
    {
        HerdDatabase.getInstance(context).getHerdDao().UpdateAnimalMovementsForDynamicEvent(amde);
        if(amde.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
        {
            amde.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            HerdDatabase.getInstance(context).getHerdDao().UpdateAnimalMovementsForDynamicEvent(amde);
        }
        updateSyncStatusOfDynamicEvent(context,amde.dynamicEventID);

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

    private void updateSyncStatusOfHealthEvent(Context context, int healthEventID)
    {
        HerdDao dao = HerdDatabase.getInstance(context).getHerdDao();
        HealthEvent healthEvent = dao.getHealthEventByID(healthEventID);
        if(healthEvent.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString())) {
            healthEvent.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateHealthEvent(healthEvent);
        }
        HerdVisit herdVisit = dao.getHerdVisitByID(healthEvent.herdVisitID).get(0);
        Herd herd = dao.getHerdByID(herdVisit.HerdID).get(0);
        Farmer affectedFarmer = dao.getFarmerByID(herd.farmerID).get(0);

        if(affectedFarmer.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString())) {
            affectedFarmer.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateFarmer(affectedFarmer);
        }

        if(herd.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString())) {
            herd.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateHerd(herd);
        }
        if(herdVisit.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString())) {
            herdVisit.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateHerdVisit(herdVisit);
        }


    }

    private void updateSyncStatusOfProductivityElement(Context context,  int productivityEventID)
    {
        HerdDao dao = HerdDatabase.getInstance(context).getHerdDao();
        ProductivityEvent productivityEvent = dao.getProductivityEventByID(productivityEventID);
        if(productivityEvent.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
        {
            productivityEvent.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateProductivityEvent(productivityEvent);
        }

        HerdVisit herdVisit = dao.getHerdVisitByID(productivityEvent.HerdVisitID).get(0);
        Herd herd = dao.getHerdByID(herdVisit.HerdID).get(0);


        Farmer affectedFarmer = dao.getFarmerByID(herd.farmerID).get(0);


        if(affectedFarmer.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString())) {
            affectedFarmer.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateFarmer(affectedFarmer);
        }

        if(herd.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString())) {
            herd.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateHerd(herd);
        }
        if(herdVisit.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString())) {
            herdVisit.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateHerdVisit(herdVisit);
        }

    }

    private void updateSyncStatusOfDynamicEvent(Context context, int dynamicEventID)
    {
        HerdDao dao = HerdDatabase.getInstance(context).getHerdDao();
        DynamicEvent dynamicEvent = dao.getDynamicEventByID(dynamicEventID);
        if(dynamicEvent.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString()))
        {
            dynamicEvent.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateDynamicEvent(dynamicEvent);
        }
        HerdVisit herdVisit = dao.getHerdVisitByID(dynamicEvent.herdVisitID).get(0);
        Herd herd = dao.getHerdByID(herdVisit.HerdID).get(0);
        Farmer affectedFarmer = dao.getFarmerByID(herd.farmerID).get(0);

        if(affectedFarmer.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString())) {
            affectedFarmer.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateFarmer(affectedFarmer);
        }

        if(herd.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString())) {
            herd.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateHerd(herd);
        }
        if(herdVisit.syncStatus.equals(SyncStatus.SYNCHRNOISED.toString())) {
            herdVisit.syncStatus = SyncStatus.PARTIALLY_SYNCHRONISED.toString();
            dao.UpdateHerdVisit(herdVisit);
        }


    }


}
