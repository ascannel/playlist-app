package com.example.playlist.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlist.Creator
import com.example.playlist.domain.api.TrackSearchInteractor
import com.example.playlist.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val interactor: TrackSearchInteractor
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _results = MutableStateFlow<List<Track>>(emptyList())
    val results = _results.asStateFlow()

    fun onQueryChange(newValue: String) {
        _query.value = newValue
    }

    fun clear() {
        _query.value = ""
        _results.value = emptyList()
    }

    // Запрос делаем в корутине, чтобы не блокировать UI
    fun search() {
        val q = _query.value.trim()
        viewModelScope.launch {
            _results.value = if (q.isEmpty()) emptyList() else interactor.searchTracks(q)
        }
    }

    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            // используем Creator (см. ранее добавленный Creator)
            return SearchViewModel(Creator.provideTrackSearchInteractor()) as T
        }
    }
}