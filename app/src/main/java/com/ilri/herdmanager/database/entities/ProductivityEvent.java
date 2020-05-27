package com.ilri.herdmanager.database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey
        (entity = HerdVisit.class,
                parentColumns = "ID",
                childColumns = "HerdVisitID"))
public class ProductivityEvent {

    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int HerdVisitID;
    public String syncStatus;

    public ProductivityEvent()
    {
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
    }

}
