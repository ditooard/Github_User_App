package com.ditooard.githubuser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize
import java.io.Serializable

@Entity(tableName = "user_fav")
@VersionedParcelize
data class FavoriteUser(
    val login: String,
    @PrimaryKey
    val id: Int,
    val avatar_url: String
) : Serializable
