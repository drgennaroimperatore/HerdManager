package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(onDelete =CASCADE,  entity = HerdVisit.class, parentColumns = "ID", childColumns = "herdVisitID")})
public class DynamicEvent {
    @PrimaryKey (autoGenerate = true)
    public int ID;
    public int herdVisitID;
    public String syncStatus;
    public int serverID =-1;

    public DynamicEvent()
    {
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
    }


}
