package com.picpay.desafio.android.presentation.ui.activities.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.utils.RemoteException
import com.picpay.desafio.android.data.utils.ResultState
import com.picpay.desafio.android.domain.usecase.UserUseCase
import com.picpay.desafio.android.domain.usecase.UsersLocalUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val userUseCase: UserUseCase,
    private val usersLocalUseCase: UsersLocalUseCase
) : ViewModel() {

    private val _users = MutableLiveData<ResultState<List<User>>>()
    val users: LiveData<ResultState<List<User>>>
        get() = _users

    private val _usersLocal = MutableLiveData<ResultState<List<User>>>()
    val usersLocal: LiveData<ResultState<List<User>>>
        get() = _usersLocal

    fun fetchUsers() {
        _users.postValue(ResultState.Loading)
        viewModelScope.launch {
            try {
                val response = userUseCase()
                if (response.isNotEmpty()) {
                    _users.postValue(ResultState.Success(response))
                    insertUser(response)
                } else {
                    _users.postValue(ResultState.Empty)
                }
            } catch (e: Exception) {
                with(RemoteException("Could not connect to PicPay API")) {
                    _users.postValue(ResultState.Error(this))
                }
            }
        }
    }

    private fun insertUser(user: List<User>) =
        viewModelScope.launch(Dispatchers.IO) {
            usersLocalUseCase(user)
        }

    fun fetchUsersLocal() {
        _usersLocal.postValue(ResultState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = usersLocalUseCase()
                if (response.isNotEmpty()) {
                    _usersLocal.postValue(ResultState.Success(response))
                } else {
                    _usersLocal.postValue(ResultState.Empty)
                }
            } catch (e: Exception) {
                with(RemoteException("Could not connect to PicPay API")) {
                    _usersLocal.postValue(ResultState.Error(this))
                }
            }
        }
    }

    fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false

            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}