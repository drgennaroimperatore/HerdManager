package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(onDelete = CASCADE, entity = HerdVisit.class, parentColumns = "ID", childColumns = "herdVisitID")})
public class HealthEvent {

    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int herdVisitID;
    public String syncStatus;
    public int serverID=-1;

    public HealthEvent()
    {
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();

    }


}
