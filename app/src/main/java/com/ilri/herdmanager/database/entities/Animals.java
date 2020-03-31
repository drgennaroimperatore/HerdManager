package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Animals")
public class Animals {
    @PrimaryKey
    public int Id;
    public String Name;
    public String Sex;
    public String Age;
}
