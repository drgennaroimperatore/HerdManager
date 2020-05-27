package com.ilri.herdmanager.database.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = HealthEvent.class, parentColumns = "ID", childColumns = "healthEventID")})
public class SignsForHealthEvent
{
    @PrimaryKey (autoGenerate = true)
    public int ID;
    public int signID;
    public int healthEventID;
    public int numberOfAffectedBabies, numberOfAffectedYoung,numberOfAffectedOld;
    public String syncStatus;

    @Override
    public boolean equals(@Nullable Object obj) {
        SignsForHealthEvent she2 = (SignsForHealthEvent)obj;
        return (signID== she2.signID);
    }

    public SignsForHealthEvent()
    {
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
    }
}
