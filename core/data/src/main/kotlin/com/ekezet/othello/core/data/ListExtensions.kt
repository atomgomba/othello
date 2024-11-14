package com.ekezet.othello.core.data

import android.os.Build

fun <E> MutableList<E>.removeFirstCompat(): E =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        removeFirst()
    } else {
        removeAt(0)
    }

fun <E> MutableList<E>.removeLastCompat(): E =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        removeLast()
    } else {
        removeAt(lastIndex)
    }
