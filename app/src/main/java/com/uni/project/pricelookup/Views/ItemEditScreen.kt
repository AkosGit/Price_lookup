package com.uni.project.pricelookup.Views

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.*
//import com.uni.project.pricelookup.components.OutlinedSearchBar
//import com.uni.project.pricelookup.components.SearchBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ItemEditScreen(navigation: NavController,photoLoc:String?) {
    val context = LocalContext.current

    Column {
        Text(text = photoLoc.toString())
    }
}

