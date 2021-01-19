package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Vaccines.class, parentColumns = "ID", childColumns = "vaccineID")})
public class VaccinesForSpecies
{
    @PrimaryKey
   public int ID;
    public String species;
    public int vaccineID;
    public VaccinesForSpecies() {}

    public VaccinesForSpecies(int id, String specID, int vaccineID)
    {
        ID=id;
        species=specID;
        this.vaccineID = vaccineID;
    }

}
