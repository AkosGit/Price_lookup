package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentPasteSearch
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
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
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
                        .clip(shape = RoundedCornerShape(size = 1.dp))
                    ,
                )

                Column {
                    Box(
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column() {
                            Text(
                                text = productName,
                            )
                            Text(
                                text = "Minimális ár: $productMinPrice",
                            )
                        }
                    }

                    Box(
                        content = {
                            Button(
                                onClick = {
                                    // TODO -> this should take us to the ProductDetails
                                },
                                colors = ButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary,
                                    disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                                    disabledContentColor = MaterialTheme.colorScheme.inversePrimary
                                ),
                                content = {
                                    Icon(
                                        Icons.Rounded.ContentPasteSearch,
                                        contentDescription = "Localized description",
                                    )
                                }
                            )
                        }
                    )
                }



            }
        }
    )
}