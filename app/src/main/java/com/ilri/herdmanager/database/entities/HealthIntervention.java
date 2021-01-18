package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;

import androidx.room.PrimaryKey;

@Entity
public class HealthIntervention {
    @PrimaryKey
    public int ID;
    public String name;

    public HealthIntervention (int id, String n)
    {
        ID = id; name =n;
    }
}
