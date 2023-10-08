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
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)

@Composable
fun ItemEditScreen(navigation: NavController) {
    //setup dragmodal
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showSheet by remember { mutableStateOf(true) }


    //setup shop chooser
    val isExpanded= remember {
        mutableStateOf(false)
    }
    val shop= remember {
        mutableStateOf("spar")
    }

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
                                photoMain.value = photoProduct.value
                            }
                    )
                }

            }
        }
        Row {
            Text(text = "Choose a shop:")
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp)
                ,
                expanded = isExpanded.value,
                onExpandedChange = {
                    isExpanded.value=it
                }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(text = "Spar")
                    },
                    onClick = {
                        shop.value = "Spar"
                        isExpanded.value = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(text = "Lidl")
                    },
                    onClick = {
                        shop.value = "Lidl"
                        isExpanded.value = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(text = "Aldi")
                    },
                    onClick = {
                        shop.value = "Aldi"
                        isExpanded.value = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(text = "Auchan")
                    },
                    onClick = {
                        shop.value = "Auchan"
                        isExpanded.value = false
                    }
                )

            }
        }

        ModalBottomSheet(
            onDismissRequest = {  },
            sheetState = modalBottomSheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            Column {
                Row {
                    Text(text = "Detected product name:")
                    TextField(value = "", onValueChange = {})
                }
                Row {
                    Text(text = "Detected price:")
                    TextField(value = "", onValueChange = {})
                }
            }
        }

    }


}





