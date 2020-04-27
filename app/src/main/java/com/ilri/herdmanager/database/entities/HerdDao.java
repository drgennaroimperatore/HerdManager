package com.ilri.herdmanager.database.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HerdDao {

    @Insert
    long InsertHerd(Herd herd);

    @Delete
    void DeleteHerd(Herd herd);


    @Query("SELECT * FROM HerdVisit WHERE HerdID= :id")
    List<HerdVisit> getAllHerdVisitsByHerdId(int id);

    @Query("SELECT * FROM Herd")
    List<Herd> getAllHerds();

    @Query("SELECT * FROM Herd WHERE ID = :herdID")
    List<Herd> getHerdByID(int herdID);

    @Query("SELECT * FROM Farmer")
    List<Farmer> getAllFarmers();

    @Query("SELECT * FROM Farmer WHERE Farmer.ID= :ID")
    List<Farmer> getFarmerByID(int ID);

    @Query("SELECT * FROM Herd WHERE Herd.farmerID =:farmerID")
    List<Herd> getHerdsByFarmerID(int farmerID);

    @Query("SELECT * FROM herdvisit WHERE Herdvisit.HerdID =:herdID ORDER BY HerdVisit.HerdVisitDate DESC")
    List<HerdVisit> getAllHerdVisitsByHerdID(int herdID);

    @Query("SELECT * FROM HealthEvent WHERE HealthEvent.herdVisitID =:herdVisitID")
    List<HealthEvent> getHealthEventForVisit(int herdVisitID);

    @Query("SELECT * FROM DiseasesForHealthEvent WHERE DiseasesForHealthEvent.healthEventID= :healthEventID")
    List<DiseasesForHealthEvent> getDiseasesForHealthEvent(int healthEventID);

    @Query("SELECT * FROM SignsForHealthEvent WHERE SignsForHealthEvent.healthEventID =:healthEventID")
    List<SignsForHealthEvent> getSignsForHealthEvent(int healthEventID);

    @Insert
    long InsertHerdVisit(HerdVisit visit);

    @Delete
    void DeleteHerdVisit(HerdVisit visit);

    @Insert
    long InsertFarmer(Farmer farmer);

    @Delete
    void DeleteFarmer(Farmer farmer);

    @Insert
    long InsertHealthEvent(HealthEvent healthEvent);

    @Delete
    void DeleteHerdEvent(HealthEvent healthEvent);

    @Insert
    long InsertDiseaseForHealthEvent(DiseasesForHealthEvent diseasesForHealthEvent);

    @Delete
    void DeleteDiseaseForHealthEvent(DiseasesForHealthEvent diseasesForHealthEvent);

    @Insert
    long InsertSignForHealthEvent(SignsForHealthEvent signsForHealthEvent);

    @Delete
    void DeleteSignsForHealthEvent(SignsForHealthEvent signsForHealthEvent);

    @Insert
    long InsertDynamicEvent(DynamicEvent dynamicEvent);

    @Delete
    void DeleteDynamicEvemt(DynamicEvent dynamicEvent);

    @Insert
    long InsertAnimalMovementForDynamicEvent(AnimalMovementsForDynamicEvent animalMovementsForDynamicEvent);

    @Delete
    void DeleteAnimalMovementForDynamicEvent(AnimalMovementsForDynamicEvent animalMovementsForDynamicEvent);

    @Insert
    long InsertDeathForDynamicEvent (DeathsForDynamicEvent deathsForDynamicEvent);

    @Delete
    void DeleteDeathForDynamicEvent (DeathsForDynamicEvent deathsForDynamicEvent);


}
