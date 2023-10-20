package com.uni.project.pricelookup.Views

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.navigation.*
import coil.compose.AsyncImage
import com.uni.project.pricelookup.HTTP
import com.uni.project.pricelookup.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.MutableState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.uni.project.pricelookup.components.CouponShapeLeftSide
import com.uni.project.pricelookup.components.CouponShapeRightSide
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import kotlin.reflect.typeOf

fun displayDate(dateFromDb: Date): String{
    val year = dateFromDb.year + 1900
    val month = dateFromDb.month + 1
    val day = dateFromDb.date

    return "${year.toString().slice(2..3)}'${month}.${day}"
}


@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalEncodingApi::class, ExperimentalFoundationApi::class)
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
    ) {
        if(isLoaded.value && product.value!=null){

            //ImageViewer
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    )
                    .height(300.dp)
                    .padding(top = 15.dp)
                    .fillMaxWidth()
            ){
                val bitmapList : MutableList<Bitmap> = mutableListOf()
                for (picture in product.value!!.Pictures) {
                    val decodedString =  Base64.decode(picture.image, 0)
                    val bitmap= BitmapFactory.decodeByteArray(decodedString, 0, decodedString!!.size)

                    bitmapList.add(bitmap)
                }
                val pagerState = rememberPagerState(
                    pageCount = { bitmapList.size }
                )
                val currentIndex = remember { mutableStateOf(1)}
                HorizontalPager(
                    state = pagerState,
                    key = null,
                ){
                    index ->
                    currentIndex.value = bitmapList.indexOf(bitmapList[index])
                    AsyncImage(
                        model = bitmapList[index],
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp, end = 10.dp)
                            .clip(
                                shape = RoundedCornerShape(
                                    topStart = 20.dp,
                                    topEnd = 20.dp
                                )
                            )
                        ,
                    )
                }
//                TODO - ha ilyet szeretnénk, ezen optimalizálni kell, mert gusztustalanul akad tőle az app
//                Row(
//                    Modifier
//                        .height(50.dp)
//                        .fillMaxWidth()
//                        .align(Alignment.BottomCenter),
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    repeat(bitmapList.count()) { iteration ->
//                        val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
//                        Box(
//                            modifier = Modifier
//                                .padding(2.dp)
//                                .clip(CircleShape)
//                                .background(color)
//                                .size(10.dp)
//
//                        )
//                    }
//                }

                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp, end = 20.dp)){
                        Button(enabled = false, modifier = Modifier.alpha(20f), onClick = { /*not needed*/ }, shape = RoundedCornerShape(20.dp),
                            content = {
                                Text(text = "${pagerState.currentPage+1}/${bitmapList.count()}")
                            },
                        )
                    }
                }
            }

            //Title
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Prices of '" + product!!.value!!.ProductName + "'",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(top = 20.dp, start = 20.dp)
                )
            }

            //Prices
            LazyColumn(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                items(product.value!!.Prices) { item ->
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .shadow(elevation = 2.dp, shape = CouponShapeLeftSide())
                                .size(250.dp, 115.dp)
                                .clip(shape = CouponShapeLeftSide())
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .padding(5.dp)
                        ){
                            Box (
                                contentAlignment = Alignment.TopStart,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                            ){
                                Text(
                                    text = "${item.ShopName}:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            Box(
                                contentAlignment = Alignment.BottomStart,
                                modifier = Modifier.fillMaxSize()
                            ){
                                Text(
                                    text = "Updated at: ${displayDate(item.updatedAt)}"
                                )
                            }
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .shadow(elevation = 2.dp, shape = CouponShapeRightSide())
                                .size(115.dp)
                                .clip(shape = CouponShapeRightSide())
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                .padding(5.dp)
                        ){
                            Text(
                                text = "${item.Price} FT",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }


}