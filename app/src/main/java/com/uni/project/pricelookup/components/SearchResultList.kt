package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uni.project.pricelookup.R
import com.uni.project.pricelookup.models.SearchResult
import kotlinx.coroutines.launch

@Composable
fun SearchResultList(navigation: NavController, searchResultList: MutableState<SearchResult?>) {
    LazyColumn(
        modifier = Modifier.padding(top = 15.dp),
//        if(csabi.state.hallod){throw error('no.. I am asleep')}
        content = {
            items(searchResultList.value!!.searchResult) {item ->
                SearchResultCard(
                    imageModel = item,
                    productName = item.ProductName,
                    productMinPrice = item.Prices.minOf { price -> price.Price  },
                    navigation = navigation
                )

            }

        }
    )
}
