package com.example.podlodkaandroidcrew.extensions

import com.example.podlodkaandroidcrew.data.model.Session
import com.example.podlodkaandroidcrew.data.model.SessionResponse
import com.example.podlodkaandroidcrew.database.SessionDatabase

fun List<SessionResponse>.asDatabaseModel(): Array<SessionDatabase> {
    return this.map {
        SessionDatabase(
            date = it.date,
            description = it.description,
            id = it.id,
            imageUrl = it.imageUrl,
            isFavourite = it.isFavourite,
            speaker = it.speaker,
            timeInterval = it.timeInterval
        )
    }.toTypedArray()
}