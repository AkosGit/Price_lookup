package com.uni.project.pricelookup.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlusOne
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.uni.project.pricelookup.PreferencesManager
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.uni.project.pricelookup.components.BottomSheet_google
import com.uni.project.pricelookup.components.ShopDropDown_google
import eu.wewox.modalsheet.ExperimentalSheetApi
import kotlinx.coroutines.*
import androidx.compose.material.icons.rounded.CameraEnhance
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    DelicateCoroutinesApi::class, ExperimentalSheetApi::class
)
@Composable
fun ItemEditScreen(navigation: NavController) {

    //setup ocr data edit
    var detectedName= remember {
        mutableStateOf("")
    }
    var detectedPrice= remember {
        mutableStateOf(0)
    }

    //getting barcode photo
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val photoBarCode=preferencesManager.getData("photoLoc","")


    val photoPlusSign=androidx.compose.material.icons.Icons.Rounded.CameraEnhance


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
        val smallPhotoModifier=Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .width(80.dp)
            .height(80.dp)
        val smallPhotoTextModifier=Modifier
            .width(150.dp)
            .height(80.dp)
        //main photo
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
                    .height(200.dp)
                    .fillMaxWidth()
            )
        }
        //price tag photo
        Row(
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = smallPhotoTextModifier
            ) {
                Text(text = "Photo of pricetag:")
            }
            Box(
                contentAlignment =Alignment.Center,
                modifier = smallPhotoModifier
            )
            {
                AsyncImage(
                    model = photoBarCode,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxSize()
                        //.height(80.dp)
                        .clickable {
                            photoMain.value = photoBarCode
                        }
                )
            }
        }
        //product picture
        Row(
            Modifier.padding(top = 10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = smallPhotoTextModifier
            ) {
                Text(text = "Photo of product:")
            }
            Box(

                contentAlignment =Alignment.Center,
                modifier = smallPhotoModifier

            )
            {
                if (photoProduct.value == "") {

                    Icon(
                        imageVector = photoPlusSign,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.background, //to be
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxSize()
                            .clickable {
                                navigation.navigate("ProductCameraView")
                            }
                    )
                } else {
                    AsyncImage(
                        model = photoProduct.value,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(5.dp)
                            //.height(80.dp)
                            .clickable {
                                photoMain.value = photoProduct.value
                            }
                    )
                }

            }
        }
        //shop selection
        val shop= remember {
            mutableStateOf("spar")
        }
        Row(Modifier.padding(30.dp)) {
            Text(text = "Select shop: ")
            ShopDropDown_google(shop)
        }

        //edit barcode details
        var visible= remember { mutableStateOf(false) }
        Box(

            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        )
        {
            Button(onClick = { visible.value = true }) {
                Text(text = "Edit barcode data")
            }
        }
        BottomSheet_google(detectedName,detectedPrice,visible)
    }
}



