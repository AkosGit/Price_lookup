package com.uni.project.pricelookup.Views

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.uni.project.pricelookup.components.CameraCapture

@Composable
fun BarcodeCameraView(navigation: NavController){
    val context= LocalContext.current
    CameraCapture(
        onError = {
            Toast.makeText(context, "Error taking photo", Toast.LENGTH_SHORT).show()
        },
        onImageCaptured = {
            Toast.makeText(context, it.path.toString(), Toast.LENGTH_SHORT).show()
            navigation.navigate("ItemEditScreen/{photoLoc}".replace(
                oldValue = "{photoLoc}",
                newValue = it.path.toString()
            ))
        }
    )
}