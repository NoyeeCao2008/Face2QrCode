package com.github.noyeecao2008.db;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Face2QrDatabaseTest {
    private static final String TAG = Face2QrDatabaseTest.class.getSimpleName();
    Face2QrDatabase mDb;
    Face2QrDao mFace2QrDao;

    @Before
    public void openDb() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(appContext,
                Face2QrDatabase.class).build();
        mFace2QrDao = mDb.face2QrDao();
    }

    @After
    public void closeDb() {
        mFace2QrDao = null;
        mDb.close();
        mDb = null;
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.github.noyeecao2008.db.test", appContext.getPackageName());
    }

    @Test
    public void testFaceDbInsert() {
        String key1 = "faceid_noyee";
        String key2 = "faceid_xxx";
        Face2QrEntity f2qr = new Face2QrEntity(key1, "12334", "face12334");
        long begin = System.currentTimeMillis();
        mFace2QrDao.upsert(f2qr);
        Log.e(TAG, "upsert time = " + (System.currentTimeMillis() - begin));

        ListenableFuture<Face2QrEntity> futureFindByFaceId = mFace2QrDao.findByFaceId(key1);
        CountDownLatch latchFind = new CountDownLatch(1);

        Futures.addCallback(futureFindByFaceId, new FutureCallback<Face2QrEntity>() {

            @Override
            public void onSuccess(Face2QrEntity result) {
                Log.e(TAG, "findByFaceId(" + key1 + ") = " + result);
                latchFind.countDown();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "findByFaceId failed", t);
                latchFind.countDown();
            }
        }, command -> command.run());

        try {
            latchFind.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}