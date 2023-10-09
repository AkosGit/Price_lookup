package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDropDown_google(shop: MutableState<String>){
    val isExpanded= remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        modifier = Modifier
            .height(100.dp)
            .width(100.dp),
        expanded = isExpanded.value,
        onExpandedChange = {
            isExpanded.value=!isExpanded.value
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(text = "Spar")
            },
            onClick = {
                shop.value = "Spar"
                isExpanded.value = false
            }
        )

        DropdownMenuItem(
            text = {
                Text(text = "Lidl")
            },
            onClick = {
                shop.value = "Lidl"
                isExpanded.value = false
            }
        )

        DropdownMenuItem(
            text = {
                Text(text = "Aldi")
            },
            onClick = {
                shop.value = "Aldi"
                isExpanded.value = false
            }
        )
        DropdownMenuItem(
            text = {
                Text(text = "Auchan")
            },
            onClick = {
                shop.value = "Auchan"
                isExpanded.value = false
            }
        )

    }
}