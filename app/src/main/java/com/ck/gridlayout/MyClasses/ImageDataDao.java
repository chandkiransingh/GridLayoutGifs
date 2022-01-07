package com.ck.gridlayout.MyClasses;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ImageDataDao {

    @Query("SELECT * FROM ImagesDataOffline")
    List<ImagesDataOffline> getAllImages();

    @Insert
    void insertImage(ImagesDataOffline... imgDataOffline);
}
