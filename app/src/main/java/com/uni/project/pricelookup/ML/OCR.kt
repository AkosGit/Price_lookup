package com.uni.project.pricelookup.ML

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

//Errors out: Failed to open file '/data/data/com.uni.project.pricelookup/code_cache/.overlay/base.apk/assets/dexopt/baseline.prof': No such file or directory
class OCR(ImagePath:String,context:Context)  {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    //val image = File(ImagePath)
    //val bmOptions = BitmapFactory.Options()
    //var bitmap = BitmapFactory.decodeFile(image.absolutePath, bmOptions)
    val img = InputImage.fromFilePath(context, Uri.parse(ImagePath))
    val result = recognizer.process(img)
        .addOnSuccessListener { visionText ->
            //SuccesOCR(visionText)
            Toast.makeText(context,visionText.text, Toast.LENGTH_LONG).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
        }

}