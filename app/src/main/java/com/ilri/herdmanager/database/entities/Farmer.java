package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Farmer
{
    @PrimaryKey(autoGenerate = true)
    public int ID;
    public String firstName, secondName, region, district, kebele;
    public String syncStatus;

    public Farmer()
    {
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
    }

}
