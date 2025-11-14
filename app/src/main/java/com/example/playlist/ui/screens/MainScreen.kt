@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.playlist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.playlist.R

@Composable
fun MainScreen(
    onSearchClick: () -> Unit,
    onTracksClick: () -> Unit,
    onPlaylistsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.home_title),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Первый ряд: Поиск + Треки
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                QuickTile(
                    title = "Поиск",
                    icon = Icons.Filled.Search,
                    modifier = Modifier.weight(1f),
                    onClick = onSearchClick
                )
                QuickTile(
                    title = "Треки",
                    icon = Icons.Filled.List,
                    modifier = Modifier.weight(1f),
                    onClick = onTracksClick
                )
            }

            // Второй ряд: Плейлисты + Настройки
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                QuickTile(
                    title = "Плейлисты",
                    icon = Icons.Filled.LibraryMusic,
                    modifier = Modifier.weight(1f),
                    onClick = onPlaylistsClick
                )
                QuickTile(
                    title = "Настройки",
                    icon = Icons.Filled.Settings,
                    modifier = Modifier.weight(1f),
                    onClick = onSettingsClick
                )
            }
        }
    }
}

@Composable
private fun QuickTile(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.height(112.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(icon, contentDescription = null)
            Text(title, style = MaterialTheme.typography.titleMedium)
        }
    }
}
