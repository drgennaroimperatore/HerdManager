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


    @Query("SELECT * FROM HerdVisit")
    List<HerdVisit> getAllHerdVisits();

    @Query("SELECT * FROM Herd")
    List<Herd> getAllHerds();

    @Query("SELECT * FROM Farmer")
    List<Farmer> getAllFarmers();

    @Query("SELECT * FROM Farmer WHERE Farmer.ID= :ID")
    List<Farmer> getFarmerByID(int ID);

    @Insert
    long InsertHerdVisit(HerdVisit visit);

    @Delete
    void DeleteHerdVisit(HerdVisit visit);

    @Insert
    long InsertFarmer(Farmer farmer);

    @Delete
    void DeleteFarmer(Farmer farmer);


}
