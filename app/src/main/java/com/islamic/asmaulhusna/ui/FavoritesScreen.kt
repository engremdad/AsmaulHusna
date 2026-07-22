package com.islamic.asmaulhusna.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamic.asmaulhusna.R
import com.islamic.asmaulhusna.data.AsmaulHusnaRepository
import com.islamic.asmaulhusna.data.FavoritesStore
import com.islamic.asmaulhusna.data.localized
import com.islamic.asmaulhusna.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(favorites: FavoritesStore, onNameClick: (Int) -> Unit, onBack: () -> Unit) {
    val favIds = favorites.favorites.value
    val list = AsmaulHusnaRepository.names.filter { it.id in favIds }
    val content = rememberNameContent()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.favorites_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.cd_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Page,
                    titleContentColor = Gold,
                    navigationIconContentColor = Gold
                )
            )
        },
        containerColor = Page
    ) { padding ->
        if (list.isEmpty()) {
            Box(Modifier.padding(padding).starLattice().fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.favorites_empty), color = CreamDim)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).starLattice(),
                contentPadding = PaddingValues(14.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                items(list, key = { it.id }) { name ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Brush.verticalGradient(listOf(EmeraldRow, EmeraldLo)))
                            .border(1.dp, EmeraldLine, RoundedCornerShape(16.dp))
                            .clickable { onNameClick(name.id) }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(name.arabic, fontSize = 26.sp, fontWeight = FontWeight.Bold,
                            color = GoldSoft)
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text("${name.id}. ${name.transliteration}",
                                fontWeight = FontWeight.SemiBold, color = Cream)
                            Text(name.localized(content).meaning, fontSize = 13.sp, color = CreamDim)
                        }
                    }
                }
            }
        }
    }
}
