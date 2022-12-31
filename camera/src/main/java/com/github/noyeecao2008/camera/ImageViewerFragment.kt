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

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.util.Base64
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.android.camera.utils.GenericListAdapter
import com.example.android.camera.utils.decodeExifOrientation
import com.github.noyeecao2008.camera.utils.ImageCodec
import com.github.noyeecao2008.util.ThreadPool
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ImageViewerFragment : Fragment() {

    /** AndroidX navigation arguments */
    private val args: ImageViewerFragmentArgs by navArgs()

    private var imageBase64: String = ""

    /** Bitmap transformation derived from passed arguments */
    private val bitmapTransformation: Matrix by lazy { decodeExifOrientation(args.orientation) }

    /** Flag indicating that there is depth data available for this image */
    private val isDepth: Boolean by lazy { args.depth }

    /** Data backing our Bitmap viewpager */
    private val bitmapList: MutableList<Bitmap> = mutableListOf()

    private fun imageViewFactory() = ImageView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ViewPager2(requireContext()).apply {
        // Populate the ViewPager and implement a cache of two media items
        offscreenPageLimit = 2
        adapter = GenericListAdapter(
            bitmapList,
            itemViewFactory = { imageViewFactory() }) { view, item, _ ->
            view as ImageView
            Glide.with(view).load(item).into(view)
            view.setOnClickListener {
                if (TextUtils.isEmpty(imageBase64)) {
                    finishAndCanceled("no image")
                } else {
                    ThreadPool.getInstance().postToBackground {
                        var faceId = ImageProcessFactory.getProcessor()
                            .base64ToFaceId(imageBase64, isAddFace())
                        ThreadPool.getInstance().postToMainThread() {
                            if (TextUtils.isEmpty(faceId)) {
                                finishAndCanceled(getString(R.string.not_recognize_face))
                            } else {
                                finishAndSendResult(faceId)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isAddFace(): Boolean {
        var isAdd =
            findNavController().graph.arguments.get(CameraConfig.CAMERA_ACTION_PARAM_ADD_NEW_AVATAR)
        return TextUtils.equals(isAdd.toString(), "1")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view as ViewPager2
        lifecycleScope.launch(Dispatchers.IO) {

            // Load input image file
            val inputBuffer = ImageCodec.loadThumbJpegBuffer(args.filePath, 256)

            imageBase64 = Base64.encodeToString(inputBuffer, Base64.DEFAULT);

            // TODO post to baidu ai
            Log.e(TAG, "inputBuffer.size = " + inputBuffer?.size);
            Log.e(TAG, "imageBase64.size = " + imageBase64.length);

            if (inputBuffer == null) {
                return@launch
            }
            // Load the main JPEG image
            addItemToViewPager(view, decodeBitmap(inputBuffer, 0, inputBuffer.size))

            // If we have depth data attached, attempt to load it
            if (isDepth) {
                try {
                    val depthStart = findNextJpegEndMarker(inputBuffer, 2)
                    addItemToViewPager(
                        view, decodeBitmap(
                            inputBuffer, depthStart, inputBuffer.size - depthStart
                        )
                    )

                    val confidenceStart = findNextJpegEndMarker(inputBuffer, depthStart)
                    addItemToViewPager(
                        view, decodeBitmap(
                            inputBuffer, confidenceStart, inputBuffer.size - confidenceStart
                        )
                    )

                } catch (exc: RuntimeException) {
                    Log.e(TAG, "Invalid start marker for depth or confidence data")
                }
            }
        }
    }

    /** Utility function used to add an item to the viewpager and notify it, in the main thread */
    private fun addItemToViewPager(view: ViewPager2, item: Bitmap) = view.post {
        bitmapList.add(item)
        view.adapter!!.notifyDataSetChanged()
    }

    /** Utility function used to decode a [Bitmap] from a byte array */
    private fun decodeBitmap(buffer: ByteArray, start: Int, length: Int): Bitmap {

        // Load bitmap from given buffer
        val bitmap = BitmapFactory.decodeByteArray(buffer, start, length)

        // Transform bitmap orientation using provided metadata
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, bitmapTransformation, true
        )
    }

    companion object {
        private val TAG = ImageViewerFragment::class.java.simpleName


        /** These are the magic numbers used to separate the different JPG data chunks */
        private val JPEG_DELIMITER_BYTES = arrayOf(-1, -39)

        /**
         * Utility function used to find the markers indicating separation between JPEG data chunks
         */
        private fun findNextJpegEndMarker(jpegBuffer: ByteArray, start: Int): Int {

            // Sanitize input arguments
            assert(start >= 0) { "Invalid start marker: $start" }
            assert(jpegBuffer.size > start) {
                "Buffer size (${jpegBuffer.size}) smaller than start marker ($start)"
            }

            // Perform a linear search until the delimiter is found
            for (i in start until jpegBuffer.size - 1) {
                if (jpegBuffer[i].toInt() == JPEG_DELIMITER_BYTES[0] &&
                    jpegBuffer[i + 1].toInt() == JPEG_DELIMITER_BYTES[1]
                ) {
                    return i + 2
                }
            }

            // If we reach this, it means that no marker was found
            throw RuntimeException("Separator marker not found in buffer (${jpegBuffer.size})")
        }
    }


    private fun finishAndSendResult(faceId: String) {
        if (activity?.isFinishing == true) {
            return
        }
        val result = Intent()
        result.putExtra(CameraConfig.CAMERA_ACTION_RESULT_FACE_ID, faceId)
        activity?.setResult(AppCompatActivity.RESULT_OK, result)
        activity?.finish()
    }

    private fun finishAndCanceled(message: String) {
        val result = Intent()
        result.putExtra(CameraConfig.CAMERA_ACTION_RESULT_MSG, message)
        activity?.setResult(AppCompatActivity.RESULT_CANCELED, result)
        activity?.finish()
    }
}
