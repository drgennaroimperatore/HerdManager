package com.ilri.herdmanager.database.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = HealthEvent.class, parentColumns = "ID", childColumns = "healthEventID")})
public class DiseasesForHealthEvent {

    @PrimaryKey (autoGenerate = true)
    public int ID;
    public int diseaseID;
    public int healthEventID;
    public int numberOfAffectedBabies, numberOfAffectedYoung,numberOfAffectedOld;
    public String syncStatus;

    @Override
    public boolean equals(@Nullable Object obj) {
        DiseasesForHealthEvent dhe2 = (DiseasesForHealthEvent)obj;
        return (diseaseID == dhe2.diseaseID);
    }

    public DiseasesForHealthEvent()
    {
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
    }
}
