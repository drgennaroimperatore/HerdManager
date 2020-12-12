package com.ilri.herdmanager.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class BodyCondition {

    @PrimaryKey
    public int ID;
    public String label;
    public String species;
    public int stage;
    public String description;
    public BodyConditionSection section;

    public BodyCondition()
    {

    }

    public BodyCondition(int id, String label, String species, int stage, String description)
    {
        ID = id;
        this.label= label;
        this.species= species;
        this.stage= stage;
        this.description= description;
        this.section= BodyConditionSection.WHOLE_BODY;
    }

    public BodyCondition(int id, String label, String species, int stage, String description, BodyConditionSection section)
    {
        ID = id;
        this.label= label;
        this.species= species;
        this.stage= stage;
        this.description= description;
        this.section= section;
    }

}
