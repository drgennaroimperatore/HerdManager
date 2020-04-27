package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = HerdVisit.class, parentColumns = "ID", childColumns = "herdVisitID")})
public class DynamicEvent {
    @PrimaryKey (autoGenerate = true)
    public int ID;
    public int herdVisitID;


}
