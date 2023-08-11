package com.example.basischallenge.utils

import com.santalu.maskara.Mask
import com.santalu.maskara.MaskChangedListener
import com.santalu.maskara.MaskStyle

val maskPhone = Mask(
    value = "+55 (__) _ ____-____",
    character = '_',
    style = MaskStyle.COMPLETABLE
)
val PhoneListener = MaskChangedListener(maskPhone)

val maskCpf = Mask(
    value = "___.___.___.-__",
    character = '_',
    style = MaskStyle.COMPLETABLE
)

val cpfListener = MaskChangedListener(maskCpf)

val maskCnpj = Mask(
    value = "__.___.___/____-__",
    character = '_',
    style = MaskStyle.COMPLETABLE
)

val cnpjListener = MaskChangedListener(maskCnpj)

val cepMask = Mask(
    value = "_____-___",
    character = '_',
    style = MaskStyle.COMPLETABLE
)

val cepListener = MaskChangedListener(cepMask)