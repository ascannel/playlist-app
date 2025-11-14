package com.example.playlist.data.repository

import com.example.playlist.data.dto.TracksSearchRequest
import com.example.playlist.data.dto.TracksSearchResponse
import com.example.playlist.data.network.NetworkClient
import com.example.playlist.domain.api.TracksRepository
import com.example.playlist.domain.models.Track

class TracksRepositoryImpl(
    private val networkClient: NetworkClient
) : TracksRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TracksSearchResponse).results.map {
                val seconds = it.trackTimeMillis / 1000
                val minutes = seconds / 60
                val trackTime = "%02d:%02d".format(minutes, seconds - minutes * 60)
                Track(it.trackName, it.artistName, trackTime)
            }
        }
        return emptyList()
    }
}
