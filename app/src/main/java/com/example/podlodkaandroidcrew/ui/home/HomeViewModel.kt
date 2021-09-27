package com.example.podlodkaandroidcrew.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.podlodkaandroidcrew.data.Result
import com.example.podlodkaandroidcrew.data.model.Session
import com.example.podlodkaandroidcrew.data.repository.SessionRepository
import com.example.podlodkaandroidcrew.extensions.addOrRemove
import com.example.podlodkaandroidcrew.ui.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: SessionRepository) : ViewModel() {

    private val _favorites = MutableStateFlow<Set<Session>>(setOf())
    val favorites: StateFlow<Set<Session>>
        get() = _favorites.asStateFlow()

    private val _sessionsWithDates = MutableStateFlow<UiState<Map<String, List<Session>>>>(UiState.Default)
    val sessionsWithDates: StateFlow<UiState<Map<String, List<Session>>>>
        get() = _sessionsWithDates.asStateFlow()

    init {
        repository.fetchSessions()
            .onStart {
                _sessionsWithDates.value = UiState.Loading
            }
            .onEach { result ->
                if (result is Result.Error) {
                        _sessionsWithDates.value = UiState.Error(
                            result.exception.localizedMessage ?: "Uknown Error")
                }
                getSessionsFromLocal()
            }
            .launchIn(viewModelScope)
    }

    private fun getSessionsFromLocal() {
        repository.getSessions()
            .onEach {
                _sessionsWithDates.value = UiState.Success(
                    createMapFromList(it)
                )
            }
            .launchIn(viewModelScope)
    }

    private fun createMapFromList(sessions: List<Session>): Map<String, List<Session>> {
        val dates = mutableSetOf<String>()
        val consolidatedMap = mutableMapOf<String, List<Session>>()
        sessions.forEach {
            dates.add(it.date)
        }
        dates.forEach { date ->
            consolidatedMap[date] = sessions.filter { session ->
                session.date.contains(date)
            }
        }
        return consolidatedMap.toMap()
    }

    fun toggleFavorite(session: Session) = viewModelScope.launch {
        val set = _favorites.value.toMutableSet()
        set.addOrRemove(session)
        _favorites.value = set.toSet()
    }

    class Factory(private val repo: SessionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}