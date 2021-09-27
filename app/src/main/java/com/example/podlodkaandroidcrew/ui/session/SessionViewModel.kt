package com.example.podlodkaandroidcrew.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.podlodkaandroidcrew.data.model.Session
import com.example.podlodkaandroidcrew.data.repository.SessionRepository
import com.example.podlodkaandroidcrew.ui.home.HomeViewModel
import kotlinx.coroutines.flow.*

class SessionViewModel(private val repository: SessionRepository) : ViewModel() {

    private val _session = MutableStateFlow<Session?>(null)
    val session: StateFlow<Session?>
        get() = _session

    fun getSessionById(id: String) {
        repository.getSessionById(id)
            .onEach { _session.value = it }
            .launchIn(viewModelScope)
    }

    class Factory(private val repo: SessionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SessionViewModel(repo) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
