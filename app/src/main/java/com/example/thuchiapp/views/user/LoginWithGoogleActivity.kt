package com.example.thuchiapp.views.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.thuchiapp.R
import com.example.thuchiapp.controller.UserController
import com.example.thuchiapp.databinding.ActivityLoginWithGoogleBinding
import com.example.thuchiapp.views.splash.PendingJarsStartActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import kotlinx.coroutines.launch

class LoginWithGoogleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginWithGoogleBinding
    private val RC_SIGN_IN = 9001
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var userController: UserController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithGoogleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = this.getSharedPreferences("AppPreferences", MODE_PRIVATE)
        userController = UserController(sharedPreferences)

        configureGoogleSignIn()
        setupViews()
    }

    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(DriveScopes.DRIVE_FILE))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupViews() {
        binding.googleBtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    lifecycleScope.launch {
                        val driveService = getDriveService()
                        if (driveService != null) {
//                            DatabaseSyncHelper.downloadAndRestoreDatabase(driveService, this@LoginWithGoogleActivity)
                            userController.login(account)
                            val intent = Intent(this@LoginWithGoogleActivity, PendingJarsStartActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.e("LoginWithGoogleActivity", "Drive service is null")
                        }
                    }
                }
            } catch (e: ApiException) {
                Log.w("LoginWithGoogleActivity", "Google sign in failed", e)
            }
        }
    }

    private fun getDriveService(): Drive? {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        return account?.let {
            val credential = GoogleAccountCredential.usingOAuth2(
                this, listOf(DriveScopes.DRIVE_FILE)
            )
            credential.selectedAccount = account.account

            val transport = NetHttpTransport()

            Drive.Builder(
                transport,
                com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance(),
                credential
            ).setApplicationName(getString(R.string.app_name)).build()
        }
    }
}
