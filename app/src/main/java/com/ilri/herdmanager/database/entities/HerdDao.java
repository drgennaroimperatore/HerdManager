package com.ilri.herdmanager.database.entities;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HerdDao {
    @Query("SELECT * FROM HerdVisit")
    List<HerdVisit> getAllHerdVisits();

    @Insert
    void InsertHerdVisit(HerdVisit visit);

    @Delete
    void DeleteHerdVisit(HerdVisit visit);
}
