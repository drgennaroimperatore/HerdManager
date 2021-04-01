package com.ilri.herdmanager.database.entities;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey
        (onDelete = CASCADE, entity = HerdVisit.class,
                parentColumns = "ID",
                childColumns = "HerdVisitID"))
public class ProductivityEvent {

    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int HerdVisitID;
    public String syncStatus;
    public int serverID=-1;

    public ProductivityEvent()
    {
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
    }

}
