package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AdditionalSigns
{
    @PrimaryKey
    public int Id;
    public String Name;

    public AdditionalSigns(int Id, String Name)
    {
        this.Id = Id;
        this.Name = Name;
    }
}
