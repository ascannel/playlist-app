package com.example.playlist.data.network

import com.example.playlist.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}