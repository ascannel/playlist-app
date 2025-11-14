package com.example.playlist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playlist.domain.models.Track

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit
) {
    val vm: SearchViewModel = viewModel(
        factory = SearchViewModel.getViewModelFactory()
    )
    val state by vm.state.collectAsState()

    var query by remember { mutableStateOf("") }

    // При первом заходе — загрузить все треки (по факту search(""))
    LaunchedEffect(Unit) {
        vm.fetchAllTracks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Поиск") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Поиск") },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            // если хочешь искать через репозиторий — можно вызвать:
                            // vm.search(query)
                            // пока оставляем фильтрацию по уже загруженному списку
                        }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    }
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Очистить")
                        }
                    }
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            when (state) {
                is SearchState.Initial,
                is SearchState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 24.dp))
                    }
                }

                is SearchState.Success -> {
                    val tracks = (state as SearchState.Success).foundList
                    val filtered = if (query.isBlank()) tracks else tracks.filter {
                        it.trackName.contains(query, ignoreCase = true) ||
                                it.artistName.contains(query, ignoreCase = true)
                    }

                    if (filtered.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Text("Ничего не найдено", modifier = Modifier.padding(top = 24.dp))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filtered) { track ->
                                TrackListItem(track = track)
                                Divider()
                            }
                        }
                    }
                }

                is SearchState.Error -> {
                    val err = (state as SearchState.Error).error
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Ошибка: $err", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun TrackListItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Заглушка под обложку
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = track.trackName, style = MaterialTheme.typography.titleMedium)
            Text(text = track.artistName, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = track.trackTime, style = MaterialTheme.typography.bodySmall)
    }
}
