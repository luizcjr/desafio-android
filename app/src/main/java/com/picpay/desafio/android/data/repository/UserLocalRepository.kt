package com.picpay.desafio.android.data.repository

import com.picpay.desafio.android.data.local.datasource.LocalDataSource
import com.picpay.desafio.android.data.local.entity.UsersEntity
import com.picpay.desafio.android.data.model.User

interface UserLocalRepository {
    suspend fun insertUsers(user: List<User>)
    fun getUsers(): List<User>
}

class UserLocalRepositoryImpl(private val dataSource: LocalDataSource) : UserLocalRepository {

    override suspend fun insertUsers(user: List<User>) = dataSource.insertUsers(user.map { UsersEntity.fromUser(it) })

    override fun getUsers(): List<User> = dataSource.readUsers().map { it.toUser() }
}