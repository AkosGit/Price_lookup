package com.uni.project.pricelookup.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.uni.project.pricelookup.R

@Preview
@Composable
fun ProductPreviewCard(photo:Int = R.drawable.chocolate_bar1) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 1.dp)
//            .size(width = 150.dp, height = 200.dp)
            .clickable {
                // TODO : to be implemented
            },

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Image(
            modifier = Modifier
                .width(width = 250.dp)
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

