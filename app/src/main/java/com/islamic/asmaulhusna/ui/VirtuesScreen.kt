package com.islamic.asmaulhusna.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamic.asmaulhusna.R
import com.islamic.asmaulhusna.ui.theme.*

/**
 * The virtues (Fazilat) of learning and reciting the 99 Names — an overview
 * with authentic Hadith references and practical guidance for daily life.
 * Content lives entirely in string resources, so it is fully localized.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VirtuesScreen(onBack: () -> Unit) {
    // Ordered (heading, body) sections, rendered as a scroll of framed cards.
    val sections = listOf(
        R.string.virtues_s1_title to R.string.virtues_s1_body,
        R.string.virtues_s2_title to R.string.virtues_s2_body,
        R.string.virtues_s3_title to R.string.virtues_s3_body,
        R.string.virtues_s4_title to R.string.virtues_s4_body,
        R.string.virtues_s5_title to R.string.virtues_s5_body,
        R.string.virtues_s6_title to R.string.virtues_s6_body,
        R.string.virtues_s7_title to R.string.virtues_s7_body,
        R.string.virtues_s8_title to R.string.virtues_s8_body,
    )

    Box(Modifier.mushafGround()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.virtues_title), fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.cd_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Gold,
                    navigationIconContentColor = Gold
                )
            )
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Spacer(Modifier.height(6.dp))
            Text(
                stringResource(R.string.virtues_subtitle),
                color = GoldSoft, fontSize = 13.sp, letterSpacing = 1.sp,
                fontWeight = FontWeight.SemiBold
            )
            sections.forEachIndexed { i, (titleRes, bodyRes) ->
                VirtueCard(
                    index = i + 1,
                    title = stringResource(titleRes),
                    body = stringResource(bodyRes)
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
    }
}

@Composable
private fun VirtueCard(index: Int, title: String, body: String) {
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
                "%02d".format(index),
                color = GoldDim, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(Modifier.width(10.dp))
            Text(
                title,
                color = Gold, fontSize = 15.sp, fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(body, color = Cream, fontSize = 14.sp, lineHeight = 24.sp)
    }
}
