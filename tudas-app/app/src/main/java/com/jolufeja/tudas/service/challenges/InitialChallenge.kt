package com.jolufeja.tudas.service.challenges

import java.util.*

data class InitialChallenge(
    val challengeName: String,
    val creatorName: String,
    val description: String,
    val dueDate: Date,
    val reward: String,
    val addressedTo: String,
    val worth: Int,
)