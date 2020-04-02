package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Farmer.class, parentColumns = "ID", childColumns = "farmerID")})
public class Herd {

    @PrimaryKey
    public int ID;
    public int speciesID;
    public int farmerID;
}
