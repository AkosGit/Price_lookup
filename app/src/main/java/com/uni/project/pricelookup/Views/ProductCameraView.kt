package com.uni.project.pricelookup.Views

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.uni.project.pricelookup.ImageSaver
import com.uni.project.pricelookup.PreferencesManager
import com.uni.project.pricelookup.components.CameraCapture
@Composable
fun ProductCameraView(navigation: NavController){
    val context= LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    CameraCapture(
        onError = {
            Toast.makeText(context, "Error taking photo", Toast.LENGTH_SHORT).show()
        },
        onImageCaptured = {
            val saver = ImageSaver(context)
            val imagePath=saver.
            setFileName("product.png").
            setDirectoryName("images").
            save(it);
            preferencesManager.saveData("product",imagePath)
            navigation.popBackStack()
        }
    )
}