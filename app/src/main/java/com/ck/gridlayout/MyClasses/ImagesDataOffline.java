package com.ck.gridlayout.MyClasses;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ImagesDataOffline {

    @PrimaryKey(autoGenerate = true)
    public int Id;

    @ColumnInfo(name = "previewImg")
    public String previewImageLocal;

    @ColumnInfo(name = "OriginalImg")
    public String OriginalImageLocal;

}
