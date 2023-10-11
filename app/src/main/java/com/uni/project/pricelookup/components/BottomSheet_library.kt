package com.uni.project.pricelookup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        backgroundColor = Color.Red
    ) {
        Box(
            Modifier
                .height(200.dp)
                .background(color = Color.Green),
            contentAlignment = Alignment.Center

        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                ) {
                    Text(
                        text = "Detected product name:",

                        )
                    TextField(value = detectedName.value, onValueChange = { detectedName.value = it })
                }
                Row(

                ) {
                    Text(
                        text = "Detected price:",
                    )
                    TextField(
                        value = priceString.value,
                        onValueChange = {
                            if(it==""){
                                priceString.value=""
                                detectedPrice.value=0
                            }
                            else{
                                val num = it.toIntOrNull()
                                if (num != null) {
                                    detectedPrice.value = it.toInt()
                                    priceString.value = it
                                }
                            }

                        })
                }
            }
        }
    }
}