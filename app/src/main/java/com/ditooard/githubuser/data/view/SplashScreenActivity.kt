package com.ditooard.githubuser.data.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.ditooard.githubuser.R
import com.ditooard.githubuser.data.local.SettingPreferences
import com.ditooard.githubuser.data.viewmodel.SplashscreenViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val Context.dataStore by preferencesDataStore("user_preferences")

    private lateinit var viewModels: SplashscreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        val settingPreferences = SettingPreferences.getInstance(application.dataStore)

        viewModels = ViewModelProvider(
            this,
            SplashscreenViewModel.Factory(settingPreferences)
        ).get(SplashscreenViewModel::class.java)

        viewModels.getTheme().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val icon: ImageView = findViewById(R.id.icon)
        icon.alpha = 0f
        icon.animate().setDuration(1500).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}