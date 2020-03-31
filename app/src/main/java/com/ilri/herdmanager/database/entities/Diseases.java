package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Diseases")
public class Diseases {
    @PrimaryKey
    public int Id;
    public String Name;
}
