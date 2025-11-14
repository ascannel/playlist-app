package com.example.playlist.domain.models

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String, // формат mm:ss
)