package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uni.project.pricelookup.R

@Composable
fun SearchResultList(navigation: NavController, searchResultList: List<String>) {

    LazyColumn(
        modifier = Modifier.padding(top = 15.dp),

        content = {
            items(searchResultList.count()){
                SearchResultCard(
                    imageModel = R.drawable.chocolate_bar1,
                    productName = "Twix csoki",
                    productMinPrice =100,
                    navigation =navigation
                )
            }
        }
    )
}