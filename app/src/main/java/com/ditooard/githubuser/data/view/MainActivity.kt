package com.ditooard.githubuser.data.view


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ditooard.githubuser.R
import com.ditooard.githubuser.data.adapter.UserAdapter
import com.ditooard.githubuser.data.local.SettingPreferences
import com.ditooard.githubuser.data.model.User
import com.ditooard.githubuser.data.viewmodel.MainViewModel
import com.ditooard.githubuser.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val Context.dataStore by preferencesDataStore("user_preferences")


    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UserAdapter


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val settingPreferences = SettingPreferences.getInstance(application.dataStore)

        viewModel = ViewModelProvider(
            this,
            MainViewModel.Factory(settingPreferences)
        ).get(MainViewModel::class.java)

        viewModel.getThemeSetting().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.btnSearch.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.tint_color_light
                    ), PorterDuff.Mode.SRC_IN
                )
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.btnSearch.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.tint_color_dark
                    ), PorterDuff.Mode.SRC_IN
                )
            }
        }

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserAdapter.onItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.USERNAME, data.login)
                    it.putExtra(DetailUserActivity.ID, data.id)
                    it.putExtra(DetailUserActivity.AVATAR_URL, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            btnSearch.setOnClickListener {
                Log.d("MenuClick", "Clicked")
                searchUser()
            }

            etQuery.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.setSearchUsers("dito")

        viewModel.getSearchUsers().observe(this, {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
                showEmpty(it.isEmpty())
            } else {
                showEmpty(true)
            }
        })

        val btnFavorite: ImageButton = findViewById(R.id.btn_favorite)
        btnFavorite.setOnClickListener {
            Log.d("ImageButtonClick", "Favorite Button Clicked")
            startActivity(Intent(this, FavoriteActivity::class.java))
        }
    }

    private fun searchUser() {
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUsers(query)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
            binding.layoutEmpty.root.visibility = View.GONE
            binding.rvUser.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showEmpty(state: Boolean) {
        if (state) {
            binding.layoutEmpty.root.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            binding.rvUser.visibility = View.GONE
        } else {
            binding.layoutEmpty.root.visibility = View.GONE
            binding.rvUser.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.getSearchUsers().value == null) {
            showEmpty(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            when (item.itemId) {
                R.id.setting_menu -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    Log.d("SettingActivity", "Setting menu item clicked")
                    return true
                }
            }
        } catch (e: Exception) {
            Log.e("SettingActivity", "Error while handling option item selection", e)
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}

