package com.ditooard.githubuser.data.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ditooard.githubuser.data.local.FavoriteUser
import com.ditooard.githubuser.data.local.FavoriteUserDao
import com.ditooard.githubuser.data.local.UserDatabase
import com.ditooard.githubuser.data.model.DetailUserResponse
import com.ditooard.githubuser.service.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    private var daoUser: FavoriteUserDao?
    private var dbUser: UserDatabase?

    val userGitHub = MutableLiveData<DetailUserResponse>()

    init {
        dbUser = UserDatabase.getDatabase(application)

        daoUser = dbUser?.favUserDao()
    }

    fun setUserDetailProfile(username: String) {
        RetrofitClient.apiInstance.getUserDetail(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>, response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("response", "succes")
                        userGitHub.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.d("response", "failure")
                    t.message?.let { Log.d("Failure", it) }
                }
            })
    }

    fun getUserDetailProfile(): LiveData<DetailUserResponse> {
        return userGitHub
    }

    fun addUserToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                id,
                avatarUrl
            )
            daoUser?.addUserToFavorite(user)
        }
    }

    fun checkUser(id: Int) = daoUser?.checkingUser(id)

    fun removeUserFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            daoUser?.removeUserFromFavorite(id)
        }
    }

}