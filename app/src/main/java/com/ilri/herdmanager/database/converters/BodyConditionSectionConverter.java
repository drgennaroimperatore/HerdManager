package com.ilri.herdmanager.database.converters;


import androidx.room.TypeConverter;

import com.ilri.herdmanager.database.entities.BodyCondition;
import com.ilri.herdmanager.database.entities.BodyConditionSection;

public class BodyConditionSectionConverter {
    @TypeConverter
    public static BodyConditionSection toBodyConditionSection(String bodyConditionSection)
    {
        return BodyConditionSection.valueOf(bodyConditionSection);
    }
    @TypeConverter
    public static String fromBodyConditionSection(BodyConditionSection bodyConditionSection)
    {
        return bodyConditionSection.toString();
    }
}
