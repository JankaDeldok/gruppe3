package com.jolufeja.tudas.data

import java.util.*

class HeaderItem : ListItem() {

    companion object {
        operator fun invoke(text: String) = HeaderItem().apply { this.text = text }
    }
    override var text: String? = null

    override fun getType(): Int {
        return 1;
    }
}