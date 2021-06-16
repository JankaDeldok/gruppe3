package com.jolufeja.tudas.data

class FeedItem : ListItem() {
    var id: Int = 0
    override var text: String? = null
    var type: String? = null
    var date: String? = null

    override fun getType(): Int {
        return 0;
    }
}