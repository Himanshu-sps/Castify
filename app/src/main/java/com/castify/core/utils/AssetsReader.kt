package com.castify.core.utils

import android.content.Context
import java.io.IOException

class AssetsReader(private val context: Context) {

    fun getJsonDataFromAsset(fileName: String, context: Context = this.context): Result<String> {
        return try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            Result.success(jsonString)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

}