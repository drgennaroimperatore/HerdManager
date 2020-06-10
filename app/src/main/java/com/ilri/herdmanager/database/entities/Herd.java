package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Farmer.class, parentColumns = "ID", childColumns = "farmerID")})
public class Herd {

    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int speciesID;
    public int farmerID;
    public String syncStatus;
    public int nBabies,nYoung,nOld;
    public int serverID=-1;

    public Herd()
    {
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
    }
}
