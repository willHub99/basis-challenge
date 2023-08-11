package com.example.basischallenge.recyclerview.userList

import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basischallenge.R
import com.example.basischallenge.activities.EditUser
import com.example.basischallenge.database.model.User
import com.example.basischallenge.recyclerview.ItemClickListener

class UserViewHolder(itemView: View, listener: ItemClickListener): RecyclerView.ViewHolder(itemView){
    private val tvName: TextView = itemView.findViewById(R.id.tvNameUser)
    private val tvDocument: TextView = itemView.findViewById(R.id.tvDocumentUser)
    private val tvPhone: TextView = itemView.findViewById(R.id.tvPhoneUser)
    private val tvEmail: TextView = itemView.findViewById(R.id.tvEmailUser)
    private val ibDelete: ImageButton = itemView.findViewById(R.id.ibDeleteUser)
    private val ibEdit: ImageButton = itemView.findViewById(R.id.ibEditUser)


    init {
        ibDelete.setOnClickListener{
            listener.onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(user: User) {
        tvName.text = user.name
        tvDocument.text = user.document
        tvPhone.text = user.phone
        tvEmail.text = user.email

        ibEdit.setOnClickListener {
           Intent(itemView.context, EditUser::class.java).also {
               it.putExtra("id", user._id)
               itemView.context.startActivity(it)
           }
        }
    }

}