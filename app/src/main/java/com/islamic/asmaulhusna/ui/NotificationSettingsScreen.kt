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
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamic.asmaulhusna.notify.ReminderPrefs
import com.islamic.asmaulhusna.notify.ReminderScheduler
import com.islamic.asmaulhusna.notify.ReminderType
import com.islamic.asmaulhusna.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    onBack: () -> Unit,
    onRequestPermission: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { ReminderPrefs(context) }

    // Local UI state seeded from prefs; each change persists and (re)schedules immediately.
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
                title = { Text("রিমাইন্ডার", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "ফিরে যান")
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
            Modifier.padding(padding).starLattice().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "সময়মতো স্মরণ করিয়ে দিতে রিমাইন্ডার চালু করুন। সাহরি ও ইফতারের সময় " +
                    "আপনার স্থানীয় সময় অনুযায়ী নিজে ঠিক করে নিন।",
                color = CreamDim, fontSize = 13.sp, lineHeight = 20.sp
            )
            states.forEachIndexed { i, s ->
                ReminderRow(
                    ui = s,
                    onToggle = { on -> apply(i, on, s.hour, s.minute) },
                    onPickTime = {
                        TimePickerDialog(
                            context,
                            { _, h, m -> apply(i, s.enabled, h, m) },
                            s.hour, s.minute, true
                        ).show()
                    }
                )
            }
        }
    }
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
            Text(ui.type.label, color = Cream, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text(ui.type.hint, color = CreamDim, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .border(1.dp, if (ui.enabled) Gold else EmeraldLine, RoundedCornerShape(50))
                    .clickable(enabled = ui.enabled, onClick = onPickTime)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Schedule, null,
                    tint = if (ui.enabled) Gold else GoldDim,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "%02d:%02d".format(ui.hour, ui.minute),
                    color = if (ui.enabled) GoldSoft else GoldDim,
                    fontSize = 14.sp, fontWeight = FontWeight.SemiBold
                )
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
