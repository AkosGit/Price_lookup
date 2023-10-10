package com.uni.project.pricelookup.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import coil.compose.AsyncImage
import com.uni.project.pricelookup.MainActivity
import com.uni.project.pricelookup.PreferencesManager
import com.uni.project.pricelookup.components.PhotoGrid
import com.uni.project.pricelookup.components.SearchResultCard
import com.uni.project.pricelookup.components.SearchResultList
import com.uni.project.pricelookup.components.SearchWidget

@Composable
fun SearchScreen(navigation: NavController, query:String?){

    val searchText= remember {
        mutableStateOf("")
    }
    val context=LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    MainActivity.CleanUpImages(context)

//    SearchResultCard(imageModel ="https://csokizzz.hu/img/9360/7622201803834/7622201803834.jpg" , productName = "milka csoki", productMinPrice =100 )

    val list = arrayListOf<String>()
    for (i in 1..12) {
        list.add("asd")
    }

    Column {
        Card(
            modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ){
            Text(
                text = "Products for ${searchText.value}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = 6.dp, top = 10.dp, end = 6.dp),
                fontWeight = FontWeight.Bold
            )

            SearchResultList(searchResultList = list)
        }
    }

}



