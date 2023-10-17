package com.uni.project.pricelookup.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    val decodedString = item?.Pictures?.get(0)?.let { Base64.decode(it.image, 0) }
    val bitmap= BitmapFactory.decodeByteArray(decodedString, 0, decodedString!!.size)
    ElevatedCard(
        modifier = Modifier
            .padding(all = 1.dp)
            .clickable {
                if (item != null) {
                    navigation?.navigate("ItemDetailsScreen/{id}".replace("{id}",item.id.toString()))
                }
            },

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
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
}

fun selecRandomImg(photo: Int): Int{
    return if (photo != R.drawable.chocolate_bar1) photo
    else{
        val randomInt = (0..8).random()

        when (randomInt) {
            1 -> { R.drawable.chocolate_bar1 }
            2 -> { R.drawable.chocolate_bar2 }
            3 -> { R.drawable.chocolate_bar3 }
            4 -> { R.drawable.chocolate_bar4 }
            5 -> { R.drawable.chocolate_bar5 }
            6 -> { R.drawable.chocolate_bar6 }
            else -> {
                R.drawable.chocolate_bar7
            }
        }
    }
}
@Preview
@Composable
fun TestProductPreviewCard(){
    ProductPreviewCard(navigation = null, item = null)
}


