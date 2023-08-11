package com.example.basischallenge.database.provider

import com.example.basischallenge.database.model.Address
import com.example.basischallenge.database.model.User
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

val config = RealmConfiguration.Builder(
    schema = setOf(User::class, Address::class)
)
    .name("users.db")
    .schemaVersion(1)
    .deleteRealmIfMigrationNeeded()
    .build()
val realm = Realm.open(configuration = config)