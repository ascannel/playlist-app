package com.example.playlist.data.network

import com.example.playlist.data.dto.BaseResponse
import com.example.playlist.data.dto.TrackDto
import com.example.playlist.data.dto.TracksSearchRequest
import com.example.playlist.data.dto.TracksSearchResponse

class RetrofitNetworkClient : NetworkClient {
    override fun doRequest(dto: Any): BaseResponse {
        return if (dto is TracksSearchRequest) {
            val fake = listOf(
                TrackDto(trackName = "Never Gonna Give You Up", artistName = "Rick Astley", trackTimeMillis = 213_000),
                TrackDto(trackName = "Billie Jean", artistName = "Michael Jackson", trackTimeMillis = 294_000),
            )
            return TracksSearchResponse(fake).apply { resultCode = 200 }
        } else {
            BaseResponse().apply { resultCode = 400 }
        }
    }
}
