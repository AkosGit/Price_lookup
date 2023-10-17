package com.uni.project.pricelookup.Views

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import coil.compose.AsyncImage
import com.uni.project.pricelookup.HTTP
import com.uni.project.pricelookup.R
import com.uni.project.pricelookup.components.selecRandomImg
import com.uni.project.pricelookup.models.ItemId
import com.uni.project.pricelookup.models.Price
import com.uni.project.pricelookup.models.Product
import com.uni.project.pricelookup.models.SearchResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalEncodingApi::class)
@Composable
fun ItemDetailsScreen(navigation: NavController,itemId:String?) {
    val context = LocalContext.current
    val isLoaded = remember {
        mutableStateOf(false)
    }
    val isNetworkError = remember {
        mutableStateOf(false)
    }
    val isFailed = remember {
        mutableStateOf(false)
    }
    val product = remember {
        mutableStateOf<Product?>(null)
    }
    val client= HTTP()
    CoroutineScope(Dispatchers.IO).launch {
        client.getProduct(ItemId = itemId!!,{
                //onSuccess
                product.value=it
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(isLoaded.value && product.value!=null){
            //TODO: Imageviewer (at this time only one image can be display)
            val decodedString =  Base64.decode(product.value!!.Pictures[0].image, 0)
            val bitmap= BitmapFactory.decodeByteArray(decodedString, 0, decodedString!!.size)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                    .height(200.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)


            ){
                AsyncImage(
                    model = bitmap,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(
                                bottomEnd = 10.dp,
                                bottomStart = 10.dp
                            )
                        ) //not workiing csaaabi heeelp
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, bottom = 30.dp),
                )

            }

            //Title
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = product!!.value!!.ProductName,
                    modifier = Modifier
                        .padding(top = 60.dp))
            }

            Spacer(
                modifier = Modifier
                    .padding(end = 55.dp, top = 3.dp, bottom = 10.dp)
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .fillMaxWidth()
                    .height(3.dp)

            )

            //Prices
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 1.dp))
                    .background(MaterialTheme.colorScheme.onSecondaryContainer)
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 12.dp) // not workiing csaaabi heeelp
            ) {

                LazyColumn(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)//not working
                )
                {
                    items(product.value!!.Prices) { item ->
                        Row {
                            Text(text = "${item.ShopName}:", color = Color.Black)
                            Text(text = "${item.Price} FT", color = Color.Black)
                        }
                    }


                }
            }
        }


    }


}