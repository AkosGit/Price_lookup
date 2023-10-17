package com.uni.project.pricelookup.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import com.uni.project.pricelookup.HTTP
import com.uni.project.pricelookup.components.NetworkError
import com.uni.project.pricelookup.components.PhotoGrid
import com.uni.project.pricelookup.models.SearchResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainPage(navigation: NavController) {
    val isLoaded = remember {
        mutableStateOf(false)
    }
    val isNetworkError = remember {
        mutableStateOf(false)
    }
    val isFailed = remember {
        mutableStateOf(false)
    }
    val results = remember {
        mutableStateOf<SearchResult?>(null)
    }
    val client= HTTP()
    CoroutineScope(Dispatchers.IO).launch {
        client.getRecommendations({
                //onSuccess
                results.value=it
                isLoaded.value=true
                this.cancel("Fuck you")
            }, {
                //onFailure
                isFailed.value=true
                this.cancel("Fuck you")
            },{
                //onNetworkError
                isNetworkError.value=true
                this.cancel("Fuck you")
            }
        )
    }



    Column {
        Card(
            modifier = Modifier.padding(start = 5.dp, end = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ){
            Text(
                text = "Products that might be of interest",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = 6.dp, top = 10.dp, end = 6.dp),
                fontWeight = Bold
            )
            if(isLoaded.value){
                PhotoGrid(recommendedItems = results.value!!,navigation)
            }
            if(isFailed.value){
                Text(text = "No products can be found")
            }
            if(isNetworkError.value){
                Text(text = "A network error has occured! :(")
                NetworkError()
            }

        }


    }


    }

