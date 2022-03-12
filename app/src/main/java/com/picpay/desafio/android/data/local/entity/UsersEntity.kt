package com.picpay.desafio.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.picpay.desafio.android.data.model.User

@Entity(tableName = "users_table")
data class UsersEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var user: User
) {
    companion object {
        fun fromUser(user: User) =
            UsersEntity(
                id = user.id,
                user = user
            )
    }

    fun toUser() = User(
        img = this.user.img,
        name = this.user.name,
        id = this.user.id,
        username = this.user.username
    )
}