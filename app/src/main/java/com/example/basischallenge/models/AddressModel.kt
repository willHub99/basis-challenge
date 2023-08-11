package com.example.basischallenge.models

data class AddressModel(
    val typeAddress: String,
    val address: String,
    val number: Int,
    val complement: Int,
    val neighborhood: String,
    val cep: String,
    val city: String,
    val uf: String
)
