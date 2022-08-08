package com.picpay.desafio.android.data.local.datasource

import com.picpay.desafio.android.data.local.dao.UsersDao
import com.picpay.desafio.android.data.local.entity.UsersEntity

class LocalDataSource(private val dao: UsersDao) {

    suspend fun insertUsers(usersEntity: List<UsersEntity>) = dao.insertUsers(usersEntity)
    suspend fun readUsers() = dao.readUsers()
}