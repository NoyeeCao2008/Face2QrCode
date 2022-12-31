package com.github.noyeecao2008.camera.utils

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import android.util.Log;

object WhichCamera {
    private val TAG: String = "WhichCamera"

    fun getForwordCameraId(context: Context?): String {
        return getCameraIdByFacing(context, CameraCharacteristics.LENS_FACING_FRONT);
    }

    fun getBackwordCameraId(context: Context?): String {
        return getCameraIdByFacing(context, CameraCharacteristics.LENS_FACING_BACK);
    }

    private fun getCameraIdByFacing(context: Context?, facing: Int): String {
        val cameraManager = context?.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraList = enumerateCameras(cameraManager, facing)
        Log.i(TAG, "getCameraIdByFacing (" + facing + ") = " + cameraList[0].cameraId);
        return cameraList[0].cameraId;
    }

    /** Helper class used as a data holder for each selectable camera format item */
    private data class FormatItem(val title: String, val cameraId: String, val format: Int)

    /** Helper function used to convert a lens orientation enum into a human-readable string */
    private fun lensOrientationString(value: Int) = when (value) {
        CameraCharacteristics.LENS_FACING_BACK -> "Back"
        CameraCharacteristics.LENS_FACING_FRONT -> "Front"
        CameraCharacteristics.LENS_FACING_EXTERNAL -> "External"
        else -> "Unknown"
    }

    private fun enumerateCameras(cameraManager: CameraManager, facing: Int): List<FormatItem> {
        val availableCameras: MutableList<FormatItem> = mutableListOf()

        // Get list of all compatible cameras
        val cameraIds = cameraManager.cameraIdList.filter {
            val characteristics = cameraManager.getCameraCharacteristics(it)
            val capabilities = characteristics.get(
                CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES
            )
            capabilities?.contains(
                CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE
            ) ?: false
        }


        // Iterate over the list of cameras and return all the compatible ones
        cameraIds.forEach loop@{ id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val facingThis = characteristics.get(CameraCharacteristics.LENS_FACING);
            if (facingThis != facing) {
                return@loop
            }

            val orientation = lensOrientationString(facing!!)

            // Query the available capabilities and output formats
            val capabilities = characteristics.get(
                CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES
            )!!
            val outputFormats = characteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
            )!!.outputFormats

            // All cameras *must* support JPEG output so we don't need to check characteristics
            availableCameras.add(
                FormatItem(
                    "$orientation JPEG ($id)", id, ImageFormat.JPEG
                )
            )

            // Return cameras that support JPEG DEPTH capability
            if (capabilities.contains(
                    CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES_DEPTH_OUTPUT
                ) &&
                outputFormats.contains(ImageFormat.DEPTH_JPEG)
            ) {
                availableCameras.add(
                    FormatItem(
                        "$orientation DEPTH ($id)", id, ImageFormat.DEPTH_JPEG
                    )
                )
            }
        }

        return availableCameras
    }

}