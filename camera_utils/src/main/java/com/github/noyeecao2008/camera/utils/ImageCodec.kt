package com.github.noyeecao2008.camera.utils

import android.graphics.Bitmap
import android.text.TextUtils
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.lang.Math.min
import kotlin.math.max

object ImageCodec {

    fun bitmapToJpeg60ByteArray(bitmap: Bitmap): ByteArray? {
        if (bitmap == null) {
            return null;
        }
        var baos: ByteArrayOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
            baos.flush()
            baos.close()
            return baos.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            return null;
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * bmp 转 base64
     *
     * @param bitmap
     * @return
     */
    fun bitmapToBase64(bitmap: Bitmap?): String? {
        var result: String? = null
        if (bitmap != null) {
            val bitmapBytes = bitmapToJpeg60ByteArray(bitmap)
            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
        }
        return result
    }

    /**
     * base64转bmp
     *
     * @return
     */
    fun base64ToBitmap(base64Img: String): Bitmap? {
        // 若包含"data:image/jpeg;base64,"前缀，只取后边的部分
        var base64Img = base64Img
        if (TextUtils.isEmpty(base64Img)) {
            return null
        }
        //
        if (base64Img.contains(",")) {
            val base64ImgArray = base64Img.split(",").toTypedArray()
            if (base64ImgArray.size == 2) {
                base64Img = base64ImgArray[1]
            }
        }
        // 解码
        try {
            val decodedString =
                Base64.decode(base64Img, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } catch (e: Exception) {
            e.printStackTrace()
            // 回调生成图片失败
        }
        return null
    }


    /** Utility function used to read input file into a byte array */
    fun loadThumbJpegBuffer(filePath: String, matrix: Matrix): ByteArray? {
        var bitmapOptions = createScaleBitmapOption();
        BitmapFactory.decodeFile(filePath, bitmapOptions)
        var bitmap: Bitmap =
            BitmapFactory.decodeFile(filePath, updateDecodeBitmapOption(bitmapOptions))
        var size = min(bitmap.width, bitmap.height)
        var fixBitmap = Bitmap.createBitmap(
            bitmap, 0, 0, size, size, matrix, true
        )
        return bitmapToJpeg60ByteArray(fixBitmap)
    }

    private val TAG: String = "ImageCodec"

    /** Maximum size of [Bitmap] decoded */
    private const val DOWNSAMPLE_SIZE: Int = 640  // 1MP


    private fun createScaleBitmapOption(): BitmapFactory.Options {
        return BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
    }

    private fun updateDecodeBitmapOption(bitmapOption: BitmapFactory.Options): BitmapFactory.Options {
        return bitmapOption.apply {
            inJustDecodeBounds = false
            // Keep Bitmaps at less than 1 MP
            if (max(outHeight, outWidth) > DOWNSAMPLE_SIZE) {
                val scaleFactorX = outWidth / DOWNSAMPLE_SIZE + 1
                val scaleFactorY = outHeight / DOWNSAMPLE_SIZE + 1
                inSampleSize = max(scaleFactorX, scaleFactorY)
            }
            Log.e(
                TAG, "inSampleSize:" + inSampleSize
                        + "; outHeight:" + outHeight
                        + "; outWidth:" + outWidth
            );
        }
    }

}