package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(onDelete = CASCADE, entity = Herd.class, parentColumns = "ID", childColumns = "HerdID")})
public class HerdVisit {

    @PrimaryKey(autoGenerate = true)
    public int ID;
    public Date HerdVisitDate;
    public int babiesAtVisit;
    public int youngAtVisit;
    public int oldAtVisit;
    public int HerdID;
    public String syncStatus;
    public String comments;
    public int serverID=-1;

    public HerdVisit()
    {
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
    }

}
