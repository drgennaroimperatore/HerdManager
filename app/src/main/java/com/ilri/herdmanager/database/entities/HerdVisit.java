package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = {@ForeignKey(entity = Herd.class, parentColumns = "ID", childColumns = "HerdID")})
public class HerdVisit {

    @PrimaryKey(autoGenerate = true)
    public int ID;
    public Date HerdVisitDate;
    public int babiesAtVisit;
    public int youngAtVisit;
    public int oldAtVisit;
    public int HerdID;

}
