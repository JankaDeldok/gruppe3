package com.jolufeja.tudas.data

import com.jolufeja.tudas.service.challenges.Challenge

class ChallengesItem : ListItem() {
    override var id: Int = 0
    override var title: String? = null
    override var author: String? = null
    override var timeLeft: Int = 0
    override var description: String? = null
    override var reward: String? = null
    override var points: Int = 0

    lateinit var challenge: Challenge

    override fun getType(): Int {
        return 0;
    }
}