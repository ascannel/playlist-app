package com.example.playlist.ui.screens

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

// ---- Состояния экрана поиска (как в методичке) ----
sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class Success(val foundList: List<Track>) : SearchState()
    data class Error(val error: String) : SearchState()
}

// ---- ViewModel, работающая НАПРЯМУЮ с репозиторием ----
class SearchViewModel(
    private val repository: TracksRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state = _state.asStateFlow()

    /**
     * Общий метод поиска треков по строке.
     * Это то, что описывается в методичке:
     * ViewModel обращается к TracksRepository и мапит результат в SearchState.
     */
    fun search(expression: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = SearchState.Loading
                val list = repository.searchTracks(expression)
                _state.value = SearchState.Success(list)
            } catch (e: IOException) {
                _state.value = SearchState.Error(e.message ?: "Ошибка сети")
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    /**
     * Удобный хелпер — «получить все треки».
     * В твоих экранах сейчас вызывается fetchAllTracks(), поэтому
     * просто считаем, что "все" = поиск с пустой строкой.
     * С точки зрения архитектуры это всё равно вызов search() и репозитория.
     */
    fun fetchAllTracks() {
        search("")
    }

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

