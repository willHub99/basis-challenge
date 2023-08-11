package com.example.basischallenge.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basischallenge.database.model.Address
import com.example.basischallenge.database.model.User
import com.example.basischallenge.repository.RealmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateUserViewModel(
    private val repository: RealmRepository = RealmRepository()
): ViewModel() {

    var addressList: MutableList<Address> = mutableListOf()

    fun createUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertUser(user)
    }

}