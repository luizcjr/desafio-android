package com.picpay.desafio.android.presentation.ui.activities.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.utils.NetworkUtils
import com.picpay.desafio.android.data.utils.RemoteException
import com.picpay.desafio.android.data.utils.ResultState
import com.picpay.desafio.android.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _users = MutableLiveData<ResultState<List<User>>>()
    val users: LiveData<ResultState<List<User>>>
        get() = _users

    private val _usersStatus = MutableLiveData<UserStatusViewModel>()
    val usersStatus: LiveData<UserStatusViewModel>
        get() = _usersStatus

    fun fetchUsers() {
        _users.postValue(ResultState.Loading)
        viewModelScope.launch {
            try {
                val response = userUseCase()
                if (response.isNotEmpty()) {
                    _users.postValue(ResultState.Success(response))
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

    fun present(state: ResultState<List<User>>, context: Context) {
        when (state) {
            ResultState.Loading -> {
                _usersStatus.postValue(
                    UserStatusViewModel(
                        progressBar = true,
                        groupContent = false,
                        errorView = false,
                        emptyView = false,
                        snackBar = false
                    )
                )
            }
            is ResultState.Error -> {
                _usersStatus.postValue(
                    UserStatusViewModel(
                        progressBar = false,
                        groupContent = false,
                        errorView = true,
                        emptyView = false,
                        snackBar = false
                    )
                )
            }
            is ResultState.Success -> {
                _usersStatus.postValue(
                    UserStatusViewModel(
                        progressBar = false,
                        groupContent = true,
                        errorView = false,
                        emptyView = false,
                        snackBar = !NetworkUtils.checkForInternet(context),
                        users = state.result
                    )
                )
            }
            else -> _usersStatus.postValue(
                UserStatusViewModel(
                    progressBar = false,
                    groupContent = false,
                    errorView = false,
                    emptyView = true,
                    snackBar = false
                )
            )
        }
    }

}