package com.github.noyeecao2008.db;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

@RunWith(AndroidJUnit4.class)
public class Face2QrDbManTest {
    private static final String TAG = Face2QrDbManTest.class.getSimpleName();
    Face2QrDbMan mMan;

    @Before
    public void openDb() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        mMan = new Face2QrDbMan();
        mMan.openDb(appContext);
    }

    @After
    public void closeDb() {
        mMan.closeDb();
    }

    @Test
    public void testIO() {
        String key = "noyee abc";
        mMan.insert(key, "qr code abc", "avatar abc");
        CountDownLatch latchFind = new CountDownLatch(1);
        mMan.findByUserId(key, (entity, success) -> {
            Log.e(TAG, "entity = " + entity + "; success" + success);
            assertTrue(success);
            latchFind.countDown();
        });
        try {
            latchFind.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFind(){
        CountDownLatch latchFind = new CountDownLatch(1);
        mMan.findByUserId("noyee abc", (entity, success) -> {
            Log.i(TAG,"entity:"+entity);
            Log.i(TAG,"success:"+success);
            latchFind.countDown();
        });

        try {
            latchFind.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
