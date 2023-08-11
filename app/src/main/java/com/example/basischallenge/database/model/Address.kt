package com.example.basischallenge.database.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.UUID

open class Address: RealmObject {
    @PrimaryKey
    var _id = UUID.randomUUID().toString()
    var typeAddress: String = ""
    var address: String = ""
    var number: Int = 0
    var complement: Int = 0
    var neighborhood: String = ""
    var cep: String = ""
    var city: String = ""
    var uf: String = ""
}