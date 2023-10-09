package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet

@OptIn(ExperimentalSheetApi::class)
@Composable
fun BottomSheet_library(
    detectedName: MutableState<String>, detectedPrice: MutableState<Int>, visible: Boolean
){
    Button(onClick = { /*visible = true*/ }) {
        Text(text = "Show modal sheet")
    }

    ModalSheet(
        visible = visible,
        onVisibleChange = { /*isible = it*/ },
    ) {
        Box(Modifier.height(200.dp))
    }
}