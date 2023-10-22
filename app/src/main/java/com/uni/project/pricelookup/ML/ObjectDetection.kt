package com.uni.project.pricelookup.ML

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.util.function.Predicate
import kotlin.math.abs
/*
class ObjectDetection {
    companion object {
        fun builtIn(bitmap: Bitmap, text: Text, context: Context){
            val blocks=text.textBlocks;
            val inObjectBlocks= emptyMap<DetectedObject,MutableList<Text.TextBlock>>().toMutableMap()
            val image = InputImage.fromBitmap(bitmap, 0)
            val options = ObjectDetectorOptions.Builder()
                .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()  // Optional
                .build()
            val objectDetector = ObjectDetection.getClient(options)
        }
        fun customModel(bitmap: Bitmap,text: Text,context: Context,customModel:String){
            val blocks=text.textBlocks;
            val inObjectBlocks= emptyMap<DetectedObject,MutableList<Text.TextBlock>>().toMutableMap()
            val image = InputImage.fromBitmap(bitmap, 0)
            val localModel = LocalModel.Builder()
                //loaded from app/src/Main/assets folder
                .setAssetFilePath(customModel)
                // or .setAbsoluteFilePath(absolute file path to model file)
                // or .setUri(URI to model file)
                .build()
            val customObjectDetectorOptions =
                CustomObjectDetectorOptions.Builder(localModel)
                    .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
                    .enableMultipleObjects()
                    .enableClassification()
                    .setClassificationConfidenceThreshold(0.4f)
                    .setMaxPerObjectLabelCount(4)
                    .build()
            val objectDetector =
                com.google.mlkit.vision.objects.ObjectDetection.getClient(customObjectDetectorOptions)

        }
        fun process(objectDetector:ObjectDetection){
            objectDetector.process(image)
                .addOnSuccessListener { detectedObjects ->
                    for (obj in detectedObjects){
                        for(block in blocks){
                            //maga a bounding box amit a képeken szoktunk látni szines dobozok
                            //bounding boxa van a textblock-nak (block.boundingBox) is meg a detected obj-nek is  (obj.boundingBox)
                            if(obj.boundingBox.contains(block.boundingBox!!.left-TEXTBOX_ERROR_TOLLERANCE, block.boundingBox!!.top-TEXTBOX_ERROR_TOLLERANCE,block.boundingBox!!.right-TEXTBOX_ERROR_TOLLERANCE,block.boundingBox!!.bottom-TEXTBOX_ERROR_TOLLERANCE)) {
                                if(inObjectBlocks[obj]==null){
                                    inObjectBlocks[obj] = mutableListOf()
                                }
                                else{
                                    inObjectBlocks[obj]!!.add(block)
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    // ...
                }
        }
    }
        fun findCorrectObj(result: MutableMap<DetectedObject, MutableList<TextBlock>>): DetectedObject? {
        for (obj in result.keys){
            for(b in result[obj]!!){
                if(b.text.uppercase().contains("FT")){
                    return obj
                }
            }
        }
        return null
    }

}
*/