package com.uni.project.pricelookup.Views

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.*
//import com.uni.project.pricelookup.components.OutlinedSearchBar
//import com.uni.project.pricelookup.components.SearchBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ItemDetailsScreen(navigation: NavController,itemId:String?) {
    val context = LocalContext.current
    val searchText = remember {
        mutableStateOf("")
    }

    Column {


    }
}