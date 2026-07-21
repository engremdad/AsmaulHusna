package com.islamic.asmaulhusna.ui

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
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
import com.islamic.asmaulhusna.notify.ReminderPrefs
import com.islamic.asmaulhusna.notify.ReminderScheduler
import com.islamic.asmaulhusna.notify.ReminderType
import com.islamic.asmaulhusna.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLanguageClick: () -> Unit,
    onRequestPermission: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { ReminderPrefs(context) }
    val currentLang = remember { LocaleStore.language(context) }

    val states = remember {
        mutableStateListOf(*ReminderType.entries.map {
            ReminderUi(it, prefs.isEnabled(it), prefs.hour(it), prefs.minute(it))
        }.toTypedArray())
    }

    fun apply(index: Int, enabled: Boolean, hour: Int, minute: Int) {
        val t = states[index].type
        states[index] = states[index].copy(enabled = enabled, hour = hour, minute = minute)
        prefs.set(t, enabled, hour, minute)
        if (enabled) {
            onRequestPermission()
            ReminderScheduler.schedule(context, t, hour, minute)
        } else {
            ReminderScheduler.cancel(context, t)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title), fontWeight = FontWeight.Bold) },
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
        Column(
            Modifier.padding(padding).starLattice().verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ── Language ──────────────────────────────────────────────
            SectionHeader(stringResource(R.string.section_language))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SectGround)
                    .border(1.dp, EmeraldLine, RoundedCornerShape(16.dp))
                    .clickable(onClick = onLanguageClick)
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Language, null, tint = Gold, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(14.dp))
                Text(stringResource(R.string.language_title), color = Cream, fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Text(currentLang.endonym, color = GoldSoft, fontSize = 14.sp)
                Spacer(Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = GoldDim)
            }

            Spacer(Modifier.height(6.dp))

            // ── Display ───────────────────────────────────────────────
            SectionHeader(stringResource(R.string.section_display))
            ToggleRow(
                title = stringResource(R.string.section_zikir),
                hint = stringResource(R.string.zikir_show_hint),
                checked = ZikirPrefs.isCounterShown(context),
                onToggle = { ZikirPrefs.setCounterShown(context, it) }
            )
            TextSizeRow(
                scale = TextScaleStore.scale(context),
                onDecrease = {
                    TextScaleStore.setScale(context, TextScaleStore.scale(context) - TextScaleStore.STEP)
                },
                onIncrease = {
                    TextScaleStore.setScale(context, TextScaleStore.scale(context) + TextScaleStore.STEP)
                }
            )

            Spacer(Modifier.height(6.dp))

            // ── Reminders ─────────────────────────────────────────────
            SectionHeader(stringResource(R.string.section_reminders))
            Text(stringResource(R.string.reminders_intro),
                color = CreamDim, fontSize = 13.sp, lineHeight = 20.sp)
            states.forEachIndexed { i, s ->
                ReminderRow(
                    ui = s,
                    onToggle = { on -> apply(i, on, s.hour, s.minute) },
                    onPickTime = {
                        TimePickerDialog(context, { _, h, m -> apply(i, s.enabled, h, m) },
                            s.hour, s.minute, true).show()
                    }
                )
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TextSizeRow(scale: Float, onDecrease: () -> Unit, onIncrease: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SectGround)
            .border(1.dp, EmeraldLine, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                stringResource(R.string.text_size_title),
                color = Cream, fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            StepperButton(
                icon = Icons.Filled.Remove,
                cd = stringResource(R.string.cd_text_smaller),
                enabled = scale > TextScaleStore.MIN,
                onClick = onDecrease
            )
            Text(
                "${(scale * 100).toInt()}%",
                color = GoldSoft, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.widthIn(min = 52.dp), textAlign = TextAlign.Center
            )
            StepperButton(
                icon = Icons.Filled.Add,
                cd = stringResource(R.string.cd_text_larger),
                enabled = scale < TextScaleStore.MAX,
                onClick = onIncrease
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            stringResource(R.string.text_size_sample),
            color = CreamDim, fontSize = 15.sp
        )
    }
}

@Composable
private fun ToggleRow(title: String, hint: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SectGround)
            .border(1.dp, EmeraldLine, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, color = Cream, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(hint, color = CreamDim, fontSize = 12.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = GoldInk,
                checkedTrackColor = Gold,
                checkedBorderColor = Gold,
                uncheckedThumbColor = CreamDim,
                uncheckedTrackColor = EmeraldRow,
                uncheckedBorderColor = EmeraldLine
            )
        )
    }
}

@Composable
private fun StepperButton(icon: androidx.compose.ui.graphics.vector.ImageVector, cd: String, enabled: Boolean, onClick: () -> Unit) {
    val tint = if (enabled) Gold else GoldDim
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(50))
            .border(1.dp, if (enabled) Gold else EmeraldLine, RoundedCornerShape(50))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, cd, tint = tint, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text.uppercase(),
        color = Gold, fontSize = 11.sp, letterSpacing = 2.5.sp, fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
    )
}

private data class ReminderUi(
    val type: ReminderType,
    val enabled: Boolean,
    val hour: Int,
    val minute: Int
)

@Composable
private fun ReminderRow(
    ui: ReminderUi,
    onToggle: (Boolean) -> Unit,
    onPickTime: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SectGround)
            .border(1.dp, EmeraldLine, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(stringResource(ui.type.labelRes), color = Cream, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(stringResource(ui.type.hintRes), color = CreamDim, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .border(1.dp, if (ui.enabled) Gold else EmeraldLine, RoundedCornerShape(50))
                    .clickable(enabled = ui.enabled, onClick = onPickTime)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Schedule, null, tint = if (ui.enabled) Gold else GoldDim,
                    modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(6.dp))
                Text("%02d:%02d".format(ui.hour, ui.minute),
                    color = if (ui.enabled) GoldSoft else GoldDim,
                    fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Switch(
            checked = ui.enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = GoldInk,
                checkedTrackColor = Gold,
                checkedBorderColor = Gold,
                uncheckedThumbColor = CreamDim,
                uncheckedTrackColor = EmeraldRow,
                uncheckedBorderColor = EmeraldLine
            )
        )
    }
}
