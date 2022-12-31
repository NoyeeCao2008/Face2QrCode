package com.github.noyeecao2008.camera.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import android.util.Log;

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WhichCameraTest {

    private val TAG: String? = WhichCamera.javaClass.simpleName

    @Test
    fun getForwordCameraId() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        var id = WhichCamera.getForwordCameraId(appContext)
        Log.i(TAG, "getForwordCameraId = " + id)
    }

    @Test
    fun getBackwordCameraId() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        var id = WhichCamera.getBackwordCameraId(appContext)
        Log.i(TAG, "getBackwordCameraId = " + id)
    }
}