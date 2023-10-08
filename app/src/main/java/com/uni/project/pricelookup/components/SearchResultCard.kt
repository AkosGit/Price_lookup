package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SearchResultCard(
    imageModel:Any,
    productName:String,
    productMinPrice:Int
){
    Card(elevation = CardDefaults.cardElevation(
        defaultElevation = 10.dp
    ), modifier = Modifier
        .padding(5.dp)
        .height(150.dp)
    )
    {
        Row{
            AsyncImage(
                model = imageModel,
                contentDescription = null,
                modifier = Modifier
                    .padding(3.dp)
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(3.dp)) {
                    Text(text = productName)
                    Text(text = "Minimális ár: $productMinPrice")
                }
                
            }

        }
    }
}