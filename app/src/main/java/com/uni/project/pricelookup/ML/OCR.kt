package com.uni.project.pricelookup.ML

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.Line
import com.google.mlkit.vision.text.Text.TextBlock
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import kotlin.math.abs


class OCR()  {
    fun TEST(context: Context){
        //val testIMG=com.uni.project.pricelookup.R.drawable.lidl_close_pricetag_other_text
        val testIMG=com.uni.project.pricelookup.R.drawable.spar_big_pricetag
        var path: Uri = Uri.parse("android.resource://com.uni.project.pricelookup/" + testIMG)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val img = InputImage.fromFilePath(context,path)
        val result = recognizer.process(img)
            .addOnSuccessListener { visionText ->
                ProcessResult(context,visionText, {})
            }
    }
    fun MakeOCR(ImagePath:String,context:Context,SuccesOCR: (Text:Text)-> Unit){
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val image = File(ImagePath)
        val bmOptions = BitmapFactory.Options()
        var bitmap = BitmapFactory.decodeFile(image.absolutePath, bmOptions)

        val file= File(ImagePath).exists()
        if(file) {
            Toast.makeText(context, "ITSSSSS ALIVEEEEE", Toast.LENGTH_LONG).show()


            val img = InputImage.fromBitmap(bitmap, 0)
            val result = recognizer.process(img)
                .addOnSuccessListener { visionText ->
                    ProcessResult(context,visionText, SuccesOCR)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
        }
    }
    private fun ProcessResult(context: Context,result:Text,SuccesOCR: (Text:Text)-> Unit){
        val blocks=result.textBlocks;
        //TODO: check max size bounding box first
        var currentBlock:TextBlock? = null
        val currencyregex = Regex("(\\d*)\\D*FT")
        var AllBlocks= mutableListOf<TextBlock>()
        //search for block that has currency in it
        var currency:Int=0
        for (b in blocks){
            val text=b.text.uppercase()
            AllBlocks.add(b)
            if(currencyregex.containsMatchIn(text)){
                currentBlock=b
                currency= currencyregex.find(currentBlock.text.uppercase())!!.groups[1]!!.value.toInt()
                AllBlocks.remove(currentBlock)
            }
        }
        //remove lines that have very different text angle to the currency line
        val ANGLE_DIFFERENCE=10
        for (b in AllBlocks){
            //lines not in ANGLE_DIFFERENCE
            if(!(b.lines[0].angle-ANGLE_DIFFERENCE <currentBlock!!.lines[0].angle &&  b.lines[0].angle+ANGLE_DIFFERENCE> currentBlock.lines[0].angle)){
                AllBlocks.remove(b)
            }
        }

            //lines that are closest in the img to the currency line, one of them is the title
            val blockDistancesByTop= mutableListOf<Int>()
            val blockDistancesByBottom= mutableListOf<Int>()
            for (b in AllBlocks){
                blockDistancesByTop.add(abs(b!!.lines[0]!!.boundingBox!!.top-currentBlock!!.lines[0]!!.boundingBox!!.top))
                blockDistancesByBottom.add(abs(b.lines[0].boundingBox!!.bottom-currentBlock!!.lines[0].boundingBox!!.bottom))
            }
            val minTop=blockDistancesByTop.indexOf(blockDistancesByTop.min())
            val minBottom=blockDistancesByBottom.indexOf(blockDistancesByBottom.min())
            var productTitle=""
            if(minTop>minBottom){
                productTitle=AllBlocks[blockDistancesByTop.indexOf(minTop)].text
            }
            else{
                productTitle=AllBlocks[blockDistancesByBottom.indexOf(minBottom)].text
            }
            Toast.makeText(context,productTitle,Toast.LENGTH_LONG).show()
            //line that has similar text to product img text


        }

    }
