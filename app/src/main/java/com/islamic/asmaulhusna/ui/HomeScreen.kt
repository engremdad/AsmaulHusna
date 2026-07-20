package com.islamic.asmaulhusna.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamic.asmaulhusna.data.AsmaulHusna
import com.islamic.asmaulhusna.data.AsmaulHusnaRepository
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNameClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val all = AsmaulHusnaRepository.names
    val filtered = remember(query) {
        if (query.isBlank()) all
        else all.filter {
            it.transliteration.contains(query, true) ||
                    it.banglaName.contains(query, true) ||
                    it.meaning.contains(query, true) ||
                    it.arabic.contains(query)
        }
    }
    val today = remember {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        all[dayOfYear % all.size]
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("আসমাউল হুসনা", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = onFavoritesClick) {
                        Icon(Icons.Filled.Favorite, "প্রিয় নাম",
                            tint = MaterialTheme.colorScheme.secondary)
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(padding).padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item(span = { GridItemSpan(3) }) {
                TodayCard(today, onClick = { onNameClick(today.id) })
            }
            item(span = { GridItemSpan(3) }) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    placeholder = { Text("নাম বা অর্থ খুঁজুন…") },
                    leadingIcon = { Icon(Icons.Filled.Search, null) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
            }
            itemsIndexed(filtered) { _, name ->
                NameGridCard(name, onClick = { onNameClick(name.id) })
            }
        }
    }
}

@Composable
private fun TodayCard(name: AsmaulHusna, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("আজকের নাম", color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)
            Spacer(Modifier.height(6.dp))
            Text(name.arabic, fontSize = 40.sp, color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(6.dp))
            Text("${name.transliteration} — ${name.banglaName}",
                color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
            Text(name.meaning, color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp,
                textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun NameGridCard(name: AsmaulHusna, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.height(120.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(28.dp).clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Text("${name.id}", color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Text(name.arabic, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center,
                maxLines = 1)
            Text(name.banglaName, fontSize = 12.sp, textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
        }
    }
}

// re-export helper for span
private fun GridItemSpan(n: Int) = androidx.compose.foundation.lazy.grid.GridItemSpan(n)
