package com.ditooard.githubuser.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ditooard.githubuser.data.model.User
import com.ditooard.githubuser.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val listUsers = ArrayList<User>()
    private var onItemClickedCallback: onItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: onItemClickCallback) {
        this.onItemClickedCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(users: ArrayList<User>) {
        listUsers.clear()
        listUsers.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.root.setOnClickListener {
                onItemClickedCallback?.onItemClicked(user)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(user.avatar_url)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivUser)
                tvUsername.text = user.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = listUsers.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    interface onItemClickCallback {
        fun onItemClicked(data: User)
    }
}