package com.example.playlist.domain.api

import com.example.playlist.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}