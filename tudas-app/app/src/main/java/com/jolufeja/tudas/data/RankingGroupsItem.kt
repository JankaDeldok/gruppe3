package com.jolufeja.tudas.data

class RankingGroupsItem : ListItem() {
    override var id: Int = 0
    override var name: String? = null
    override var size: Int = 0

    override fun getType(): Int {
        return 4;
    }
}