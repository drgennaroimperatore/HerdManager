package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(foreignKeys = {@ForeignKey(entity = DiseasesForHealthEvent.class,
        parentColumns = "ID", childColumns = "diseaseForHealthEventID")})
public class SignsForDiseasesForHealthEvent implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int signID;
    public String presence;
    public int diseaseForHealthEventID;
}
