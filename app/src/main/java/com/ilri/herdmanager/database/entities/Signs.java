package com.ilri.herdmanager.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Signs")

public class Signs {
    @PrimaryKey
    public int Id;
    @ColumnInfo(name = "Type_Of_Value")
    public int TypeOfValue;
    public String Probability;
    public String Name;
}