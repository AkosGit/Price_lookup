package com.uni.project.pricelookup.Views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.google.mlkit.vision.text.Text
import com.uni.project.pricelookup.HTTP
import com.uni.project.pricelookup.R
import com.uni.project.pricelookup.components.NetworkError
import com.uni.project.pricelookup.models.SearchResult

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    DelicateCoroutinesApi::class, ExperimentalSheetApi::class
)
@Composable
fun ItemEditScreen(navigation: NavController) {
    val isLoaded = remember {
        mutableStateOf(false)
    }
    val isNetworkError = remember {
        mutableStateOf(false)
    }
    val isFailed = remember {
        mutableStateOf(false)
    }
    val client=HTTP(LocalContext.current)
    val defaultBigCardPadding = PaddingValues(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 5.dp)

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
//TODO
// basically megcsinálni egy carouselt, ahol a két kép között lehet scrollolni
        Modifier.onFocusChanged {
            val photoLocProduct = preferencesManager.getData("product", "")
            if (photoLocProduct != "") {
                photoProduct.value = photoLocProduct

            }
        }
    ) {
        val smallPhotoModifier= Modifier
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .width(80.dp)
            .height(80.dp)
        val smallPhotoTextModifier= Modifier
            .width(150.dp)
            .height(80.dp)



        //main photo
        Box(modifier = Modifier.fillMaxWidth()){
            Card(modifier = Modifier.padding(defaultBigCardPadding)) {
                AsyncImage(
                    model = photoMain.value,
                    contentDescription = null,

                    modifier = Modifier
                        .padding(20.dp)
                        .height(100.dp)
                        .fillMaxWidth(),
                )
            }
        }

        //small photos
        Box (modifier = Modifier.fillMaxWidth()) {
            Card(modifier = Modifier
                .padding(defaultBigCardPadding)
                .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    ElevatedCard(
                        modifier = Modifier.padding(defaultBigCardPadding)
                    ){
                        Column (
//                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(100.dp)
                                    .clickable {
                                        photoMain.value = photoBarCode
                                    }
                            ){
                                AsyncImage(
                                    model = photoBarCode,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(shape = RoundedCornerShape(10.dp))
                                )
                            }

                            Text(text = "Photo of pricetag")
                        }
                    }

                    ElevatedCard(
                        modifier = Modifier.padding(defaultBigCardPadding)
                    ) {
                        Column(
//                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(100.dp)
                                    .clickable {
                                        photoMain.value = photoBarCode
                                    }
                            ){
                                if (photoProduct.value == "") {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clickable {
                                                navigation.navigate("ProductCameraView")
                                            }
                                    ){
                                        Image(
                                            painterResource(R.mipmap.ic_launcher_foreground),
                                            contentDescription = "appIcon",
                                            modifier = Modifier
                                                .fillMaxSize()
                                        )
                                    }
                                }
                                else {
                                    AsyncImage(
                                        model = photoProduct.value,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(shape = RoundedCornerShape(10.dp))
                                            .clickable {
                                                photoMain.value = photoProduct.value
                                            }
                                    )
                                }
                            }
                            Text(text = "Photo of product")
                        }
                    }
                }
            }
        }

        //shop selection
        val shop= remember {
            mutableStateOf("spar")
        }
        Row(Modifier.padding(30.dp)) {
            ShopDropDown_google(shop)
        }

        fun OnOCRSucces(visionText: Text){
            Toast.makeText(context,visionText.text,Toast.LENGTH_LONG).show()
        }
        fun OnOCRFail(Error: Exception){
            Toast.makeText(context,Error.message,Toast.LENGTH_LONG).show()
        }

        var visible= remember { mutableStateOf(false) }
        Row{
            Button( onClick = {
                //val ocr=OCR()
                //ocr.MakeOCR(photoBarCode,context,{text-> })
                //HTTP().sendImage(photoBarCode)
            }) {
                Text(text = "Process barcode image")
            }

            //edit barcode details
            //TODO
            // ezt a részt egy kicsit renbeszedni kinézetileg
            Button(onClick = { visible.value = true }) {
                Text(text = "Edit barcode data")
            }
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    client.updateProduct(detectedName.value,shop.value,detectedPrice.value,photoProduct.value, {
                        //onSuccess
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
            }) {
                Text(text = "UPLOAD")
            }
        }


//        TODO: ne legyen muliline és enternél menyjen a kövire
        BottomSheet_google(detectedName,detectedPrice,visible)
        if(isNetworkError.value){
            NetworkError()
        }
        if(isFailed.value){
            NetworkError()
        }
    }
}




