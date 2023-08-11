package com.example.basischallenge.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basischallenge.database.model.Address
import com.example.basischallenge.database.model.User
import com.example.basischallenge.repository.RealmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditUserViewModel(
    private val repository: RealmRepository = RealmRepository()
) : ViewModel() {

    var listAddress: MutableList<Address> = mutableListOf<Address>()

    private var _user: MutableLiveData<User> = MutableLiveData()
    var user: LiveData<User> = _user

    fun getUser(id: String) = viewModelScope.launch(Dispatchers.IO) {
        val user = repository.getUser(id)
        user?.let {
            _user.postValue(it)
            listAddress.addAll(it.address)
        }
    }

    fun updateUser(userUpdate: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateUser(userUpdate)
    }

    fun deleteUserAddress(id: String, addressId: String, position: Int) {
        repository.deleteUserAddress(id = id, addressId = addressId)
        listAddress.removeAt(position)
    }

}