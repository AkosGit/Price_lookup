package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.uni.project.pricelookup.R

@Composable
fun SearchResultList(searchResultList: List<String>) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = 5.dp)
            .clip(shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            )),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
        ),

        content = {
            LazyColumn(
                content = {
                    items(searchResultList.count()){
                        SearchResultCard(
                            imageModel = R.drawable.chocolate_bar1,
                            productName = "Twix csoki",
                            productMinPrice =100
                        )
                    }
                }
            )
        }
    )

}