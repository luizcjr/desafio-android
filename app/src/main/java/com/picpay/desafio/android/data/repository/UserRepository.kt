package com.picpay.desafio.android.data.repository

import android.content.Context
import com.picpay.desafio.android.data.local.datasource.LocalDataSource
import com.picpay.desafio.android.data.local.entity.UsersEntity
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.service.PicPayService
import com.picpay.desafio.android.data.utils.NetworkUtils
import com.picpay.desafio.android.data.utils.RemoteException
import retrofit2.HttpException
import kotlin.jvm.Throws

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun insertUsers(user: List<User>)
}

class UserRepositoryImpl(
    private val picPayService: PicPayService,
    private val dataSource: LocalDataSource,
    private val context: Context
) : UserRepository {
    @Throws(RemoteException::class)
    override suspend fun getUsers(): List<User> {
        try {
            return if (NetworkUtils.checkForInternet(context)) {
                val result = picPayService.getUsers()
                insertUsers(result)
                result
            } else {
                dataSource.readUsers().map { it.toUser() }
            }
        } catch (ex: HttpException) {
            throw RemoteException("Could not connect to PicPay API")
        }
    }

    override suspend fun insertUsers(user: List<User>) =
        dataSource.insertUsers(user.map { UsersEntity.fromUser(it) })
}