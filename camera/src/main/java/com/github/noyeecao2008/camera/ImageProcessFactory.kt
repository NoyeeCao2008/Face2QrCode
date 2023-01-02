package com.github.noyeecao2008.camera

object ImageProcessFactory {
    data class FaceInfo(
        val userId: String,
        val faceImgB64: String,
        val msg: String?
    ) {
    }

    interface ImageProcesor {
        fun addUserId(imageBase64: String, userId: String): FaceInfo?
        fun searchUserId(imageBase64: String): FaceInfo?
    }

    private lateinit var sProcessor: ImageProcesor

    fun getProcessor(): ImageProcesor {
        return sProcessor
    }

    @JvmStatic
    fun init(processor: ImageProcesor) {
        sProcessor = processor
    }
}