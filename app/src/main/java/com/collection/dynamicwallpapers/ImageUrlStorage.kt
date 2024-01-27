package com.collection.dynamicwallpapers

import android.content.Context

class ImageUrlStorage(private val context: Context) {

    private val FILE_NAME = "image_urls.txt"

    fun saveImageUrl(imageUrl: String) {
        val fileContents = readAllImageUrls() + imageUrl
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(fileContents.joinToString("\n").toByteArray())
        }
    }

    fun removeImageUrl(imageUrl: String) {
        val fileContents = readAllImageUrls().filterNot { it == imageUrl }
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(fileContents.joinToString("\n").toByteArray())
        }
    }

    fun loadImageUrls(): List<String> {
        return readAllImageUrls()
    }

    private fun readAllImageUrls(): List<String> {
        val file = context.getFileStreamPath(FILE_NAME)
        if (file.exists()) {
            return file.bufferedReader().useLines { it.toList() }
        }
        return emptyList()
    }
}
