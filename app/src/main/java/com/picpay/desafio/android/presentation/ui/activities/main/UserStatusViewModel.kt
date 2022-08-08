package com.picpay.desafio.android.presentation.ui.activities.main

import com.picpay.desafio.android.data.model.User

data class UserStatusViewModel(
    val progressBar: Boolean,
    val groupContent: Boolean,
    val errorView: Boolean,
    val emptyView: Boolean,
    val snackBar: Boolean,
    val users: List<User>? = null
)
