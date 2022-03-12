package com.picpay.desafio.android.presentation.ui.activities.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.presentation.ui.utils.UserListDiffCallback

/**
 * Implementar o adapter usando o ListAdapter, faz com que tenha melhor performance
 * principalmente quando a lista possui muitos itens, pois usando o DiffUtil
 * ele calcula a diferença entre a lista antiga e a nova lista e faz o mínimo de alteração
 * na lista
 */
class UserListAdapter : ListAdapter<User, UserListItemViewHolder>(UserListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_user, parent, false)

        return UserListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserListItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}