package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Farmer
{
    @PrimaryKey
    public int ID;
    public String firstName, secondName, region, district, kebele;

}
