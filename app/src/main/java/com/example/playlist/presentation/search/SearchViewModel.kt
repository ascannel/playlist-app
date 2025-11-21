package com.example.playlist.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlist.Creator
import com.example.playlist.domain.api.TracksRepository
import com.example.playlist.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed class SearchState {
    object Initial : SearchState()          // экран только открылся
    object Searching : SearchState()        // идёт поиск
    data class Success(val list: List<Track>) : SearchState()
    data class Fail(val error: String) : SearchState()
}

class SearchViewModel(
    private val tracksRepository: TracksRepository
) : ViewModel() {

    private val _searchScreenState =
        MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    fun search(whatSearch: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _searchScreenState.value = SearchState.Searching
                val list = tracksRepository.searchTracks(whatSearch)
                _searchScreenState.value = SearchState.Success(list)
            } catch (e: IOException) {
                _searchScreenState.value =
                    SearchState.Fail(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    // если вдруг ещё нужен «все треки» — можно просто вызвать поиск с пустой строкой
    fun fetchAllTracks() = search("")

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(Creator.getTracksRepository()) as T
                }
            }
    }
}