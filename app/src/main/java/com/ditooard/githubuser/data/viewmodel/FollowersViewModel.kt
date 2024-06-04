package com.ditooard.githubuser.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ditooard.githubuser.data.model.User
import com.ditooard.githubuser.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {
    val followersList = MutableLiveData<ArrayList<User>>()

    fun setListFollowers(username: String) {
        RetrofitClient.apiInstance.getFollowers(username)
            .enqueue(object : Callback<ArrayList<User>> {
                override fun onResponse(
                    call: Call<ArrayList<User>>, response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        followersList.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    t.message?.let { Log.d("Failure", it) }
                }
            })
    }

    fun getListFollowers(): LiveData<ArrayList<User>> {
        return followersList
    }
}