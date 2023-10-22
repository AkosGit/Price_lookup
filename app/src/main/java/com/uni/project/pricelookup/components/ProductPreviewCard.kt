package com.uni.project.pricelookup.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uni.project.pricelookup.R
import com.uni.project.pricelookup.models.Product
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalEncodingApi::class)
@Composable
fun ProductPreviewCard(photo:Int = R.drawable.chocolate_bar1, navigation: NavController?,item:Product?) {
    //TODO: display base picture

    ElevatedCard(
        modifier = Modifier
            .padding(all = 1.dp)
            .clickable {
                if (item != null) {
                    navigation?.navigate("ItemDetailsScreen/{id}".replace("{id}", item.id.toString()))
                }
            },

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        if (item?.Pictures?.count() != 0){
            val decodedString = item?.Pictures?.get(0)?.let { Base64.decode(it.image, 0) }
            val bitmap= BitmapFactory.decodeByteArray(decodedString, 0, decodedString!!.size)
            AsyncImage(
                modifier = Modifier
                    .width(200.dp)
                    .height(130.dp)
                    .clip(shape = RoundedCornerShape(size = 12.dp)),

                model = bitmap,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        else{
            Box(

            ){
                Image(
                    painterResource(R.mipmap.ic_launcher_foreground),
                    modifier = Modifier
                        .width(200.dp)
                        .height(130.dp)
                        .clip(shape = RoundedCornerShape(size = 12.dp)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, end = 20.dp)
                        .alpha(20f)
                ){
                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 8.dp)
                    ){
                        Text(text = "No photo from product")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun TestProductPreviewCard(){
    ProductPreviewCard(navigation = null, item = null)
}


