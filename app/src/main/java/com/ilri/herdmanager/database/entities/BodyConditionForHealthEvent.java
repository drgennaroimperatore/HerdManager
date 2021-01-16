package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = HealthEvent.class, parentColumns = "ID", childColumns ="healthEventID" ),
        @ForeignKey(entity = BodyCondition.class, parentColumns = "ID", childColumns = "bodyConditionID")})
public class BodyConditionForHealthEvent {
    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int healthEventID;
    public int bodyConditionID;
    public int nAffectedBabies, nAffectedYoung,nAffectedAdult;
    public String syncStatus;
    public int serverID = -1;

    public BodyConditionForHealthEvent() {syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();}
}
