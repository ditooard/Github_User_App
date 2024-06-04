package com.ditooard.githubuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteUser::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    companion object {
        private var INS: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase? {
            if (INS == null) {
                synchronized(UserDatabase::class) {
                    INS = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "db_user"
                    ).build()
                }
            }
            return INS
        }
    }

    abstract fun favUserDao(): FavoriteUserDao
}