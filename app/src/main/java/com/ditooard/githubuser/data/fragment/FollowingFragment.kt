package com.ditooard.githubuser.data.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ditooard.githubuser.R
import com.ditooard.githubuser.data.adapter.UserAdapter
import com.ditooard.githubuser.data.view.DetailUserActivity
import com.ditooard.githubuser.data.viewmodel.FollowingViewModel
import com.ditooard.githubuser.databinding.FragmentFollowBinding

class FollowingFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("No Data")

    private lateinit var username: String
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapt: UserAdapter


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowBinding.bind(view)

        val args = arguments
        username = args?.getString(DetailUserActivity.USERNAME).toString()

        adapt = UserAdapter()
        adapt.notifyDataSetChanged()

        binding.apply {
            userRv.setHasFixedSize(true)
            userRv.layoutManager = LinearLayoutManager(activity)
            userRv.adapter = adapt
        }

        showLoadingOnScreen(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowingViewModel::class.java
        )

        viewModel.setListFollowing(username)
        viewModel.getListFollowing().observe(viewLifecycleOwner, {
            if (it != null) {
                adapt.setList(it)
                showLoadingOnScreen(false)
            }
        })
    }

    private fun showLoadingOnScreen(state: Boolean) {
        if (state) {
            binding.loadingBar.visibility = View.VISIBLE
        } else {
            binding.loadingBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}