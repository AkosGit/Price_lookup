package com.uni.project.pricelookup.ML

import android.R
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File


//Errors out: Failed to open file '/data/data/com.uni.project.pricelookup/code_cache/.overlay/base.apk/assets/dexopt/baseline.prof': No such file or directory
class OCR(ImagePath:String,context:Context)  {
    fun DOTHETHING(ImagePath:String,context:Context, SuccesOCR: (Text:Text)-> Unit){
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = File(ImagePath)
        val bmOptions = BitmapFactory.Options()
        var bitmap = BitmapFactory.decodeFile(image.absolutePath, bmOptions)

        val file= File(ImagePath).exists()
        if(file){
            Toast.makeText(context,"ITSSSSS ALIVEEEEE", Toast.LENGTH_LONG).show()
        }

        val img = InputImage.fromBitmap(bitmap,0)
        //var path: Uri = Uri.parse("android.resource://com.uni.project.pricelookup/" + com.uni.project.pricelookup.R.drawable.chocolate_bar2)
        val result = recognizer.process(img)
            .addOnSuccessListener { visionText ->
                SuccesOCR(visionText)


            }
            .addOnFailureListener { e ->
                Toast.makeText(context,e.message, Toast.LENGTH_LONG).show()
            }
    }


}