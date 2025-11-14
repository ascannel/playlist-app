package com.example.playlist

import com.example.playlist.data.network.RetrofitNetworkClient
import com.example.playlist.data.repository.TracksRepositoryImpl
import com.example.playlist.domain.api.TrackSearchInteractor
import com.example.playlist.domain.api.TracksRepository
import com.example.playlist.domain.impl.TrackSearchInteractorImpl

object Creator {

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackSearchInteractor(): TrackSearchInteractor {
        return TrackSearchInteractorImpl(getTracksRepository())
    }

    // если хочешь без интерактора:
    fun provideTracksRepository(): TracksRepository = getTracksRepository()
}
