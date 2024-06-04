package com.ditooard.githubuser.service

import com.ditooard.githubuser.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL_GITHUB = BuildConfig.BASE_URL

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_GITHUB)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInstance = retrofit.create(Api::class.java)
}