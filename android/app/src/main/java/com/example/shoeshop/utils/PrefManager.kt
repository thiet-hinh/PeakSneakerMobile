package com.example.shoeshop.utils

import android.content.Context
import com.example.shoeshop.model.User
import com.google.gson.Gson

object PrefManager {

    private const val PREF = "app_pref"
    private const val KEY_USER = "current_user"
    private val gson = Gson()

    fun saveUser(context: Context, user: User) {
        val userJson = gson.toJson(user)
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().putString(KEY_USER, userJson).apply()
    }

    fun getUser(context: Context): User? {
        val userJson = context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY_USER, null) ?: return null
        return gson.fromJson(userJson, User::class.java)
    }

    fun clearUser(context: Context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().remove(KEY_USER).apply()
    }
}