package com.example.playlist.data.repository

import com.example.playlist.data.dto.TracksSearchRequest
import com.example.playlist.data.network.RetrofitNetworkClient
import com.example.playlist.domain.api.TracksRepository
import com.example.playlist.domain.models.Track
import kotlinx.coroutines.delay

class TracksRepositoryImpl(
    private val networkClient: RetrofitNetworkClient
) : TracksRepository {

    override suspend fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        // эмулируем задержку сервера
        delay(1000)

        return if (response.resultCode == 200) {
            (response as com.example.playlist.data.dto.TracksSearchResponse)
                .results
                .map { dto ->
                    val totalSeconds = dto.trackTimeMillis / 1000
                    val minutes = totalSeconds / 60
                    val seconds = totalSeconds % 60
                    val timeString = "%02d:%02d".format(minutes, seconds)

                    Track(
                        trackName = dto.trackName,
                        artistName = dto.artistName,
                        trackTime = timeString
                    )
                }
        } else {
            emptyList()
        }
    }
}