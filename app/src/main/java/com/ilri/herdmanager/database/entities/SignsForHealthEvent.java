package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = HealthEvent.class, parentColumns = "ID", childColumns = "healthEventID")})
public class SignsForHealthEvent
{
    @PrimaryKey
    public int ID;
    public int signID;
    public int healthEventID;
    public int numberOfAffectedBabies, numberOfAffectedYoung,numberOfAffectedOld;
}
