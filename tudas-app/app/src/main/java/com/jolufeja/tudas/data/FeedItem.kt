package com.jolufeja.tudas.data

class FeedItem : ListItem() {
    override var id: Int = 0
    override var text: String? = null
    override var type: String? = null
    override var date: String? = null

    override fun getType(): Int {
        return 2;
    }
}