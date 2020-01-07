package com.github.baldogre.yaspacetest.model

data class Event(
    val id: Int,
    val title: String,
    val date: String,
    val hashTags: List<String>,
    val iAmGoing: Boolean,
    val numberOfParticipant: Int,
    val maxParticipant: Int
)