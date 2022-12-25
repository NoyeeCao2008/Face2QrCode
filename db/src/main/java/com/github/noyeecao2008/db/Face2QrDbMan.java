package com.github.noyeecao2008.db;

import android.content.Context;
import android.telecom.Call;
import android.util.Log;

import androidx.room.Room;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.CountDownLatch;

public class Face2QrDbMan {
    private static final String TAG = Face2QrDbMan.class.getSimpleName();
    Face2QrDatabase mDb;
    Face2QrDao mFace2QrDao;

    public void openDb(Context appContext) {
        mDb = Room.databaseBuilder(appContext,
                Face2QrDatabase.class, Face2QrDao.tableName).build();
        mFace2QrDao = mDb.face2QrDao();
    }

    public void closeDb() {
        mFace2QrDao = null;
        mDb.close();
        mDb = null;
    }

    public Face2QrDatabase getDb() {
        return mDb;
    }

    public static interface Callback {
        void onFinish(Face2QrEntity entity, boolean success);
    }

    public void insert(String faceId, String qrInfo, String avatarBase64) {
        if (mFace2QrDao == null) {
            return;
        }
        Face2QrEntity f2qr = new Face2QrEntity(faceId, qrInfo, avatarBase64);
        long begin = System.currentTimeMillis();
        mFace2QrDao.upsert(f2qr);
        Log.e(TAG, "upsert time = " + (System.currentTimeMillis() - begin));

    }

    public void findByFaceId(String faceId, Face2QrDbMan.Callback callback) {
        if (mFace2QrDao == null) {
            if (callback != null) {
                callback.onFinish(null, false);
            }
            return;
        }

        ListenableFuture<Face2QrEntity> futureFindByFaceId = mFace2QrDao.findByFaceId(faceId);

        Futures.addCallback(futureFindByFaceId, new FutureCallback<Face2QrEntity>() {

            @Override
            public void onSuccess(Face2QrEntity result) {
                Log.i(TAG, "findByFaceId(" + faceId + ") = " + result);
                if (callback != null) {
                    callback.onFinish(result, true);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i(TAG, "findByFaceId failed", t);
                if (callback != null) {
                    callback.onFinish(null, false);
                }
            }
        }, command -> command.run());

    }
}
