package com.uni.project.pricelookup.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.uni.project.pricelookup.R
import com.uni.project.pricelookup.models.SearchResult
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun SearchResultList(navigation: NavController, searchResultList: MutableState<SearchResult?>) {
    LazyColumn(
        modifier = Modifier.padding(top = 15.dp),
//        if(csabi.state.hallod){throw error('no.. I am asleep')}
        content = {
            items(searchResultList.value!!.searchResult) {item ->
                val decodedString = Base64.decode(item.Pictures[0].image, 0)
                val bitmap= BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

                SearchResultCard(
                    productName = item.ProductName,
                    imageModel = bitmap,
                    productMinPrice = item.Prices.minOf { price -> price.Price  },
                    navigation = navigation
                )

            }

        }
    )
}
