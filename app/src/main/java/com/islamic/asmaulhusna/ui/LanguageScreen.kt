package com.islamic.asmaulhusna.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamic.asmaulhusna.R
import com.islamic.asmaulhusna.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val current = remember { LocaleStore.language(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.language_title), fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier.padding(padding).starLattice(),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            items(AppLanguage.entries) { lang ->
                val selected = lang == current
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SectGround)
                        .border(1.dp, if (selected) Gold else EmeraldLine, RoundedCornerShape(16.dp))
                        .clickable {
                            if (!selected) {
                                LocaleStore.setLanguage(context, lang)
                                (context as? Activity)?.recreate()
                            } else onBack()
                        }
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(lang.endonym, color = Cream, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text(lang.english, color = CreamDim, fontSize = 12.sp)
                    }
                    if (selected) Icon(Icons.Filled.Check, null, tint = Gold)
                }
            }
        }
    }
}
