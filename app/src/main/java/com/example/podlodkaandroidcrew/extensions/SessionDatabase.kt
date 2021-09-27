package com.example.podlodkaandroidcrew.extensions

import com.example.podlodkaandroidcrew.data.model.Session
import com.example.podlodkaandroidcrew.data.model.SessionResponse
import com.example.podlodkaandroidcrew.database.SessionDatabase

fun List<SessionDatabase>.asDomain(): List<Session> {
    return this.map {
        Session(
            date = it.date,
            description = it.description,
            id = it.id,
            imageUrl = it.imageUrl,
            speaker = it.speaker,
            timeInterval = it.timeInterval
        )
    }
}

fun SessionDatabase.asDomain(): Session {
    return Session(
            date = this.date,
            description = this.description,
            id = this.id,
            imageUrl = this.imageUrl,
            speaker = this.speaker,
            timeInterval = this.timeInterval
        )
}