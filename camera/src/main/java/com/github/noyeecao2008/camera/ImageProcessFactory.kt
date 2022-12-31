package com.github.noyeecao2008.camera

object ImageProcessFactory {
    interface ImageProcesor {
        fun base64ToFaceId(imageBase64: String, addFace: Boolean): String
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