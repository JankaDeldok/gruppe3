package com.jolufeja.tudas.data

class ChallengesItem : ListItem() {
    override var id: Int = 0
    override var title: String? = null
    override var author: String? = null
    override var timeLeft: Int = 0
    override var description: String? = null
    override var reward: String? = null
    override var points: Int = 0

    override fun getType(): Int {
        return 0;
    }
}