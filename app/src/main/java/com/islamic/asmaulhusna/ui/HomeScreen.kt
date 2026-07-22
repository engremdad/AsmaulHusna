package com.islamic.asmaulhusna.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamic.asmaulhusna.R
import com.islamic.asmaulhusna.data.AsmaulHusna
import com.islamic.asmaulhusna.data.AsmaulHusnaRepository
import com.islamic.asmaulhusna.data.NameContent
import com.islamic.asmaulhusna.data.localized
import com.islamic.asmaulhusna.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNameClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit,
    onVirtuesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val all = AsmaulHusnaRepository.names
    val content = rememberNameContent()
    val filtered = remember(query, content) {
        if (query.isBlank()) all
        else all.filter {
            val c = it.localized(content)
            it.transliteration.contains(query, true) ||
                    c.name.contains(query, true) ||
                    c.meaning.contains(query, true) ||
                    it.arabic.contains(query)
        }
    }
    val today = remember { AsmaulHusnaRepository.today() }

    Box(Modifier.mushafGround()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("أَسْمَاءُ ٱللَّٰه", fontSize = 24.sp, fontWeight = FontWeight.Bold,
                            color = Gold)
                        Text(stringResource(R.string.app_subtitle), fontSize = 11.sp,
                            letterSpacing = 2.sp, color = CreamDim)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Cream
                ),
                actions = {
                    IconButton(onClick = onVirtuesClick) {
                        Icon(Icons.AutoMirrored.Filled.MenuBook, stringResource(R.string.cd_virtues), tint = Gold)
                    }
                    IconButton(onClick = onFavoritesClick) {
                        Icon(Icons.Filled.Favorite, stringResource(R.string.cd_favorites), tint = Gold)
                    }
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Filled.Settings, stringResource(R.string.settings_title), tint = Gold)
                    }
                }
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(padding).padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            item(span = { GridItemSpan(3) }) {
                TodayCard(today, today.localized(content), onClick = { onNameClick(today.id) })
            }
            item(span = { GridItemSpan(3) }) {
                SearchField(query, onChange = { query = it })
            }
            itemsIndexed(filtered) { _, name ->
                NameGridCard(name, name.localized(content), onClick = { onNameClick(name.id) })
            }
        }
    }
    }
}

@Composable
private fun TodayCard(name: AsmaulHusna, loc: NameContent, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            // outer emerald spacer + gold hairline = the double-rule frame
            .clip(RoundedCornerShape(20.dp))
            .background(Page)
            .border(1.dp, GoldLine, RoundedCornerShape(20.dp))
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Brush.verticalGradient(listOf(EmeraldGlow, EmeraldHi, EmeraldLo)))
            .background(Brush.verticalGradient(0f to GoldSoft.copy(alpha = 0.07f), 0.09f to GoldSoft.copy(alpha = 0f)))
            .border(1.dp, GoldLine, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp, horizontal = 18.dp)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.today_label).uppercase(), color = Gold, fontSize = 10.sp,
                letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Text(name.arabic, fontSize = 42.sp, color = Cream,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            val todaySub = if (loc.name.equals(name.transliteration, true)) name.transliteration
                else "${name.transliteration} · ${loc.name}"
            Text(todaySub,
                color = GoldSoft, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(loc.meaning, color = CreamDim, fontSize = 13.sp,
                textAlign = TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(query: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onChange,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        placeholder = { Text(stringResource(R.string.search_hint), color = CreamDim) },
        leadingIcon = { Icon(Icons.Filled.Search, null, tint = GoldDim) },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = EmeraldRow,
            unfocusedContainerColor = EmeraldRow,
            focusedBorderColor = Gold,
            unfocusedBorderColor = EmeraldLine,
            focusedTextColor = Cream,
            unfocusedTextColor = Cream,
            cursorColor = Gold
        )
    )
}

@Composable
private fun NameGridCard(name: AsmaulHusna, loc: NameContent, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .height(118.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Brush.verticalGradient(listOf(EmeraldRow, EmeraldLo)))
            .border(1.dp, EmeraldLine, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        Text("${name.id}", color = GoldDim, fontSize = 9.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 2.dp))
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(name.arabic, fontSize = 22.sp, fontWeight = FontWeight.Bold,
                color = GoldSoft, textAlign = TextAlign.Center, maxLines = 1)
            Spacer(Modifier.height(4.dp))
            Text(loc.name, fontSize = 11.sp, textAlign = TextAlign.Center,
                color = CreamDim, maxLines = 1)
        }
    }
}
