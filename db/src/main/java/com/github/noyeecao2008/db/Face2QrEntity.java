package com.github.noyeecao2008.db;

import androidx.annotation.ColorInt;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Fts4 //通过全文搜索 (FTS) 快速访问数据库信息
@Entity(tableName = "f2qr")
public class Face2QrEntity {

    public Face2QrEntity(String faceId, String qrInfo, String avatar) {
        this.faceId = faceId;
        this.qrInfo = qrInfo;
        this.avatar = avatar;
    }

    private Face2QrEntity() {

    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    @Ignore
    public int rowid = 0;

    @ColumnInfo(name = "face_id")
    public String faceId;

    @ColumnInfo(name = "qr_info")
    public String qrInfo;

    @ColumnInfo(name = "avatar")
    public String avatar;

    @Override
    public String toString() {
        return "Face2QrEntity{" +
                "rowid=" + rowid +
                ", faceId='" + faceId + '\'' +
                ", qrInfo='" + qrInfo + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
