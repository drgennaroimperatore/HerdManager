package com.ilri.herdmanager.classes;

import com.ilri.herdmanager.database.entities.AnimalMovementsForDynamicEvent;
import com.ilri.herdmanager.database.entities.DeathsForDynamicEvent;

import java.util.List;

public class DynamicEventContainer {

   public List<DeathsForDynamicEvent> mDeaths;
   public AnimalMovementsForDynamicEvent mMovements;
}
