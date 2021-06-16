package com.jolufeja.tudas.data

open class ListItem {
    open var text: String? = null

    open fun getType(): Int {
        return -1
    }
}