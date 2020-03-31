package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Likelihoods",
        foreignKeys = {
                @ForeignKey( entity = Animals.class,
                        parentColumns = "Id",
                        childColumns = "AnimalId"),
                @ForeignKey( entity = Signs.class,
                        parentColumns = "Id",
                        childColumns = "SignId" ),
                @ForeignKey( entity = Diseases.class,
                        parentColumns = "Id",
                        childColumns = "DiseaseId")
        }
)
public class Likelihoods {

    @PrimaryKey
    public int Id;
    public String Value;
    public int AnimalId;
    public int SignId;
    public int DiseaseId;
}
