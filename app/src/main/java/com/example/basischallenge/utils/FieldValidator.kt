package com.example.basischallenge.utils

import android.util.Patterns

fun String.isValidEmail() =
    this.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPhone() =
    this.isEmpty() || !Patterns.PHONE.matcher(this).matches()

fun String.isUfValid() =
    this.isEmpty() || !listAllBrazilUnionFederative.contains(this.uppercase())


val listAllBrazilUnionFederative = listOf<String>(
    "RO", "AC", "AM", "RR", "PA", "AP",
    "TO", "MA", "PI", "CE", "RN", "PB",
    "PE", "AL", "SE", "BA", "MG", "ES",
    "RJ", "SP", "PR", "SC", "RS", "MS",
    "MT", "GO", "DF"
)