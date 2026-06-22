package com.example.shoeshop.utils

import android.content.Context

object PrefManager {

    private const val PREF_NAME = "shoe_store_pref"
    private const val KEY_UID = "uid"

    fun saveUid(context: Context, uid: String) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putString(KEY_UID, uid).apply()
    }

    fun getUid(context: Context): String? {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_UID, null)
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply()
    }
}