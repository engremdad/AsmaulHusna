package com.islamic.asmaulhusna.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
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
                    val shareBody = buildShareText(
                        arabic = name.arabic,
                        transliteration = name.transliteration,
                        name = loc.name,
                        sections = listOf(
                            stringResource(R.string.section_meaning) to loc.meaning,
                            stringResource(R.string.section_virtue) to loc.fazilat,
                            stringResource(R.string.section_practice) to loc.amal
                        ),
                        appName = stringResource(R.string.app_name)
                    )
                    val copiedMsg = stringResource(R.string.toast_copied)
                    IconButton(onClick = { copyToClipboard(context, name.transliteration, shareBody, copiedMsg) }) {
                        Icon(Icons.Filled.ContentCopy, stringResource(R.string.cd_copy), tint = Gold)
                    }
                    IconButton(onClick = { shareTextVia(context, shareBody) }) {
                        Icon(Icons.Filled.Share, stringResource(R.string.cd_share), tint = Gold)
                    }
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

private fun buildShareText(
    arabic: String,
    transliteration: String,
    name: String,
    sections: List<Pair<String, String>>,
    appName: String
): String {
    val title = if (name.isNotBlank() && !name.equals(transliteration, true))
        "$arabic — $transliteration ($name)" else "$arabic — $transliteration"
    val body = sections
        .filter { it.second.isNotBlank() }
        .joinToString("\n\n") { (label, text) -> "$label\n$text" }
    val main = if (body.isBlank()) title else "$title\n\n$body"
    return "$main\n\n— $appName"
}

private fun copyToClipboard(context: Context, label: String, text: String, toast: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
    // Android 13+ shows its own system copy confirmation, so avoid a duplicate toast.
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }
}

private fun shareTextVia(context: Context, text: String) {
    val send = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(send, null))
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
            .drawBehind {
                // luminous glow lifted behind the name, settling into darker emerald
                drawRect(
                    Brush.radialGradient(
                        colors = listOf(EmeraldGlow, EmeraldLo, Ink),
                        center = Offset(size.width / 2f, size.height * 0.04f),
                        radius = size.height * 1.15f
                    )
                )
            }
            .background(Brush.verticalGradient(0f to GoldSoft.copy(alpha = 0.09f), 0.07f to GoldSoft.copy(alpha = 0f)))
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(SectGround)
            .drawBehind {
                // Match CSS `border:1px emerald; border-left:3px gold` on a rounded card:
                // stroke the full emerald border, then reveal a 3px gold stroke on the
                // left only — so the gold follows the rounded top-left/bottom-left corners.
                val r = 16.dp.toPx()
                val cr = CornerRadius(r, r)
                val s1 = 1.dp.toPx()
                val s3 = 3.dp.toPx()
                drawRoundRect(
                    color = EmeraldLine, cornerRadius = cr,
                    topLeft = Offset(s1 / 2f, s1 / 2f),
                    size = Size(size.width - s1, size.height - s1),
                    style = Stroke(s1)
                )
                // Gold left rule that tapers off toward the corners, slowly dissolving
                // into the emerald border instead of ending abruptly.
                val f = ((r * 1.6f) / size.height).coerceIn(0.06f, 0.45f)
                val goldFade = Brush.verticalGradient(
                    0f to Gold.copy(alpha = 0f),
                    f to Gold,
                    1f - f to Gold,
                    1f to Gold.copy(alpha = 0f)
                )
                clipRect(right = r) {
                    drawRoundRect(
                        brush = goldFade, cornerRadius = cr,
                        topLeft = Offset(s3 / 2f, s3 / 2f),
                        size = Size(size.width - s3, size.height - s3),
                        style = Stroke(s3)
                    )
                }
            }
            .padding(16.dp)
    ) {
        Text(title, fontWeight = FontWeight.Bold, color = Gold, fontSize = 11.sp,
            letterSpacing = 2.sp)
        Spacer(Modifier.height(8.dp))
        Text(body, color = Cream, fontSize = 14.sp, lineHeight = 24.sp)
    }
}
