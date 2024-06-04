package com.ditooard.githubuser.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.ditooard.githubuser.data.local.SettingPreferences

class SplashscreenViewModel(private val preferences: SettingPreferences) : ViewModel() {

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    class Factory(private val preferences: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            SplashscreenViewModel(preferences) as T
    }
}