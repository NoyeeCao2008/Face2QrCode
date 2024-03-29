package com.github.noyeecao2008.camera

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import android.util.Log;
import com.github.noyeecao2008.camera.ImageProcessFactory.FaceInfo
import com.github.noyeecao2008.camera.utils.WhichCamera


class CameraActivityLauncher private constructor(private val avatarLauncher: ActivityResultLauncher<Map<String, String>>) {


    private val TAG: String = CameraActivityLauncher.javaClass.simpleName

    class AvatarContract() : ActivityResultContract<Map<String, String>, FaceInfo>() {

        override fun createIntent(context: Context, map: Map<String, String>): Intent {
            val intentScan = Intent(context, CameraActivity::class.java)
            intentScan.setAction(CameraConfig.CAMERA_ACTION)
            intentScan.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val cameraId: String? = map.get(CameraConfig.CAMERA_ACTION_PARAM_CAMERA_ID)
            val userId: String? = map.get(CameraConfig.CAMERA_ACTION_PARAM_USER_ID)
            val message: String? = map.get(CameraConfig.CAMERA_ACTION_PARAM_MSG)
            val addNewAvatar: String? = map.get(CameraConfig.CAMERA_ACTION_PARAM_ADD_NEW_AVATAR)

            intentScan.putExtra(CameraConfig.CAMERA_ACTION_PARAM_CAMERA_ID, cameraId)
            intentScan.putExtra(CameraConfig.CAMERA_ACTION_PARAM_USER_ID, userId)
            intentScan.putExtra(CameraConfig.CAMERA_ACTION_PARAM_MSG, message)
            intentScan.putExtra(CameraConfig.CAMERA_ACTION_PARAM_ADD_NEW_AVATAR, addNewAvatar)
            return intentScan
        }

        override fun parseResult(resultCode: Int, intent: Intent?): FaceInfo {
            var userId =
                intent?.getStringExtra(CameraConfig.CAMERA_ACTION_RESULT_FACE_ID).toString()
            var faceImg =
                intent?.getStringExtra(CameraConfig.CAMERA_ACTION_RESULT_FACE_IMG).toString()
            var msg = intent?.getStringExtra(CameraConfig.CAMERA_ACTION_RESULT_MSG).toString()
            var returnMsg = if (TextUtils.isEmpty(userId)) {
                msg
            } else {
                "userId: $userId"
            }
            Log.i("CameraResult:", "resutl msg $returnMsg");
            return FaceInfo(userId,faceImg,msg)
        }
    }

    fun launchForAdd(context: Context, userId: String) {
        launch(context, true, true, userId)
    }

    fun launchForSearch(context: Context) {
        launch(context, false, false, "")
    }

    private fun launch(context: Context, facingForward: Boolean, addNewAvatar: Boolean, userId: String) {
        val map = HashMap<String, String>();

        val cameraId = if (facingForward) {
            WhichCamera.getForwordCameraId(context)
        } else {
            WhichCamera.getBackwordCameraId(context)
        }
        map.put(CameraConfig.CAMERA_ACTION_PARAM_CAMERA_ID, cameraId);
        map.put(CameraConfig.CAMERA_ACTION_PARAM_USER_ID, userId);


        val msg = if (addNewAvatar) {
            "Register by face and QRCode."
        } else {
            "Search QRCode by face."
        }
        map.put(CameraConfig.CAMERA_ACTION_PARAM_MSG, msg);

        var addNewAvatarStr: String = if (addNewAvatar) {
            "1"
        } else {
            "0"
        }
        map.put(CameraConfig.CAMERA_ACTION_PARAM_ADD_NEW_AVATAR, addNewAvatarStr)
        if (BuildConfig.DEBUG) {
            Log.i(
                TAG,
                "launch(facingForward:$facingForward, addNewAvatar: $addNewAvatar) msg $msg, cameraId $cameraId"
            )
        }
        avatarLauncher.launch(map);
    }

    companion object {
        @JvmStatic
        fun createLauncher(
            activityResultCaller: ActivityResultCaller,
            callback: ActivityResultCallback<FaceInfo>
        ): CameraActivityLauncher {
            return CameraActivityLauncher(
                activityResultCaller.registerForActivityResult(
                    AvatarContract(), callback
                )
            )
        }
    }
}