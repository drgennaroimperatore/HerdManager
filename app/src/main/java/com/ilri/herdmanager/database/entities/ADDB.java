package com.ilri.herdmanager.database.entities;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;


@Database(entities =
            {       Animals.class,
                    Diseases.class,
                    Likelihoods.class,
                    PriorsDiseases.class,
                    SignCores.class,
                    Signs.class, AdditionalSigns.class}, version = 2, exportSchema = false)
    public abstract class ADDB extends RoomDatabase {
       ADDBDAO mADDBDAO;
    public abstract ADDBDAO getADDBDAO();

        public static RoomDatabase.Callback mDBCallback = new RoomDatabase.Callback()
        {
            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);

                db.execSQL("DELETE FROM AdditionalSigns");

                List<AdditionalSigns> additionalSigns = getDataForAdditionalSigns();

                for(AdditionalSigns s: additionalSigns)
                {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("Id", s.Id);
                    contentValues.put("Name",  s.Name);
                    db.insert("AdditionalSigns", CONFLICT_IGNORE, contentValues);
                }

            }
        };


        private static volatile ADDB mInstance;

        static final Migration MIGRATION_1_2 = new Migration(1, 2) {
            @Override
            public void migrate(SupportSQLiteDatabase database) {
                // Since we didn't alter the table, there's nothing else to do here.
                database.execSQL("CREATE TABLE IF NOT EXISTS AdditionalSigns " +
                        "(Id INTEGER NOT NULL PRIMARY KEY," +
                        "Name TEXT)");
            }
        };

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

        private static ADDB buildDB(Context context)
        {
            return Room.databaseBuilder(context, ADDB.class, "ADDB").addCallback(mDBCallback)
                    .createFromAsset("ADDB.db").addMigrations(MIGRATION_1_2).allowMainThreadQueries().build();
        }

        public static ADDB getInstance(Context context) {
            if (mInstance == null) {
                mInstance = buildDB(context);
            }

            return mInstance;
        }

        public static void destroyInstance() {
            mInstance = null;
        }

        @Override
        public void clearAllTables() {

        }




        public static List<AdditionalSigns> getDataForAdditionalSigns() {
            List<AdditionalSigns> additionalSigns = new ArrayList<>();
            int initialId = 1;

            additionalSigns.add(new AdditionalSigns(initialId++, "Abduction of front limbs"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Abnormal behaviour"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Abnormal breathing"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Abortion"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Allergic reaction"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Alopecia Anaemia"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Anorexia"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Anuria"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Apathy"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Appetite (loss of)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Arched back and mouth opening during coughing"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Ascitis"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Ataixa"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Blind teat"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Blindness"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Blood in faeces"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Blood not clotting"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Bloody diarrhoea"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Bottle jaw"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Breathing abnormal"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Brisket oedema"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Circling"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Circular lesion on the skin"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Closure of teat"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Clouded cornea"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Congessed mucous membrane"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Constipation"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Convulsion"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Corrugation of the skin"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Coughing (owner reported)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Coughing observed"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Crackling lung sound"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Crepitation"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Criptating/crackling sound/Skin emphysem"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Crust around mouth"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Crusting around eyes and nose"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Dark red urine"));
            additionalSigns.add(new AdditionalSigns(initialId++, " Death (unexplained)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Dehydration"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Depression"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Diarrhoea"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Difficulty breathing"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Discharge of pus"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Dog bite (history)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Dry muzzle"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Dullness due to pain"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Dysentery"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Dyspnoea"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Emaciation"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Enlarged Lymph Node"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Erecting of ear and tail"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Erosion on dental pad and gum"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Erosion on tongue"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Excessive thirst"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Facial swelling"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Falling over"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Fecal output (absense of)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Fever"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Haemoglobinuria"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Hair loss"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Head pushing"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Head swelling"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Hematuria"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Hydrophobia"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Hyperemic mucous m/m"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Icterus"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Ill thrift"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Incordination of movement"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Increased rate of respiration"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Itching"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Jaundice"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Lacrimation"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Lameness"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Larval discharge"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Lesions around nasal cavity"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Lesions in mouth"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Lesions on internal part of tail"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Lichinification / Skin thickening"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Loss of appetite"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Loss of body condition"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Loss of consciousness"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Lymph Node enlargement"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Mouth lesions"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Muscle tremors"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Nasal discharge"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Nasal discharge (bilateral)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Nasal discharge (mucoprullent)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Nasal discharge (serous)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Nasal lesions"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Neck bending / head to flank"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Nodular lesions"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Nodular lesions (non-painful)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Nodular lesions (painful)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Occular discharge"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Occular discharge / Involunatary closure of eyelid"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Occular discharge / Involunatary closure of eyelid"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Pale membranes"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Pallor"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Parasites in feaces"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Paresis / paralysis"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Pedalling type leg movement"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Perineal lesion"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Petechial haemorrhages"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Photophobia"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Pica"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Pot belly"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Pruritis"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Pus discharge"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Pyrexia"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Recumbency"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Red urine"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Retained placenta"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Rough coat"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Salivation"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Scab / ulcers around mouth"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Scab formation on the skin"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Shivering"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Skin lesion / Scabs around oronasal area / muzzel"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Skin nodules"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Skin nodules (circular lessions) (painful)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Sneezing"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Standing hair"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Stamping gait"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Staring coat"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Stiffness of ears, tail, nostrils"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Still birth"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Stretching to urinate"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Stunted growth"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Submandibular oedema"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Sudden death"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Swelling / Hardness of udder and teats"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Swelling of neck"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Swelling of submandibular gland"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Swelling of ventral area"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Swollen front limbs"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Teat lesions"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Thrist (excessive)"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Trembling"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Ulcer on tongue"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Ulceration of lymphatic vessel"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Ulceration on medial canthus"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Ulcers below fetlock"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Vaginal discharge"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Ventral oedema"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Weakness"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Weight loss"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Wound"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Wound in buccal cavity"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Yellow creamy discharge from nodules"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Yellowing of membranes"));
            additionalSigns.add(new AdditionalSigns(initialId++, "Yellowish, flakey discharge"));

            return  additionalSigns;
        }



    }
