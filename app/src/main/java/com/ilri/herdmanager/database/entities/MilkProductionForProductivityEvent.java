package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = ProductivityEvent.class, parentColumns = "ID", childColumns = "productivityEventID"))
public class MilkProductionForProductivityEvent {
   @PrimaryKey(autoGenerate = true)
   public int ID;
   public int litresOfMilkPerDay;
   public int numberOfLactatingAnimals;
   public int productivityEventID;
   public String syncStatus;
   public int serverID =-1;

   public MilkProductionForProductivityEvent()
   {
      syncStatus = SyncStatus.NOT_SYNCHRONISED.toString();
   }
}
