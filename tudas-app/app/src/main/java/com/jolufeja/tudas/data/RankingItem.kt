package com.jolufeja.tudas.data

class RankingItem : ListItem() {
    override var id: Int = 0
    override var name: String? = null
    override var points: Int = 0
    override var ranking: Int = 0
    override var rankingType: Int = 0

    override fun getType(): Int {
        return 3;
    }
}