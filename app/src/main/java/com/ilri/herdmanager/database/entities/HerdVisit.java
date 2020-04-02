package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = {@ForeignKey(entity = Herd.class, parentColumns = "ID", childColumns = "HerdID")})
public class HerdVisit {
    @PrimaryKey
    public int ID;
    Date HerdVisitDate;
    public int herdSizeAtVisit;
    int HerdID;

}
