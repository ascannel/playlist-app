package com.example.playlist.domain.api

import com.example.playlist.domain.models.Track

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
}
