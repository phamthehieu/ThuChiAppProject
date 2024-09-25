package com.example.thuchiapp.views.splash

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.thuchiapp.MainActivity
import com.example.thuchiapp.controller.UserController
import com.example.thuchiapp.databinding.ActivitySplashBinding
import com.example.thuchiapp.views.OnboardingActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var userController: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        userController = UserController(sharedPreferences)

        configureGoogleSignIn()
        setup()
    }

    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setup() {
        val lottieAnimationView = binding.lottieAnimationView
        lottieAnimationView.setAnimation("hello.json")
        lottieAnimationView.speed = 2.0f
        lottieAnimationView.playAnimation()
        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                val account = GoogleSignIn.getLastSignedInAccount(this@SplashActivity)
//                if (account != null) {
//                    userController.login(account)
//
//                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                } else {
                    val intent = Intent(this@SplashActivity, OnboardingActivity::class.java)
                    startActivity(intent)
                    finish()
//                }
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }
}
