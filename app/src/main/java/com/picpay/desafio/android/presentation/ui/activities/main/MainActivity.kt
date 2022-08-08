package com.picpay.desafio.android.presentation.ui.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.picpay.desafio.android.R
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
        mViewModel.fetchUsers()
        observeData()
    }

    private fun observeData() {
        mViewModel.users.observe(this, {
            mViewModel.present(it, this)
        })
        mViewModel.usersStatus.observe(this, {
            binding.userListProgressBar.isVisible = it.progressBar
            binding.groupContent.isVisible = it.groupContent
            binding.includeLayoutError.errorView.isVisible = it.errorView
            binding.includeLayoutEmpty.emptyView.isVisible = it.emptyView
            if (it.snackBar) showSnackBar()
            it.users?.let { users ->
                mAdapter.submitList(users)
            }
            if (it.errorView) {
                binding.includeLayoutError.btnRetry.setOnClickListener {
                    fetchData()
                }
            }
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
}
