package com.islamic.asmaulhusna.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamic.asmaulhusna.data.AsmaulHusnaRepository
import com.islamic.asmaulhusna.data.FavoritesStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(nameId: Int, favorites: FavoritesStore, onBack: () -> Unit) {
    val name = remember(nameId) { AsmaulHusnaRepository.names.first { it.id == nameId } }
    val context = LocalContext.current
    val isFav = favorites.favorites.value.contains(nameId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${name.id}. ${name.transliteration}", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "ফিরে যান")
                    }
                },
                actions = {
                    IconButton(onClick = { favorites.toggle(nameId) }) {
                        Icon(
                            if (isFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            "ফেভারিট",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(name.arabic, fontSize = 64.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(12.dp))
                    Text(name.transliteration, fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold)
                    Text(name.banglaName, fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { AudioPlayer.play(context, nameId) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Icon(Icons.Filled.VolumeUp, null)
                Spacer(Modifier.width(8.dp))
                Text("শুনুন")
            }
            Spacer(Modifier.height(16.dp))
            SectionCard("অর্থ", name.meaning)
            SectionCard("ফজিলত", name.fazilat)
            SectionCard("আমল", name.amal)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SectionCard(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
            Spacer(Modifier.height(6.dp))
            Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 15.sp,
                lineHeight = 22.sp)
        }
    }
}
