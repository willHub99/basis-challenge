package com.example.basischallenge.recyclerview.userList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basischallenge.R
import com.example.basischallenge.database.model.User
import com.example.basischallenge.recyclerview.ItemClickListener

class UserAdapter(): RecyclerView.Adapter<UserViewHolder>() {

    private var listUsers: List<User> = emptyList()
    private lateinit var mListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_user_item,parent, false)
        return UserViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = listUsers[position]
        holder.bind(user = user)
    }

    fun setList(list: List<User>) {
        this.listUsers = list
        notifyDataSetChanged()
    }

    fun setListener(listener: ItemClickListener) {
        this.mListener = listener
    }

}