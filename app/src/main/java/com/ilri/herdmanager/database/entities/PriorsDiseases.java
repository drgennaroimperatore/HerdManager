package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "PriorsDiseases",
        foreignKeys = {
                @ForeignKey( entity = Animals.class,
                        parentColumns = "Id",
                        childColumns = "AnimalID"),
                @ForeignKey( entity = Diseases.class,
                        parentColumns = "Id",
                        childColumns = "DiseaseID")})
public class PriorsDiseases {
    @PrimaryKey
    public int Id;
    public int DiseaseID;
    public int AnimalID;
    public String Probability;
}
