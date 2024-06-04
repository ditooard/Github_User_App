package com.ditooard.githubuser.service

import com.ditooard.githubuser.BuildConfig
import com.ditooard.githubuser.data.model.DetailUserResponse
import com.ditooard.githubuser.data.model.User
import com.ditooard.githubuser.data.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val mySecretGithubToken = BuildConfig.GITHUB_TOKEN
const val authorized = "Authorization: token " + mySecretGithubToken

interface Api {
    @GET("search/users")
    @Headers(authorized)
    fun getSearchUsers(
        @Query("q") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers(authorized)
    fun getUserDetail(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers(authorized)
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers(authorized)
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}