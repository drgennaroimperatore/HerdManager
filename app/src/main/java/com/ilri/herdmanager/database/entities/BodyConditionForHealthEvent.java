package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BodyConditionForHealthEvent that = (BodyConditionForHealthEvent) o;
        return /*ID == that.ID &&*/
               /* healthEventID == that.healthEventID &&*/
                bodyConditionID == that.bodyConditionID &&
                nAffectedBabies == that.nAffectedBabies &&
                nAffectedYoung == that.nAffectedYoung &&
                nAffectedAdult == that.nAffectedAdult;
    }

}
