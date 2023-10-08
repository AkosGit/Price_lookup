package com.uni.project.pricelookup.Views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import coil.compose.AsyncImage
import com.uni.project.pricelookup.components.SearchResultCard
import com.uni.project.pricelookup.components.SearchWidget

@Composable
fun SearchScreen(navigation: NavController, query:String?){
    //Text(text = query.toString())
    val searchText= remember {
        mutableStateOf("")
    }
    SearchWidget(
        text = searchText.value,
        onTextChange = {
            searchText.value=it
            ;
        },
        onSearchClicked = {

        },
        onCloseClicked = {
            searchText.value=""
        }
    )
    SearchResultCard(imageModel ="https://csokizzz.hu/img/9360/7622201803834/7622201803834.jpg" , productName = "milka csoki", productMinPrice =100 )

}



