package com.uni.project.pricelookup.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun NetworkError(){
    val context= LocalContext.current
    Toast.makeText(context,"The network is fucked, sorry :(",Toast.LENGTH_LONG).show()
}


