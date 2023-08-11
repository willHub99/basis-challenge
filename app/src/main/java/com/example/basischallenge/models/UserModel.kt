package com.example.basischallenge.models

import com.example.basischallenge.database.model.Address
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import org.mongodb.kbson.ObjectId


data class UserModel(
    val id: ObjectId = ObjectId.invoke(),
    val type: String = "",
    val name: String = "",
    val document: String = "",
    val phone: String = "",
    val email: String = "",
    val address: RealmList<Address> = realmListOf<Address>()
)
