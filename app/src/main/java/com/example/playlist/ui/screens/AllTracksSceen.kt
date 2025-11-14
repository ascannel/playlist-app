package com.example.playlist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlist.domain.models.Track

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTracksScreen(
    onBack: () -> Unit,
    viewModel: SearchViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAllTracks(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Все треки") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (state) {
                is SearchState.Initial -> { /* ничего не рисуем */ }

                is SearchState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                is SearchState.Success -> {
                    val tracks = (state as SearchState.Success).foundList
                    if (tracks.isEmpty()) {
                        Text(
                            text = "На устройстве не найдено треков в папке Music",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(tracks) { track: Track ->
                                TrackListItem(track)
                                HorizontalDivider(thickness = 0.5.dp)
                            }
                        }
                    }
                }

                is SearchState.Error -> {
                    Text(
                        text = "Ошибка: ${(state as SearchState.Error).error}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}
