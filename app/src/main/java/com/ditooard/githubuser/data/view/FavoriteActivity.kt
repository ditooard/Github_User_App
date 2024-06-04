package com.ditooard.githubuser.data.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ditooard.githubuser.data.adapter.UserAdapter
import com.ditooard.githubuser.data.local.FavoriteUser
import com.ditooard.githubuser.data.model.User
import com.ditooard.githubuser.data.viewmodel.FavoriteViewModel
import com.ditooard.githubuser.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FavoriteViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback(object : UserAdapter.onItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@FavoriteActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.USERNAME, data.login)
                    it.putExtra(DetailUserActivity.ID, data.id)
                    it.putExtra(DetailUserActivity.AVATAR_URL, data.avatar_url)
                    startActivity(it)
                }
            }
        })

        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        viewModel.getUserFav()?.observe(this, { favoriteUsers ->
            if (favoriteUsers.isNullOrEmpty()) {
                // Menampilkan layout kosong jika daftar favorit kosong
                binding.rvUser.visibility = View.GONE
                binding.layoutEmpty.root.visibility = View.VISIBLE
            } else {
                // Menampilkan daftar favorit jika ada data
                val userList = mapList(favoriteUsers)
                adapter.setList(userList)

                binding.rvUser.visibility = View.VISIBLE
                binding.layoutEmpty.root.visibility = View.GONE
            }
        })

    }

    private fun mapList(users: List<FavoriteUser>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (user in users) {
            val userMapped = User(
                user.login,
                user.id,
                user.avatar_url
            )
            listUsers.add(userMapped)
        }
        return listUsers
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
