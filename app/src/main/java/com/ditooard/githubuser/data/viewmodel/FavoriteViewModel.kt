package com.ditooard.githubuser.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ditooard.githubuser.data.local.FavoriteUser
import com.ditooard.githubuser.data.local.FavoriteUserDao
import com.ditooard.githubuser.data.local.UserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private var daoUser: FavoriteUserDao?
    private var dbUser: UserDatabase?

    init {
        dbUser = UserDatabase.getDatabase(application)
        daoUser = dbUser?.favUserDao()
    }

    fun getUserFav(): LiveData<List<FavoriteUser>>? {
        return daoUser?.getFavoriteUser()
    }

}