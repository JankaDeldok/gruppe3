package com.jolufeja.tudas.service.challenges

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.time.LocalDate
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class Challenge(
    val id: Int? = null,
    val name: String,
    val creator: String,
    val creationDate: LocalDate,
    val description: String,
    val dueDate: Date,
    val reward: String,
    val worth: Int,
    val addressedTo: String
) : Serializable