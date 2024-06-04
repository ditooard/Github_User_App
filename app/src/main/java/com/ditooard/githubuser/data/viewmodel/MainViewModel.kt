package com.ditooard.githubuser.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.ditooard.githubuser.data.local.SettingPreferences
import com.ditooard.githubuser.data.model.User
import com.ditooard.githubuser.data.model.UserResponse
import com.ditooard.githubuser.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val preferences: SettingPreferences) : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<User>>()

    fun getThemeSetting() = preferences.getThemeSetting().asLiveData()


    fun setSearchUsers(query: String) {
        RetrofitClient.apiInstance.getSearchUsers(query).enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>, response: Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    listUsers.postValue(response.body()?.items)
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                t.message?.let { Log.d("Failure", it) }


            }

        })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }

    class Factory(private val preferences: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            MainViewModel(preferences) as T
    }


}


