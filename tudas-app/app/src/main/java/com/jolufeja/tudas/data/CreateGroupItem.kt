package com.jolufeja.tudas.data

class CreateGroupItem  : ListItem() {
    override var id: Int = 0
    override var text: String? = null

    override fun getType(): Int {
        return 6;
    }
}