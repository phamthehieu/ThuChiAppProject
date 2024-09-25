package com.example.thuchiapp.controller

import android.content.SharedPreferences
import com.example.thuchiapp.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class UserController(private val sharedPreferences: SharedPreferences) {
    private val user = User()

    fun login(account: GoogleSignInAccount) {
        user.fullName = account.displayName
        user.profileImageUrl = account.photoUrl?.toString()
        sharedPreferences.edit()
            .putString("FULL_NAME", user.fullName)
            .putString("PROFILE_IMAGE_URL", user.profileImageUrl)
            .apply()
    }

    fun getFullName(): String? {
        return user.fullName ?: sharedPreferences.getString("FULL_NAME", null)
    }

    fun getProfileImageUrl(): String? {
        return user.profileImageUrl ?: sharedPreferences.getString("PROFILE_IMAGE_URL", null)
    }
}
