package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Vaccines
{
    @PrimaryKey
    public int ID;
    public String Name;

    public Vaccines(){}

    public Vaccines(int id ,String name)
    {
        ID = id;
        Name = name;
    }
}
