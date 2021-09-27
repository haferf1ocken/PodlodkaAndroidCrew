package com.example.podlodkaandroidcrew.data.model

data class SessionResponse(
    val date: String,
    val description: String,
    val id: String,
    val imageUrl: String,
    val isFavourite: Boolean,
    val speaker: String,
    val timeInterval: String
)