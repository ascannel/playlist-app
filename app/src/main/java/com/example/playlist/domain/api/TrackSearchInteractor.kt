package com.example.playlist.domain.api

import com.example.playlist.domain.models.Track

interface TrackSearchInteractor {
    suspend fun searchTracks(expression: String): List<Track>
}
