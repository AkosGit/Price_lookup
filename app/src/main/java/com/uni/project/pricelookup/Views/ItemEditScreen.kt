package com.uni.project.pricelookup.Views

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.uni.project.pricelookup.components.BottomSheet_library
import com.uni.project.pricelookup.components.ShopDropDown_google
import com.uni.project.pricelookup.components.ShopDropDown_library
import eu.wewox.modalsheet.ExperimentalSheetApi
import eu.wewox.modalsheet.ModalSheet
import kotlinx.coroutines.*

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    DelicateCoroutinesApi::class, ExperimentalSheetApi::class
)

@Composable
fun ItemEditScreen(navigation: NavController) {
    //setup BottomSheet
    var visible by remember { mutableStateOf(false) }


    //setup shop chooser

    val shop= remember {
        mutableStateOf("spar")
    }

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
                    .height(200.dp)
                    .fillMaxWidth()
            )
        }
        //price tag photo
        Row(
            Modifier.padding(top = 20.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Photo of pricetag:")
            }
            Box(
            )
            {
                AsyncImage(
                    model = photoBarCode,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(5.dp)
                        .height(80.dp)
                        .clickable {
                            photoMain.value = photoBarCode
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
            ) {
                Text(text = "Photo of product:")
            }
            Box(

            )
            {
                if (photoProduct.value == "") {

                    Icon(
                        imageVector = photoPlusSign,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.background, //to be
                        modifier = Modifier
                            .padding(5.dp)
                            .height(80.dp)
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
                            .height(80.dp)
                            .clickable {
                                photoMain.value = photoProduct.value
                            }
                    )
                }

            }
        }


        Row {
            Text(text = "Select shop: ")
            ShopDropDown_google(shop)
        }


        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        )
        {
            Button(onClick = { visible = true }) {
                Text(text = "Edit barcode data")
            }
        }
        ModalSheet(
            visible = visible,
            onVisibleChange = { visible = it },
        ) {
            Box(
                Modifier
                    .height(200.dp),
                contentAlignment = Alignment.Center

            ){
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                    ) {
                        Text(
                            text = "Detected product name:",

                            )
                        TextField(value = detectedName.value, onValueChange = {detectedName.value=it})
                    }
                    Row(

                    ) {
                        Text(
                            text = "Detected price:",
                        )
                        TextField(value = detectedPrice.value.toString(), onValueChange = {detectedPrice.value=it.toInt()})
                    }
                }
            }
        }
    }
}



