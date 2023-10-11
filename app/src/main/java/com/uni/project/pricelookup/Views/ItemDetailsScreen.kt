package com.uni.project.pricelookup.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.uni.project.pricelookup.R
import com.uni.project.pricelookup.components.selecRandomImg
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

//import com.uni.project.pricelookup.components.OutlinedSearchBar
//import com.uni.project.pricelookup.components.SearchBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ItemDetailsScreen(navigation: NavController,itemId:String?) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp))
                .height(200.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)


        ){
                Image(

                    painter = painterResource(id = selecRandomImg(R.drawable.chocolate_bar5)),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp)) //not workiing csaaabi heeelp
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
                text = "Title of the item",
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
           Column(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(20.dp)//not working
           ) {
               Row {
                   Text(text = "Aldi:", color = Color.Black)
                   Text(text = "5 FT", color = Color.Black)
               }
               Row {
                   Text(text = "Spar:",color = Color.Black)
                   Text(text = "10 FT", color = Color.Black)
               }
               Row {
                   Text(text = "Lidl:",color = Color.Black)
                   Text(text = "9.5 FT",color = Color.Black)
               }

           }
        }

    }


}