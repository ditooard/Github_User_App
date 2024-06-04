package com.ditooard.githubuser.data.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ditooard.githubuser.R
import com.ditooard.githubuser.data.adapter.SectionPagerAdapter
import com.ditooard.githubuser.data.viewmodel.DetailUserViewModel
import com.ditooard.githubuser.databinding.ActivityDetailUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val username = intent.getStringExtra(USERNAME)
        val id = intent.getIntExtra(ID, 0)
        val avatarUrl = intent.getStringExtra(AVATAR_URL)

        val bundle = Bundle()
        bundle.putString(USERNAME, username)

        showLoadingOnScreen(true)

        viewModel = ViewModelProvider(this).get(
            DetailUserViewModel::class.java
        )

        if (username != null) {
            viewModel.setUserDetailProfile(username)
        }

        viewModel.getUserDetailProfile().observe(this, {
            if (it != null) {
                Log.d("getUser", "succes")
                binding.apply {
                    usernameTv.text = it.name
                    nameTv.text = it.login
                    followersTv.text = "${it.followers} Followers"
                    followingTv.text = "${it.following} Following"
                    Glide.with(this@DetailUserActivity).load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade()).centerCrop()
                        .into(profileIv)
                }
            }
            showLoadingOnScreen(false)
        })

        var _Checked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        Log.d("isChecked", "true")
                        binding.favToggle.isChecked = true
                        _Checked = true
                    } else {
                        Log.d("isChecked", "false")
                        binding.favToggle.isChecked = false
                        _Checked = false
                    }
                }
            }
        }

        binding.favToggle.setOnClickListener {
            _Checked = !_Checked
            if (_Checked) {
                if (username != null && avatarUrl != null) {
                    Log.d("addToFavorite", "succes")
                    viewModel.addUserToFavorite(username, id, avatarUrl)
                }
                Toast.makeText(this, "User Add to your Favorite", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("removeFromFavorite", "succes")
                viewModel.removeUserFromFavorite(id)
                Toast.makeText(this, "User Removed from your Favorite", Toast.LENGTH_SHORT).show()
            }
            binding.favToggle.isChecked = _Checked
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
        }
    }


    private fun showLoadingOnScreen(state: Boolean) {
        binding.loadingBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val USERNAME = "extra_username"
        const val ID = "extra_id"
        const val AVATAR_URL = "extra_avatar_url"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}