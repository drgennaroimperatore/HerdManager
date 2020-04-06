package com.ilri.herdmanager.database.entities;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;



    @Database(entities =
            {       Animals.class,
                    Diseases.class,
                    Likelihoods.class,
                    PriorsDiseases.class,
                    SignCores.class,
                    Signs.class}, version = 1, exportSchema = false)
    public abstract class ADDB extends RoomDatabase {
        ADDBDAO mADDBDAO;

        private static ADDB mInstance;

        @NonNull
        @Override
        protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
            return null;
        }

        @NonNull
        @Override
        protected InvalidationTracker createInvalidationTracker() {
            return null;
        }

        public static ADDB getInstance(Context context) {
            if (mInstance == null) {
                mInstance = Room.databaseBuilder(context, ADDB.class, "ADDB")
                        .createFromAsset("ADDB.db").allowMainThreadQueries().build();
            }

            return mInstance;
        }

        public static void destroyInstance() {
            mInstance = null;
        }

        @Override
        public void clearAllTables() {

        }

        public abstract ADDBDAO getADDBDAO();

    }
