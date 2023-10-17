package com.uni.project.pricelookup.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalContext

@Composable
fun NetworkError(){
    val context= LocalContext.current
    Toast.makeText(context,"The network is fucked, sorry :(",Toast.LENGTH_LONG).show()
}


