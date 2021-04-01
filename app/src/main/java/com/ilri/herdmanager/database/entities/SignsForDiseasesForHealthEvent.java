package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {@ForeignKey(onDelete = CASCADE, entity = DiseasesForHealthEvent.class,
        parentColumns = "ID", childColumns = "diseaseForHealthEventID")})
public class SignsForDiseasesForHealthEvent implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int signID;
    public String presence;
    public int diseaseForHealthEventID;
}
