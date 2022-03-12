package com.picpay.desafio.android.data.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.picpay.desafio.android.data.model.User

class UserTypeConverter {
    @TypeConverter
    fun userToString(user: User): String {
        return Gson().toJson(user)
    }

    @TypeConverter
    fun stringToUser(data: String): User {
        val listType = object : TypeToken<User>() {}.type
        return Gson().fromJson(data, listType)
    }
}