package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDropDown_google(shop: MutableState<String>){
    val isExpanded= remember {
        mutableStateOf(false)
    }
    val shops = listOf("Spar","Lidl","Aldi","Auchan")
    var dropDownWidth by remember {
        mutableStateOf(0)
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded.value,
        onExpandedChange = {
            isExpanded.value=!isExpanded.value
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .semantics {
                    contentDescription = "TextField"
                }
                .onSizeChanged {
                    dropDownWidth = it.width
                }
                .padding(bottom = 5.dp)
                .fillMaxWidth()
                .menuAnchor(),

            label = {
                Text(text = "Shop")
            },
            shape = RoundedCornerShape(
                bottomStart = 20.dp,
                bottomEnd = 20.dp
            ),
            readOnly = true,
            value = shop.value,
            onValueChange = {},
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ),
        )
        ExposedDropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false },
            modifier = Modifier
                .clip(shape = RoundedCornerShape(
                    topStart = 30.dp, topEnd = 30.dp
                ))
                .width(with(LocalDensity.current) { dropDownWidth.toDp() })

        ) {
            shops.forEach{
                DropdownMenuItem(
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.primary
                    ),
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