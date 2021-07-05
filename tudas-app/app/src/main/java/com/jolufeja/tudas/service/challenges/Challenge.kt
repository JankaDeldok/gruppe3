package com.jolufeja.tudas.service.challenges

import java.time.LocalDate

data class Challenge(
    val id: Int,
    val challengeName: String,
    val creatorName: String,
    val description: String,
    val dueDate: LocalDate,
    val reward: String,
    val points: Int,
    val isPublic: Boolean
)