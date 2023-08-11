package com.example.basischallenge.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basischallenge.database.model.User
import com.example.basischallenge.repository.RealmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class HomeViewModel(
    private val repository: RealmRepository = RealmRepository()
): ViewModel() {

    private var _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>> = _users

    val filter: MutableList<User> = mutableListOf<User>()

    init {
        getUsers()
    }

    fun getUsers() = viewModelScope.launch(Dispatchers.IO) {
        repository.getData().collect {
            _users.postValue(it)
        }
    }

    fun deleteUser(id: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteUser(id)
    }

    fun filterUserList(value: String): List<User>? {
        return users.value?.filter {
            it.name.contains( value, ignoreCase = false)
        }
    }
}