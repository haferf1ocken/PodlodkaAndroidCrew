package com.example.podlodkaandroidcrew.data.repository

import com.example.podlodkaandroidcrew.api.PodlodkaAndroidCrewApi
import com.example.podlodkaandroidcrew.data.Result
import com.example.podlodkaandroidcrew.data.model.Session
import com.example.podlodkaandroidcrew.data.model.SessionResponse
import com.example.podlodkaandroidcrew.database.SessionDao
import com.example.podlodkaandroidcrew.extensions.asDatabaseModel
import com.example.podlodkaandroidcrew.extensions.asDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class SessionRepository(
    private val sessionDao: SessionDao
) {

    fun fetchSessions(): Flow<Result<Any>> = flow {
        val response = Result.on {
            PodlodkaAndroidCrewApi.retrofitService.getSessions()
        }
        if (response is Result.Success) {
            saveSessionsToLocal(response.data)
        }
        emit(response)

    }.flowOn(Dispatchers.IO)

    private suspend fun saveSessionsToLocal(sessions: List<SessionResponse>) {
        sessionDao.insertAll(*sessions.asDatabaseModel())
    }

    fun getSessions(): Flow<List<Session>> = sessionDao.getAll().map { it.asDomain() }

    fun getSessionById(id: String) = sessionDao.getSessionWithId(id).map { it.asDomain() }
}