package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDropDown_library(shop: MutableState<String>){
    val shops = mutableListOf(Shop(name = "Spar", emoji = "\uD83D\uDCB5"),Shop(name = "Aldi", emoji = "\uD83D\uDCB6"),Shop(name = "Lidl", emoji = "\uD83D\uDCB7"),Shop(name = "Auchan", emoji = "\uD83D\uDCB3"))
    val keyboardController = LocalSoftwareKeyboardController.current
    SearchableExpandedDropDownMenu(
        listOfItems = shops,
        modifier = Modifier.fillMaxWidth(),
        onDropDownItemSelected = { item ->
            shop.value=item.name
        },
        dropdownItem = { test ->
            DropDownItem(test = test)
        },
        defaultItem = {

        },
        onSearchTextFieldClicked = {
            keyboardController?.show()
        }
    )

}
@Composable
fun DropDownItem(test: Shop) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize(),
    ) {
        Text(text = test.emoji)
        Spacer(modifier = Modifier.width(12.dp))
        Text(test.name)
    }
}
data class Shop(
    val name: String,
    val emoji: String,
) {
    override fun toString(): String {
        return "$emoji $name"
    }
}