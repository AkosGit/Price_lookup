package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet_google(detectedName: MutableState<String>, detectedPrice:MutableState<Int> ) {
    BottomSheetScaffold(
        sheetSwipeEnabled = true,
        sheetPeekHeight = 100.dp,
        sheetDragHandle = { BottomSheetDefaults.DragHandle()  },
        sheetContent = {
            Column(

            ) {
                Row(

                ) {
                    Text(
                        text = "Detected product name:",

                    )
                    TextField(value = detectedName.value, onValueChange = {detectedName.value=it})
                }
                Row(

                ) {
                    Text(
                        text = "Detected price:",
                    )
                    TextField(value = detectedPrice.value.toString(), onValueChange = {detectedPrice.value=it.toInt()})
                }

                Row(

                ) {
                    Text(
                        text = "Detected price:",
                    )
                    TextField(value = detectedPrice.value.toString(), onValueChange = {detectedPrice.value=it.toInt()})
                }
                Row(

                ) {
                    Text(
                        text = "Detected price:",
                    )
                    TextField(value = detectedPrice.value.toString(), onValueChange = {detectedPrice.value=it.toInt()})
                }
                Row(

                ) {
                    Text(
                        text = "Detected price:",
                    )
                    TextField(value = detectedPrice.value.toString(), onValueChange = {detectedPrice.value=it.toInt()})
                }
            }
        }
    ) {
        Text(text = "dsdsds")
    }
}