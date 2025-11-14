package com.example.playlist.domain.impl

import com.example.playlist.domain.api.TrackSearchInteractor
import com.example.playlist.domain.api.TracksRepository
import com.example.playlist.domain.models.Track

class TrackSearchInteractorImpl(
    private val repository: TracksRepository
) : TrackSearchInteractor {
    override fun searchTracks(expression: String): List<Track> =
        repository.searchTracks(expression)
}