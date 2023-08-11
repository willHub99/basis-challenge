package com.example.basischallenge.database.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.util.UUID

class User(): RealmObject {
    @PrimaryKey
    var _id = UUID.randomUUID().toString()
    var type: String = ""
    var name: String = ""
    var document: String = ""
    var phone: String = ""
    var email: String = ""
    var address: RealmList<Address> = realmListOf<Address>()
}