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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uni.project.pricelookup.R

@Composable
fun SearchResultCard(
    imageModel:Any,
    productName:String,
    productMinPrice:Int,
    navigation: NavController?
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
        shape = RoundedCornerShape(
            topStart = 30.dp,
            topEnd = 0.dp,
            bottomEnd = 0.dp,
            bottomStart = 30.dp
        ),

        content = {
            Row{
                AsyncImage(
                    model = imageModel,
                    contentDescription = null,
                    alignment = Alignment.CenterStart,
                    modifier = Modifier
                        .width(200.dp)
                        .height(130.dp)
                        .clip(shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 30.dp,
                            bottomEnd = 30.dp,
                            bottomStart = 30.dp
                        ))
                    ,
                )

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    Text(
                        text = productName,
                    )
                    Text(
                        text = "Minimális ár: $productMinPrice",
                    )

                    ElevatedButton(
                        onClick = {
                            navigation?.navigate("ItemDetailsScreen/{id}".replace("{id}","1"))
                        },
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                            disabledContentColor = MaterialTheme.colorScheme.inversePrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp
                        ),
                        content = {
                            Icon(
                                Icons.Rounded.ContentPasteSearch,
                                contentDescription = "Localized description",
                            )
                        }
                    )
                }
//                Box(
//                    contentAlignment = Alignment.CenterStart,
//
//                ) {
//
//                }

            }
        }
    )
}



@Preview
@Composable
fun SearchResultCardPreview(){
    SearchResultCard(
        imageModel = R.drawable.chocolate_bar1,
        productName = "Twix csoki",
        productMinPrice =100,
        navigation = null
    )
}