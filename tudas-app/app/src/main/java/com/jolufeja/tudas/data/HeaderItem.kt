package com.jolufeja.tudas.data

import java.util.*

class HeaderItem : ListItem() {
    override var text: String? = null

    override fun getType(): Int {
        return 1;
    }
}