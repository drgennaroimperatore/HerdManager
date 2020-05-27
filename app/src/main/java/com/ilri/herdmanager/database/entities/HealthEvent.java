package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = HerdVisit.class, parentColumns = "ID", childColumns = "herdVisitID")})
public class HealthEvent {

    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int herdVisitID;
    public String syncStatus;

    public HealthEvent()
    {
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();

    }


}
