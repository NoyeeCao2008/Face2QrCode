/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.noyeecao2008.camera

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.util.Log;
import androidx.navigation.NavArgument
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.github.noyeecao2008.camera.databinding.ActivityCameraBinding


class CameraActivity : AppCompatActivity() {
    val TAG: String = CameraActivity.javaClass.simpleName

    private lateinit var activityCameraBinding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCameraBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(activityCameraBinding.root)
        setNavigation()
    }

    fun setNavigation() {
        var camid = intent.getStringExtra(CameraConfig.CAMERA_ACTION_PARAM_CAMERA_ID)
        var msg = intent.extras?.get(CameraConfig.CAMERA_ACTION_PARAM_MSG)?.toString()
        var addnew = intent.getStringExtra(CameraConfig.CAMERA_ACTION_PARAM_ADD_NEW_AVATAR)
        Log.i(TAG, "CameraActivity camid = $camid; msg = $msg; addnew = $addnew");

//        var nav = Navigation.findNavController(this, R.id.fragment_container)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        val nav = navHostFragment.navController

        var keys = arrayListOf(
            CameraConfig.CAMERA_ACTION_PARAM_CAMERA_ID,
            CameraConfig.CAMERA_ACTION_PARAM_MSG,
            CameraConfig.CAMERA_ACTION_PARAM_ADD_NEW_AVATAR
        )

        keys.forEach() { key ->
            val value = intent.getStringExtra(key)
            nav.graph.addArgument(key, NavArgument.Builder().setDefaultValue(value).build())
        }
    }

    override fun onResume() {
        super.onResume()
        // Before setting full screen flags, we must wait a bit to let UI settle; otherwise, we may
        // be trying to set app to immersive mode before it's ready and the flags do not stick
        activityCameraBinding.fragmentContainer.postDelayed({
            activityCameraBinding.fragmentContainer.systemUiVisibility = FLAGS_FULLSCREEN
        }, IMMERSIVE_FLAG_TIMEOUT)
    }

    companion object {


        /** Combination of all flags required to put activity into immersive mode */
        const val FLAGS_FULLSCREEN =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        /** Milliseconds used for UI animations */
        const val ANIMATION_FAST_MILLIS = 50L
        const val ANIMATION_SLOW_MILLIS = 100L
        private const val IMMERSIVE_FLAG_TIMEOUT = 500L
    }

}
