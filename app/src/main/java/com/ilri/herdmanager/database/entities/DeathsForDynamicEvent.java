package com.ilri.herdmanager.database.entities;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = HerdVisit.class, parentColumns = "ID", childColumns = "dynamicEventID")})
public class DeathsForDynamicEvent {
    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int dynamicEventID;
    public String causeOfDeath;
    public int deadBabies, deadYoung, deadOld;

    @Override
    public boolean equals(@Nullable Object obj) {
        DeathsForDynamicEvent dde2 = (DeathsForDynamicEvent)obj;
        return causeOfDeath.equals(dde2.causeOfDeath);
    }
}
