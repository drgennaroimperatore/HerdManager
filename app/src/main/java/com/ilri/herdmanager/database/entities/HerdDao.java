package com.ilri.herdmanager.database.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HerdDao {

    @Insert
    void InsertHerd(Herd herd);

    @Delete
    void DeleteHerd(Herd herd);


    @Query("SELECT * FROM HerdVisit")
    List<HerdVisit> getAllHerdVisits();

    @Insert
    void InsertHerdVisit(HerdVisit visit);

    @Delete
    void DeleteHerdVisit(HerdVisit visit);

    @Insert
    void InsertFarmer(Farmer farmer);

    @Delete
    void DeleteFarmer(Farmer farmer);


}
