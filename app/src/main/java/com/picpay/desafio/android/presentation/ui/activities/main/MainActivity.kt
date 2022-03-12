package com.picpay.desafio.android.presentation.ui.activities.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.picpay.desafio.android.R
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.utils.ResultState
import com.picpay.desafio.android.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by viewModel()
    private lateinit var mAdapter: UserListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initializeRecyclerView()
        fetchData()
    }

    private fun initializeRecyclerView() {
        mAdapter = UserListAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }
    }

    private fun fetchData() {
        if (mViewModel.checkForInternet(this)) {
            mViewModel.fetchUsers()
            observeRemoteData()
        } else {
            mViewModel.fetchUsersLocal()
            observeLocalData()
        }
    }

    private fun observeRemoteData() {
        mViewModel.users.observe(this, {
            statesView(it)
        })
    }

    private fun observeLocalData() {
        showSnackBar()
        mViewModel.usersLocal.observe(this, { userLocal ->
            statesView(userLocal)
        })
    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            getString(R.string.snack_bar_message),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Ok") {}
            .setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            .show()
    }

    private fun statesView(state: ResultState<List<User>>) {
        when (state) {
            ResultState.Loading -> {
                setComponentVisibility(
                    progressBar = true,
                    group = false,
                    errorView = false,
                    emptyView = false
                )
            }
            is ResultState.Error -> {
                setComponentVisibility(
                    progressBar = false,
                    group = false,
                    errorView = true,
                    emptyView = false
                )

                binding.includeLayoutError.btnRetry.setOnClickListener {
                    fetchData()
                }
            }
            is ResultState.Success -> {
                setComponentVisibility(
                    progressBar = false,
                    group = true,
                    errorView = false,
                    emptyView = false
                )

                mAdapter.submitList(state.result)
            }
            is ResultState.Empty -> {
                setComponentVisibility(
                    progressBar = false,
                    group = false,
                    errorView = false,
                    emptyView = true
                )
            }
        }
    }

    private fun setComponentVisibility(
        progressBar: Boolean,
        group: Boolean,
        errorView: Boolean,
        emptyView: Boolean
    ) {
        binding.userListProgressBar.isVisible = progressBar
        binding.groupContent.isVisible = group
        binding.includeLayoutError.errorView.isVisible = errorView
        binding.includeLayoutEmpty.emptyView.isVisible = emptyView
    }
}
