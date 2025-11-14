package com.example.playlist.data.network

import com.example.playlist.creator.Storage
import com.example.playlist.data.dto.BaseResponse
import com.example.playlist.data.dto.TracksSearchRequest
import com.example.playlist.data.dto.TracksSearchResponse

class RetrofitNetworkClient(
    private val storage: Storage = Storage()
) : NetworkClient {

    override fun doRequest(dto: Any): BaseResponse {
        return when (dto) {
            is TracksSearchRequest -> {
                val searchList = storage.search(dto.expression)
                TracksSearchResponse(searchList).apply {
                    resultCode = 200
                }
            }

            else -> {
                // Неподдерживаемый запрос – вернём пустой ответ с кодом ошибки
                TracksSearchResponse(emptyList()).apply {
                    resultCode = 400
                }
            }
        }
    }
}