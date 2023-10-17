package com.uni.project.pricelookup.Views

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import coil.compose.AsyncImage
import com.uni.project.pricelookup.HTTP
import com.uni.project.pricelookup.MainActivity
import com.uni.project.pricelookup.PreferencesManager
import com.uni.project.pricelookup.components.SearchResultList
import com.uni.project.pricelookup.models.SearchResult
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun SearchScreen(navigation: NavController, query:String?){

    val searchText= remember {
        mutableStateOf("")
    }
    val context=LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    MainActivity.CleanUpImages(context)
    val isLoaded = remember {
        mutableStateOf(false)
    }
    var results = remember {
        mutableStateOf<SearchResult?>(null)
    }
    val client=HTTP()
    CoroutineScope(Dispatchers.IO).launch {
        client.searchProductByname(
            query!!,{
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
        if(isLoaded.value){
            Card(
                modifier = Modifier.padding(start = 5.dp, end = 5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ){
                Text(
                    text = "Products for ${searchText.value}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(start = 6.dp, top = 10.dp, end = 6.dp),
                    fontWeight = FontWeight.Bold
                )
                if(results.value!=null){
                    SearchResultList(searchResultList = results, navigation = navigation)
                }
            }
        }
        else if(isFailed.value){
            Text(text = "Nol items found!")
        }
        else{
            NetworkError()
        }

    }

}



