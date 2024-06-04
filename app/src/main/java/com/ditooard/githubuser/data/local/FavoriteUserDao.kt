package com.ditooard.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavoriteUserDao {
    @Insert
    fun addUserToFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM user_fav")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM user_fav WHERE user_fav.id = :id")
    fun checkingUser(id: Int): Int

    @Query("DELETE FROM user_fav WHERE user_fav.id = :id")
    fun removeUserFromFavorite(id: Int): Int
}


