package com.uni.project.pricelookup.Views

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import com.uni.project.pricelookup.components.PhotoGrid
//import com.uni.project.pricelookup.components.OutlinedSearchBar
//import com.uni.project.pricelookup.components.SearchBar
import com.uni.project.pricelookup.components.SearchWidget
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainPage(navigation: NavController) {
    val context= LocalContext.current
    val searchText= remember {
        mutableStateOf("")
    }
    val list = arrayListOf<String>()
    for (i in 1..20) {
        list.add("asd")
    }

    Column {
        Card(
            modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray.copy(alpha = 0.1f)
            )
        ){
            Text(
                text = "Products that might be of interest",
                modifier = Modifier
                    .padding(start = 6.dp, top = 10.dp, end = 6.dp),
                fontWeight = Bold
            )
            PhotoGrid(recommendedItems = list)
        }


    }


    }

