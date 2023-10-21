package com.uni.project.pricelookup.Views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.uni.project.pricelookup.MainActivity
import com.uni.project.pricelookup.PreferencesManager
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@Composable
fun SettingsScreen(navigation: NavController){
    val context= LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val URL = remember {
        mutableStateOf("")
    }
    if(URL.value==""){
         URL.value=preferencesManager.getData("SERVER_URL","")
    }
    MainActivity.CleanUpImages(context)
    Column {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(top = 10.dp)
        )
        Row(
            //Alignment=Alignment.CenterHorizontally
            modifier = Modifier.padding(10.dp)
        ){
            Text(text = "Server URL:", modifier = Modifier.padding(end = 10.dp))
            TextField(
                singleLine = true,
                value = URL.value ,
                onValueChange = {
                    URL.value=it
                    preferencesManager.saveData("SERVER_URL",it)
                }
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
        )
    }

}