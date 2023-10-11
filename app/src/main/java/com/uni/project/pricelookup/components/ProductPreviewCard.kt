package com.uni.project.pricelookup.components

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
import com.uni.project.pricelookup.R


@Composable
fun ProductPreviewCard(photo:Int = R.drawable.chocolate_bar1, navigation: NavController?) {
    ElevatedCard(
        modifier = Modifier
            .padding(all = 1.dp)
            .clickable {
                navigation?.navigate("ItemDetailsScreen/{id}".replace("{id}","1"))
            },

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Image(
            modifier = Modifier
                .width(200.dp)
                .height(130.dp)
                .clip(shape = RoundedCornerShape(size = 12.dp)),

            painter = painterResource(id = selecRandomImg(photo)),
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
    ProductPreviewCard(navigation = null)
}


