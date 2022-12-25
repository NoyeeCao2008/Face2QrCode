package com.github.noyeecao2008.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Face2QrEntity.class}, version = 1)
public abstract class Face2QrDatabase extends RoomDatabase {
    public abstract Face2QrDao face2QrDao();
}
