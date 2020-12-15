package com.ilri.herdmanager.database.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ilri.herdmanager.database.converters.BodyConditionSectionConverter;
import com.ilri.herdmanager.database.converters.DateConverter;

import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE;

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
BirthsForProductivityEvent.class,
BodyCondition.class, BodyConditionForHealthEvent.class}, version= 20)


@TypeConverters({DateConverter.class, BodyConditionSectionConverter.class})
public abstract class HerdDatabase extends RoomDatabase
{

    HerdDao mHerdDao;

    public static HerdDatabase mInstance = null;

    public static RoomDatabase.Callback mDBCallBack= new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            //db.execSQL("DELETE FROM BodyCondition");

            Cursor cursor = db.query("SELECT * FROM BodyCondition" );
            int  count =cursor.getCount();
            if(count==0) {
                List<BodyCondition> bodyConditionList = populateBodyConditionTable();

                for (BodyCondition bc : bodyConditionList) {

                    ContentValues contentValues = new ContentValues();
                    contentValues.put("ID", bc.ID);
                    contentValues.put("description", bc.description);
                    contentValues.put("label", bc.label);
                    contentValues.put("species", bc.species);
                    contentValues.put("stage", bc.stage);
                    contentValues.put("section", bc.section.toString());

                    db.insert("BodyCondition", CONFLICT_IGNORE, contentValues);
                }
            }


        }
    };

    public static HerdDatabase getInstance(Context context)
    {
        if(mInstance == null)
        {
            mInstance = Room.databaseBuilder(context,
                    HerdDatabase.class, "herddb").allowMainThreadQueries().fallbackToDestructiveMigration().addCallback(mDBCallBack).build();
        }
        return mInstance;
    }


    private static List<BodyCondition> populateBodyConditionTable()
    {
        List<BodyCondition> bodyConditionList = new ArrayList<>();

        int initialID=1;

        //Cattle Scale
        bodyConditionList.add(new BodyCondition(initialID++,"Starving","CATTLE",1,"Bone structure of shoulder, ribs, back, hooks, and pins are sharp to the touch and easily visible. No evidence of fat deposits or muscling.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Very Thin","CATTLE",2,"No evidence of fat deposition and there is muscle loss especially in the hindquarters. The spinous processes feel sharp to the touch and are easily seen with space between them\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Thin","CATTLE",3,"Very little fat cover over the loin, back, and foreribs. The backbone is still highly visible. Processes of the spine can be identified individually by touch and may still be visible. Spaces between the processes are less pronounced. Muscle loss in hind quarter.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Boderline","CATTLE",4,"Foreribs are slightly noticeable and the 12th and 13th ribs are still very noticeable to the eye. The transverse spinous processes can be identified only by palpation (with slight pressure) and feel rounded rather than sharp. Slight muscle loss in hind quarter.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Moderate(1)","CATTLE",5,"The 12th and 13th ribs are not visible to the eye unless the animal has been shrunk. The transverse spinous processes can only be felt with firm pressure and feel rounded but are not noticeable to the eye. Spaces between the processes are not visible and are only distinguishable with firm pressure. Areas on each side of the tailhead are starting to fill.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Moderate(2)","CATTLE",6,"Ribs are fully covered and are not noticeable to the eye. Hindquarters are plump and full. Noticeable springiness over the foreribs and on each side of the tailhead. Firm pressure is now required to feel the transverse processes. Brisket has some fat.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Fleshy","CATTLE",7,"Ends of the spinous processes can only be felt with very firm pressure. Spaces between processes can barely be distinguished. Abundant fat cover on either side of the tailhead with evident patchiness. Fat in the brisket.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Fat","CATTLE",8,"Animal takes on a smooth, blocky appearance. Bone structure disappears from sight. Fat cover is thick and spongy and patchiness is likely. Brisket is full.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Very Fat","CATTLE",9,"Bone structure is not seen or easily felt. The tailhead is buried in fat. The animal’s mobility may actually be impaired by excessive fat. Square appearance.\n"));

        //Sheep goat scale
        bodyConditionList.add(new BodyCondition(initialID++,"Starving","SHEEP_GOAT",0,"Extremely emaciated and on the point of death. It is not possible to detect any muscle or fatty tissue between the skin and the bone.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Very Thin","SHEEP_GOAT",1,"The spinous process is prominent and sharp. The transverse processes are also sharp, the fingers pass easily under the ends, and it is possible to feel between each process. The eye muscle areas are shallow with no fat cover.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Thin","SHEEP_GOAT",2,"Thin 2 The spinous process feels prominent but smooth, and individual processes can be felt only as fine corrugations. The transverse process is smooth and rounded, and it is possible to pass the fingers under the ends with a little pressure. The eye muscle area is of moderate depth, but has little fat cover\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Moderate","SHEEP_GOAT",3,"The spinous process is detected only as a small elevation; it is smooth and rounded and individual bones can be felt only with pressure. The transverse process is smooth and well covered, and firm pressure is required to feel over the ends. The eye muscle area is full, and has a moderate degree of fat cover.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Fat","SHEEP_GOAT",4,"The spinous processes can just be detected with pressure as a hard line between the fat covered eye muscle area. The end of the transverse process cannot be felt. The eye muscle area is full, and has a thick covering of fat.\n"));
        bodyConditionList.add(new BodyCondition(initialID++,"Very Fat","SHEEP_GOAT",5,"The spinous process can't be detected even with firm pressure, and there is a depression between the layers of fat in the position where the spinous process would normally be felt. The transverse process cannot be detected. The eye muscle area is very full with thick fat cover. There may be large deposits of fat over the rump and tail.\n"));

        //Camel Scale
        bodyConditionList.add(new BodyCondition(initialID++, "Very Thin","CAMEL",1,"Individually Visible",BodyConditionSection.RIBS));
        bodyConditionList.add(new BodyCondition(initialID++, "Thin","CAMEL",2,"Slightly Visible",BodyConditionSection.RIBS));
        bodyConditionList.add(new BodyCondition(initialID++, "Moderate","CAMEL",3,"Not Very Visible",BodyConditionSection.RIBS));
        bodyConditionList.add(new BodyCondition(initialID++, "Fat","CAMEL",4,"Not Visible",BodyConditionSection.RIBS));

       /* bodyConditionList.add(new BodyCondition(initialID++, "Very Thin","CAMEL",1,"Very Prominent",BodyConditionSection.TUBEROSITIES_SHOULDERS_SCAPULA_SPINES_TRAVERSE_PROC_VERTEBRAE));
        bodyConditionList.add(new BodyCondition(initialID++, "Thin","CAMEL",2,"Prominent",BodyConditionSection.TUBEROSITIES_SHOULDERS_SCAPULA_SPINES_TRAVERSE_PROC_VERTEBRAE));
        bodyConditionList.add(new BodyCondition(initialID++, "Moderate","CAMEL",3,"Slightly Prominent",BodyConditionSection.TUBEROSITIES_SHOULDERS_SCAPULA_SPINES_TRAVERSE_PROC_VERTEBRAE));
        bodyConditionList.add(new BodyCondition(initialID++, "Fat","CAMEL",4,"Not Visible",BodyConditionSection.TUBEROSITIES_SHOULDERS_SCAPULA_SPINES_TRAVERSE_PROC_VERTEBRAE));

        bodyConditionList.add(new BodyCondition(initialID++, "Very Thin","CAMEL",1,"Visble",BodyConditionSection.HOLLOW_OF_FLANK));
        bodyConditionList.add(new BodyCondition(initialID++, "Thin","CAMEL",2,"Not Visible",BodyConditionSection.HOLLOW_OF_FLANK));
        bodyConditionList.add(new BodyCondition(initialID++, "Moderate","CAMEL",3,"Not Visible",BodyConditionSection.HOLLOW_OF_FLANK));
        bodyConditionList.add(new BodyCondition(initialID++, "Fat","CAMEL",4,"Not Visible",BodyConditionSection.HOLLOW_OF_FLANK));

        bodyConditionList.add(new BodyCondition(initialID++, "Very Thin","CAMEL",1,"Very Deep",BodyConditionSection.RECTO_GENITAL_REGION));
        bodyConditionList.add(new BodyCondition(initialID++, "Thin","CAMEL",2,"Deep",BodyConditionSection.RECTO_GENITAL_REGION));
        bodyConditionList.add(new BodyCondition(initialID++, "Moderate","CAMEL",3,"Slightly Deep",BodyConditionSection.RECTO_GENITAL_REGION));
        bodyConditionList.add(new BodyCondition(initialID++, "Fat","CAMEL",4,"Full of Fat",BodyConditionSection.RECTO_GENITAL_REGION));

       */ return bodyConditionList;
    }

    public abstract HerdDao getHerdDao();
}
