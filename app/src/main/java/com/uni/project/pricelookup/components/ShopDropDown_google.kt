package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
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
    val shops = listOf("Spar","Lidl","Aldi","Auchan")
    ExposedDropdownMenuBox(
        modifier = Modifier
            //.height(20.dp)
            //.width(20.dp)
        ,
        expanded = isExpanded.value,
        onExpandedChange = {
            isExpanded.value=!isExpanded.value
        }
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = shop.value,
            onValueChange = {},
            //trailingIcon = ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded.value),
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
        ) {
            shops.forEach{
                DropdownMenuItem(
                    text = {
                        Text(text = it)
                    },
                    onClick = {
                        shop.value = it
                        isExpanded.value = false
                    }
                )
            }

        }

    }
}