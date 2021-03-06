package com.ilri.herdmanager.database.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HerdDao {

    @Insert
    long InsertHerd(Herd herd);

    @Delete
    void DeleteHerd(Herd herd);


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

    @Query("SELECT * FROM HerdVisit WHERE HerdVisit.ID =:herdVisitID")
    List<HerdVisit> getHerdVisitByID(int herdVisitID);

    @Query("SELECT * FROM herdvisit WHERE Herdvisit.HerdID =:herdID ORDER BY HerdVisit.HerdVisitDate DESC")
    List<HerdVisit> getAllHerdVisitsByHerdID(int herdID);

    @Query("SELECT * FROM HealthEvent WHERE HealthEvent.herdVisitID =:herdVisitID")
    List<HealthEvent> getHealthEventForVisit(int herdVisitID);

    @Query("SELECT * FROM DiseasesForHealthEvent WHERE DiseasesForHealthEvent.healthEventID= :healthEventID")
    List<DiseasesForHealthEvent> getDiseasesForHealthEvent(int healthEventID);

    @Query("SELECT * FROM SignsForHealthEvent WHERE SignsForHealthEvent.healthEventID =:healthEventID")
    List<SignsForHealthEvent> getSignsForHealthEvent(int healthEventID);

    @Query("SELECT * FROM ProductivityEvent WHERE ProductivityEvent.HerdVisitID= :herdVisitID")
    List<ProductivityEvent> getProductivityEventForVisit(int herdVisitID);

    @Query("SELECT * FROM MilkProductionForProductivityEvent WHERE MilkProductionForProductivityEvent.productivityEventID =:productivityEventID")
    List<MilkProductionForProductivityEvent> getMilkProductionForProductivityEvent(int productivityEventID);

    @Query("SELECT * FROM BirthsForProductivityEvent WHERE BirthsForProductivityEvent.productivityEventID=:productivityEventID")
    List<BirthsForProductivityEvent> getBirthsForProductivityEvent(int productivityEventID);

    @Query("SELECT * FROM DynamicEvent WHERE DynamicEvent.herdVisitID =:herdVisitID")
    List<DynamicEvent> getDynamicEventForVisit(int herdVisitID);

    @Query("SELECT * FROM AnimalMovementsForDynamicEvent WHERE AnimalMovementsForDynamicEvent.dynamicEventID =:dynamicEventID")
    List<AnimalMovementsForDynamicEvent> getAnimalMovementsForDynamicEvent(int dynamicEventID);

    @Query("SELECT * FROM DeathsForDynamicEvent WHERE DeathsForDynamicEvent.dynamicEventID =:dynamicEventID")
    List<DeathsForDynamicEvent> getDeathsForDynamicEvent(int dynamicEventID);

    @Query("SELECT * FROM Farmer WHERE Farmer.firstName Like '%' || :name || '%' OR Farmer.secondName Like '%' || :name || '%' ")
    List<Farmer> getFarmerbyName(String name);

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
    long InsertProductivityEvent(ProductivityEvent productivityEvent);

    @Delete
    void DeleteProductivityEvent(ProductivityEvent productivityEvent);

    @Insert
    long InsertMilkProductionForProductivityEvent(MilkProductionForProductivityEvent milkProductionForProductivityEvent);

    @Delete
    void DeleteMilkProductionForProductivityEvent(MilkProductionForProductivityEvent milkProductionForProductivityEvent);

    @Insert
    long InsertBirthsForProductivityEvent (BirthsForProductivityEvent birthsForProductivityEvent);

    @Delete
    void DeleteBirthsForProductivityEvent(BirthsForProductivityEvent birthsForProductivityEvent);

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

    @Update
    int UpdateFarmer(Farmer f);

    @Update
    int UpdateHerd(Herd herd);

    @Update
    int UpdateHerdVisit(HerdVisit herdVisit);

    @Update
    int UpdateHealthEvent (HealthEvent healthEvent);

    @Update
    int UpdateDiseaseForHealthEvent(DiseasesForHealthEvent dhe);

    @Update
    int UpdateSignForHealthEvent(SignsForHealthEvent she);


    @Update
    int UpdateProductivityEvent(ProductivityEvent productivityEvent);

    @Update
    int UpdateBirthsForProductivityEvent (BirthsForProductivityEvent bpe);

    @Update
    int UpdateMilkForProductivityEvent(MilkProductionForProductivityEvent mpe);

    @Update
    int UpdateDynamicEvent(DynamicEvent dynamicEvent);

    @Update
    int UpdateAnimalMovementsForDynamicEvent(AnimalMovementsForDynamicEvent animalMovementsForDynamicEvent);

    @Update
    int UpdateDeathsForDynamicEvent (DeathsForDynamicEvent dde);


}
