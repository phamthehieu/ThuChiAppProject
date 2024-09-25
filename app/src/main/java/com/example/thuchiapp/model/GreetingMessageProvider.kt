package com.example.thuchiapp.model

import android.content.Context
import com.example.thuchiapp.R
import java.util.Calendar

class GreetingMessageProvider {
    fun getGreetingMessage(context: Context): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> context.getString(R.string.morning)
            in 12..17 -> context.getString(R.string.noon)
            in 18..21 -> context.getString(R.string.afternoon)
            else -> context.getString(R.string.night)
        }
    }
}
