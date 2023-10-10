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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.uni.project.pricelookup.R

@Composable
fun SearchResultList(searchResultList: List<String>) {

    LazyColumn(
        modifier = Modifier.padding(top = 15.dp),

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