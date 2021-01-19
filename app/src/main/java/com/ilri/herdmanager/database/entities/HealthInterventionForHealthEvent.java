package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity( foreignKeys = {@ForeignKey(entity = HealthIntervention.class, parentColumns = "ID", childColumns = "healthInterventionID"),
                        @ForeignKey(entity = HealthEvent.class, parentColumns = "ID", childColumns = "healthEventID")})
public class HealthInterventionForHealthEvent
{
    @PrimaryKey(autoGenerate = true)
    public int ID;
    public int healthInterventionID;
    public int healthEventID;
    public String vaccinationName;
    public int nBabies; int nYoung; int nOld;
    public String comments;

}
