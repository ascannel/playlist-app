package com.example.playlist.ui.screens

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SearchState {
    object Initial : SearchState()
    object Loading : SearchState()
    data class Success(val foundList: List<Track>) : SearchState()
    data class Error(val error: String) : SearchState()
}

class SearchViewModel : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state = _state.asStateFlow()

    /**
     * Загружаем все треки с устройства (фильтруем по /Music).
     */
    fun loadAllTracks(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.value = SearchState.Loading
                val list = queryDeviceTracks(context)
                _state.value = SearchState.Success(list)
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.message ?: "Ошибка чтения файлов")
            }
        }
    }

    private fun queryDeviceTracks(context: Context): List<Track> {
        val resolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,    // путь к файлу, пригодится для фильтрации
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val cursor = resolver.query(uri, projection, selection, null, null)
            ?: return emptyList()

        val result = mutableListOf<Track>()

        cursor.use { c ->
            val titleIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataIdx = c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (c.moveToNext()) {
                val path = c.getString(dataIdx) ?: ""

                // Оставляем только файлы из /Music (или /music)
                if (!path.contains("/music", ignoreCase = true)) continue

                val title = c.getString(titleIdx) ?: "Unknown"
                val artist = c.getString(artistIdx) ?: "Unknown"
                val durationMs = c.getLong(durationIdx)

                val totalSeconds = (durationMs / 1000).toInt()
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                val time = "%d:%02d".format(minutes, seconds)

                result.add(
                    Track(
                        trackName = title,
                        artistName = artist,
                        trackTime = time
                    )
                )
            }
        }

        return result
    }
}
