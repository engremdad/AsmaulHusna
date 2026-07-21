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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamic.asmaulhusna.data.AsmaulHusna
import com.islamic.asmaulhusna.data.AsmaulHusnaRepository
import com.islamic.asmaulhusna.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNameClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit,
    onSettingsClick: () -> Unit
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
    val today = remember { AsmaulHusnaRepository.today() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("أَسْمَاءُ ٱللَّٰه", fontSize = 24.sp, fontWeight = FontWeight.Bold,
                            color = Gold)
                        Text("আসমাউল হুসনা · ৯৯ নাম", fontSize = 11.sp,
                            letterSpacing = 2.sp, color = CreamDim)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Page,
                    titleContentColor = Cream
                ),
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Filled.Notifications, "রিমাইন্ডার", tint = Gold)
                    }
                    IconButton(onClick = onFavoritesClick) {
                        Icon(Icons.Filled.Favorite, "প্রিয় নাম", tint = Gold)
                    }
                }
            )
        },
        containerColor = Page
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(padding).starLattice().padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            item(span = { GridItemSpan(3) }) {
                TodayCard(today, onClick = { onNameClick(today.id) })
            }
            item(span = { GridItemSpan(3) }) {
                SearchField(query, onChange = { query = it })
            }
            itemsIndexed(filtered) { _, name ->
                NameGridCard(name, onClick = { onNameClick(name.id) })
            }
        }
    }
}

@Composable
private fun TodayCard(name: AsmaulHusna, onClick: () -> Unit) {
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
            .background(Brush.verticalGradient(listOf(EmeraldHi, EmeraldLo)))
            .border(1.dp, GoldLine, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp, horizontal = 18.dp)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("আজকের নাম · TODAY", color = Gold, fontSize = 10.sp,
                letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Text(name.arabic, fontSize = 42.sp, color = Cream,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text("${name.transliteration} · ${name.banglaName}",
                color = GoldSoft, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(name.meaning, color = CreamDim, fontSize = 13.sp,
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
        placeholder = { Text("নাম বা অর্থ খুঁজুন…", color = CreamDim) },
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
private fun NameGridCard(name: AsmaulHusna, onClick: () -> Unit) {
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
            Text(name.banglaName, fontSize = 11.sp, textAlign = TextAlign.Center,
                color = CreamDim, maxLines = 1)
        }
    }
}
