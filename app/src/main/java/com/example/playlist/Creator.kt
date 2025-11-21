package com.example.playlist

import com.example.playlist.data.Storage
import com.example.playlist.data.network.RetrofitNetworkClient
import com.example.playlist.data.repository.TracksRepositoryImpl
import com.example.playlist.domain.api.TracksRepository

object Creator {

    fun getTracksRepository(): TracksRepository {
        val storage = Storage()
        val client = RetrofitNetworkClient(storage)
        return TracksRepositoryImpl(client)
    }
}
