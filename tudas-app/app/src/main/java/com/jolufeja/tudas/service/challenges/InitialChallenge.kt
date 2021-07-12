package com.jolufeja.tudas.service.challenges

import java.time.LocalDate

data class InitialChallenge(
    val challengeName: String,
    val creatorName: String,
    val description: String,
    val dueDate: LocalDate,
    val reward: String,
    val addressedTo: String,
    val worth: Int,
)