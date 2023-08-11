package com.example.basischallenge.repository

import android.util.Log
import com.example.basischallenge.database.model.Address
import com.example.basischallenge.database.model.User
import com.example.basischallenge.database.provider.realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.internal.RealmReference
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class RealmRepository {
    fun getData(): Flow<List<User>> {
        return realm.query<User>().asFlow().map { it.list }
    }

    fun getUser(id: String): User? {
        return realm.query<User>(query = "_id == $0", id).first().find()
    }

    fun filter(name: String): Flow<List<User>> {
        return realm.query<User>(query = "name CONTAINS[c] $0", name).asFlow().map { it.list }
    }

    suspend fun insertUser(user: User) {
        realm.write { copyToRealm(user) }
    }

    fun updateUser(userUpdate: User) {
        realm.query<User>(query = "_id == $0", userUpdate._id).first().find()?.also { user ->
            realm.writeBlocking {
                findLatest(user)?.apply {
                    type = userUpdate.type
                    name = userUpdate.name
                    document = userUpdate.document
                    phone = userUpdate.phone
                    email = userUpdate.email
                    val localIds = user.address.map {
                        it._id
                    }
                    for (address in userUpdate.address) {
                        if (!localIds.contains(address._id)) {
                            this.address.add(address)
                        }
                    }
                }
            }
        }
    }

    fun deleteUserAddress(id: String, addressId: String) {
        realm.query<User>(query = "_id == $0", id).first().find()?.also { user ->
            realm.writeBlocking {
                findLatest(user)?.apply {
                    address.removeIf { address ->
                        address._id == addressId
                    }
                }
            }
        }
    }

    suspend fun deleteUser(id: String) {
        realm.write {
            val user = query<User>(query = "_id == $0", id).first().find()
            try {
                user?.let { delete(it) }
            } catch (e: Exception) {
                Log.d("errorDeleteUser", "${e.message}")
            }
        }
    }
}