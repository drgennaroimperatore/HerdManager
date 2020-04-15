package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = HealthEvent.class, parentColumns = "ID", childColumns = "healthEventID")})
public class DiseasesForHealthEvent {
    @PrimaryKey (autoGenerate = true)
    public int ID;
    public int diseaseID;
    public int healthEventID;
}
