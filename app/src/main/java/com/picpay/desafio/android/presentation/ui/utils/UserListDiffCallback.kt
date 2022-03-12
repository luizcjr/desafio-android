package com.picpay.desafio.android.presentation.ui.utils

import androidx.recyclerview.widget.DiffUtil
import com.picpay.desafio.android.data.model.User

/**
 * Refatorei para herdar de ItemCallback para não ser necessário passar as listas
 * no construtor da classe
 */
class UserListDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}