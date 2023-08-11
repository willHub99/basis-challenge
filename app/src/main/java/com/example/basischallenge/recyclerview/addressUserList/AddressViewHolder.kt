package com.example.basischallenge.recyclerview.addressUserList

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.basischallenge.R
import com.example.basischallenge.database.model.Address
import com.example.basischallenge.recyclerview.ItemClickListener

class AddressViewHolder(itemView: View, listener: ItemClickListener): RecyclerView.ViewHolder(itemView) {
    private val ivTypeUser: ImageView = itemView.findViewById(R.id.ivTypeUser)
    private val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
    val btnDelete: ImageButton = itemView.findViewById(R.id.ibDeleteAddress)

    init {
        btnDelete.setOnClickListener {
            listener.onItemClick(bindingAdapterPosition)
        }
    }

    fun bind(address: Address) {

        val addressMessage =  "${address.address} - ${address.city}\n\n ${address.uf} / ${address.cep}"
        tvAddress.text = addressMessage
        val backgroundDrawable = if (address.typeAddress == "residencial") R.drawable.home else R.drawable.building
        ivTypeUser.setImageResource(backgroundDrawable)
    }
}