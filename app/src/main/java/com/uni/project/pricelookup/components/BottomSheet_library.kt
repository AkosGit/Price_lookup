package com.uni.project.pricelookup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.text.Text.Line
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSheetApi::class)
@Composable
fun BottomSheet_google(detectedName: MutableState<String>, detectedPrice:MutableState<Int>,visible: MutableState<Boolean> ) {
    val priceString = remember {
        mutableStateOf("0")
    }
    ModalSheet(
        visible = visible.value,
        onVisibleChange = { visible.value = it },
        backgroundColor = Color.Red,
        elevation = 8.dp
    ) {
        Box(
            Modifier
                .height(300.dp)
                .background(MaterialTheme.colorScheme.primaryContainer)
            ,
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(15.dp)
            ) {
                OutlinedTextField(
                    value = detectedName.value,
                    label = {
                        Text(
                            text = "Detected product name:",
                        )
                    },
                    onValueChange = { detectedName.value = it },

                    singleLine = true,
                    shape = RoundedCornerShape(size = 30.dp),
                    modifier = Modifier
                        .semantics {
                            contentDescription = "TextField"
                        }
                        .padding(bottom = 5.dp)
                        .fillMaxWidth()
                        .scale(.83f)
                    ,
                )

                OutlinedTextField(
                    value = priceString.value,
                    label = {
                        Text(
                            text = "Detected price:",
                        )
                    },
                    suffix = {
                        Text(text = " Ft")
                    },
                    onValueChange = {
                        if (it == "") {
                            priceString.value = ""
                            detectedPrice.value = 0
                        } else {
                            val num = it.toIntOrNull()
                            if (num != null) {
                                detectedPrice.value = it.toInt()
                                priceString.value = it
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(size = 30.dp),
                    modifier = Modifier
                        .semantics {
                            contentDescription = "TextField"
                        }
                        .padding(bottom = 5.dp)
                        .fillMaxWidth()
                        .scale(.83f),
                )
            }
        }
    }
}