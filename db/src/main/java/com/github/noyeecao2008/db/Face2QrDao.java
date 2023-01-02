package com.github.noyeecao2008.db;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Upsert;

import com.google.common.util.concurrent.ListenableFuture;

@Dao
public interface Face2QrDao {
    String tableName = "f2qr";

    @Query("SELECT * FROM f2qr WHERE user_id = :userId LIMIT 1")
    ListenableFuture<Face2QrEntity> findByUserId(String userId);

    @Upsert
    void upsert(Face2QrEntity f2qr);
}
