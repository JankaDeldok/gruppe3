package com.jolufeja.tudas.data

open class ListItem {
    open var text: String? = null
    open var id: Int = 0
    open var title: String? = null
    open var author: String? = null
    open var timeLeft: Int = 0
    open var description: String? = null
    open var reward: String? = null
    open var points: Int = 0
    open var type: String? = null
    open var date: String? = null

    open fun getType(): Int {
        return -1
    }
}