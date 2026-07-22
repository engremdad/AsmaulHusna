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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamic.asmaulhusna.R
import com.islamic.asmaulhusna.ui.theme.*

/**
 * Shown once, on first launch, before the app is entered. Picking a language
 * persists the choice and recreates the activity — which then renders the app
 * (in the chosen language) because [LocaleStore.isLanguageChosen] is now true.
 */
@Composable
fun FirstRunLanguageScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Page)
            .starLattice()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, bottom = 20.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("أَسْمَاءُ ٱللَّٰه", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Gold)
            Spacer(Modifier.height(12.dp))
            Text(
                stringResource(R.string.choose_language),
                color = Cream, fontSize = 18.sp, fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            items(AppLanguage.entries) { lang ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SectGround)
                        .border(1.dp, EmeraldLine, RoundedCornerShape(16.dp))
                        .clickable {
                            LocaleStore.setLanguage(context, lang)
                            (context as? Activity)?.recreate()
                        }
                        .padding(horizontal = 18.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(lang.endonym, color = Cream, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
                        Text(lang.english, color = CreamDim, fontSize = 12.sp)
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = GoldDim)
                }
            }
        }
    }
}
