package com.ck.gridlayout.MyClasses;

import android.content.Context;
import android.icu.text.MessagePattern;

import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ImagesDataOffline.class}, version = 1 )
public abstract class AppDatabase extends RoomDatabase {

    public abstract ImageDataDao imgDataDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, "spyneDb").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

}
