package com.example.basischallenge.recyclerview.addressUserList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.basischallenge.R
import com.example.basischallenge.database.model.Address
import com.example.basischallenge.recyclerview.ItemClickListener

class AddressAdapter: RecyclerView.Adapter<AddressViewHolder>() {

    private var listAddress: MutableList<Address> = mutableListOf()
    private lateinit var mListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_address_item, parent, false)
        return AddressViewHolder(view, mListener)
    }

    override fun getItemCount(): Int {
        return listAddress.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = listAddress[position]
        holder.bind(address = address)
    }

    fun setAddressList(list: MutableList<Address>) {
        this.listAddress = list
        notifyDataSetChanged()
    }

    fun setListener(listener: ItemClickListener) {
        this.mListener = listener
    }
}