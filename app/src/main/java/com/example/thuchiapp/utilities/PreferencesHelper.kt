package com.example.thuchiapp.utilities

import android.content.Context
import android.content.SharedPreferences

object PreferencesHelper {

    private const val PREFERENCES_NAME = "AppPreferences"
    private const val TOTAL_KEY = "totalAmount"

    // Hàm lưu giá trị total vào SharedPreferences
    fun saveTotalToPreferences(context: Context, total: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(TOTAL_KEY, total)
        editor.apply()
    }

    // Hàm lấy giá trị total từ SharedPreferences
    fun getTotalFromPreferences(context: Context): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(TOTAL_KEY, "0") ?: "0"
    }
}