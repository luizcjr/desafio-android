package com.picpay.desafio.android.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.picpay.desafio.android.data.local.dao.UsersDao
import com.picpay.desafio.android.data.local.entity.UsersEntity
import com.picpay.desafio.android.data.utils.UserTypeConverter

@Database(entities = [UsersEntity::class], version = 1, exportSchema = false)
@TypeConverters(UserTypeConverter::class)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}