@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.playlist.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.playlist.R
import androidx.compose.ui.semantics.Role
import androidx.core.net.toUri

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current

    // Получаем ресурсы внутри @Composable
    val settingsTitle = stringResource(R.string.settings_title)
    val shareTitle = stringResource(R.string.btn_share)
    val shareText = stringResource(R.string.share_text)
    val devEmail = stringResource(R.string.dev_email)
    val mailSubject = stringResource(R.string.mailto_subject)
    val mailBody = stringResource(R.string.mailto_body)
    val eulaUrl = stringResource(R.string.eula_url)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(settingsTitle, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {

            SettingsRow(title = stringResource(R.string.btn_share)) {
                val share = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                ctx.startActivity(Intent.createChooser(share, shareTitle))
            }
            Divider()

            SettingsRow(title = stringResource(R.string.btn_write)) {
                val mailto = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(devEmail))
                    putExtra(Intent.EXTRA_SUBJECT, mailSubject)
                    putExtra(Intent.EXTRA_TEXT, mailBody)
                }
                ctx.startActivity(mailto)
            }
            Divider()

            SettingsRow(title = stringResource(R.string.btn_eula)) {
                val uri = eulaUrl.toUri()
                val open = Intent(Intent.ACTION_VIEW, uri)
                ctx.startActivity(open)
            }
        }
    }
}

@Composable
private fun SettingsRow(title: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(title) },
        trailingContent = { Icon(Icons.Filled.ChevronRight, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, role = Role.Button)
    )
}
