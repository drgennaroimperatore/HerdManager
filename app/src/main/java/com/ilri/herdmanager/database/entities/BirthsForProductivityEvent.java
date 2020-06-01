package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = ProductivityEvent.class, parentColumns = "ID", childColumns = "productivityEventID"))
public class BirthsForProductivityEvent {
    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int nOfGestatingAnimals;
    public int nOfBirths;
    public int productivityEventID;
    public String syncStatus;
    public int serverID;

    public BirthsForProductivityEvent()
    {

        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
        serverID = -1;

    }
}
