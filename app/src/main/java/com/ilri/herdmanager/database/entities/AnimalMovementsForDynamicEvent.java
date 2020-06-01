package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = DynamicEvent.class, parentColumns = "ID", childColumns = "dynamicEventID")})
public class AnimalMovementsForDynamicEvent {

    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int soldBabies, soldYoung, soldOld;
    public int lostBabies, lostYoung, lostOld;
    public int boughtBabies, boughtYoung, boughtOld;
    public int dynamicEventID;
    public String syncStatus;
    public int serverID;

    public AnimalMovementsForDynamicEvent()
    {
        soldBabies = soldOld = soldYoung = lostBabies = lostYoung = lostOld = boughtYoung = boughtBabies = boughtOld =0;
        syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
        serverID =-1;
    }
}
