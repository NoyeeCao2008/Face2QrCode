package com.github.noyeecao2008.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.checkerframework.common.aliasing.qual.Unique;

@Entity(tableName = "f2qr")
public class Face2QrEntity {

    public Face2QrEntity(String userId, String qrInfo, String avatar) {
        this.userId = userId;
        this.qrInfo = qrInfo;
        this.avatar = avatar;
    }

    private Face2QrEntity() {
    }

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    @NonNull
    public String userId;

    @ColumnInfo(name = "qr_info")
    public String qrInfo;

    @ColumnInfo(name = "avatar")
    public String avatar;

    @Override
    public String toString() {
        return "Face2QrEntity{" +
                "userId='" + userId + '\'' +
                ", qrInfo='" + qrInfo + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
