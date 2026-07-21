package com.islamic.asmaulhusna.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamic.asmaulhusna.R
import com.islamic.asmaulhusna.data.AsmaulHusnaRepository
import com.islamic.asmaulhusna.data.FavoritesStore
import com.islamic.asmaulhusna.data.localized
import com.islamic.asmaulhusna.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(nameId: Int, favorites: FavoritesStore, onBack: () -> Unit) {
    val name = remember(nameId) { AsmaulHusnaRepository.names.first { it.id == nameId } }
    val loc = name.localized(rememberNameContent())
    val context = LocalContext.current
    val isFav = favorites.favorites.value.contains(nameId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${name.id}. ${name.transliteration}", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.cd_back))
                    }
                },
                actions = {
                    IconButton(onClick = { favorites.toggle(nameId) }) {
                        Icon(
                            if (isFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            stringResource(R.string.cd_favorite),
                            tint = Gold
                        )
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
        Column(
            modifier = Modifier
                .padding(padding)
                .starLattice()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NameHero(
                name.arabic, name.transliteration,
                if (loc.name.equals(name.transliteration, true)) "" else loc.name
            )

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { AudioPlayer.play(context, nameId) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Gold,
                    contentColor = GoldInk
                ),
                contentPadding = PaddingValues(horizontal = 26.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Filled.VolumeUp, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.action_listen), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(20.dp))

            SectionCard(stringResource(R.string.section_meaning), loc.meaning)
            SectionCard(stringResource(R.string.section_virtue), loc.fazilat)
            SectionCard(stringResource(R.string.section_practice), loc.amal)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun NameHero(arabic: String, transliteration: String, bangla: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Page)
            .border(1.dp, GoldLine, RoundedCornerShape(24.dp))
            .padding(4.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.verticalGradient(listOf(EmeraldHi, EmeraldLo)))
            .cornerBrackets()
            .padding(vertical = 30.dp, horizontal = 20.dp)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(arabic, fontSize = 60.sp, fontWeight = FontWeight.Bold,
                color = Cream, textAlign = TextAlign.Center)
            Spacer(Modifier.height(14.dp))
            Text(transliteration, fontSize = 20.sp, color = GoldSoft,
                fontWeight = FontWeight.SemiBold)
            if (bangla.isNotBlank()) Text(bangla, fontSize = 15.sp, color = CreamDim)
        }
    }
}

@Composable
private fun SectionCard(title: String, body: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SectGround)
            .border(1.dp, EmeraldLine, RoundedCornerShape(16.dp))
    ) {
        // gold rule down the leading edge
        Box(Modifier.width(3.dp).heightIn(min = 64.dp).fillMaxHeight().background(Gold))
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = Gold, fontSize = 11.sp,
                letterSpacing = 2.sp)
            Spacer(Modifier.height(8.dp))
            Text(body, color = Cream, fontSize = 14.sp, lineHeight = 24.sp)
        }
    }
}
