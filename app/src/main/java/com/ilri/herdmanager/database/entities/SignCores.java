package com.ilri.herdmanager.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "SignCores", foreignKeys = {
        @ForeignKey
                (entity = Signs.class, parentColumns = "Id", childColumns = "SignID"),
        @ForeignKey
                (entity = Animals.class, parentColumns = "Id", childColumns = "AnimalID"),

})
public class SignCores {
    @PrimaryKey
    @ColumnInfo(name = "ID")
    public int Id;
    public int SignID;
    public int AnimalID;

}