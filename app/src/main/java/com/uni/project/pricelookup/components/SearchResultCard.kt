package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SearchResultCard(
    imageModel:Any,
    productName:String,
    productMinPrice:Int
){
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = Modifier
            .padding(12.dp, 5.dp),

        content = {
            Row{
                AsyncImage(
                    model = imageModel,
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .height(130.dp)
                        .clip(shape = RoundedCornerShape(size = 3.dp)),
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.padding(3.dp)) {
                        Text(
                            text = productName,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Minimális ár: $productMinPrice",
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                }

            }
        }
    )
}