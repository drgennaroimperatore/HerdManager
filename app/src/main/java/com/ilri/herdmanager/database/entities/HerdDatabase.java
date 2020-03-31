package com.ilri.herdmanager.database.entities;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ilri.herdmanager.database.converters.DateConverter;

@Database(entities = {HerdVisit.class, ProductivityEvent.class}, version= 1)
@TypeConverters({DateConverter.class})
public abstract class HerdDatabase extends RoomDatabase
{

public abstract HerdDao herdDao();
}
