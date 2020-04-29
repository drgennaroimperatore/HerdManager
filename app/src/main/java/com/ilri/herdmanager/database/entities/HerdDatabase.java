package com.ilri.herdmanager.database.entities;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ilri.herdmanager.database.converters.DateConverter;

@Database(entities = {Herd.class,
        Farmer.class,
        HerdVisit.class,
        ProductivityEvent.class,
        HealthEvent.class,
        DynamicEvent.class,
        SignsForHealthEvent.class,
        DiseasesForHealthEvent.class,
        AnimalMovementsForDynamicEvent.class,
        DeathsForDynamicEvent.class,
MilkProductionForProductivityEvent.class,
BirthsForProductivityEvent.class}, version= 9)


@TypeConverters({DateConverter.class})
public abstract class HerdDatabase extends RoomDatabase
{

    HerdDao mHerdDao;

    public static HerdDatabase mInstance = null;

    public static HerdDatabase getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = Room.databaseBuilder(context,
                    HerdDatabase.class, "herddb").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return mInstance;
    }



    public abstract HerdDao getHerdDao();
}
