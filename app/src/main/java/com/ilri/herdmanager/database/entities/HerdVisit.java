package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class HerdVisit {
    @PrimaryKey
    public int ID;
    Date HerdVisitDate;
    int HerdID;

}
