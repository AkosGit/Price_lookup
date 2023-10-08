package com.uni.project.pricelookup.Views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.PlusOne
import androidx.compose.material.icons.rounded.Sailing
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uni.project.pricelookup.PreferencesManager

//import com.uni.project.pricelookup.components.OutlinedSearchBar
//import com.uni.project.pricelookup.components.SearchBar

@OptIn(ExperimentalComposeUiApi::class)

@Composable
fun ItemEditScreen(navigation: NavController) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    //getting barcode photo
    val photoBarCode=preferencesManager.getData("photoLoc","")


    val photoPlusSign= Icons.Rounded.PlusOne


    var photoProduct= remember {
        mutableStateOf("")
    }

    var photoMain= remember {
        mutableStateOf(photoBarCode)
    }

    Column(

        Modifier.onFocusChanged {
            val photoLocProduct = preferencesManager.getData("product", "")
            if (photoLocProduct != "") {
                photoProduct.value = photoLocProduct

            }
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()

        )
        {
            AsyncImage(
                model = photoMain.value,
                contentDescription = null,
                modifier = Modifier
                    .padding(20.dp)
                    .height(300.dp)
                    .fillMaxWidth()
            )
        }
        //price tag photo
        Row(
            Modifier.padding(top = 30.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ){
                Text(text = "Photo of pricetag:")
            }
            Box(
            )
            {
                AsyncImage(
                    model  = photoBarCode,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(5.dp)
                        .height(80.dp)
                        .clickable {
                            photoMain.value=photoBarCode
                        }
                )
            }
        }
        //product picture
        Row(
            Modifier.padding(top = 30.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ){
                Text(text = "Photo of product:")
            }
            Box(

            )
            {
                if(photoProduct.value==""){

                    Icon(
                        imageVector = photoPlusSign,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.background, //to be
                        modifier= Modifier
                            .padding(5.dp)
                            .height(80.dp)
                            .clickable {
                                navigation.navigate("ProductCameraView")
                            }
                    )
                }
                else{
                    AsyncImage(
                        model  = photoProduct.value,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)
                            .height(80.dp)
                            .clickable {
                                photoMain.value=photoProduct.value
                            }
                    )
                }

                }

        }


    }
}

