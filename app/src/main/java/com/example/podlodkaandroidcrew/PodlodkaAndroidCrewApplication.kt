package com.example.podlodkaandroidcrew

import android.app.Application
import com.example.podlodkaandroidcrew.data.repository.SessionRepository
import com.example.podlodkaandroidcrew.database.PodlodkaAndroidCrewDatabase

class PodlodkaAndroidCrewApplication : Application() {
    private val database by lazy { PodlodkaAndroidCrewDatabase.getDatabase(this) }
    val repository by lazy { SessionRepository(database.sessionDao()) }
}