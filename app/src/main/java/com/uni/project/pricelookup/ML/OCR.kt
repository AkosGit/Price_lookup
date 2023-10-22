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


class OCR()  {
    fun <T> remove(list: MutableList<T>, predicate: Predicate<T>) {
        list.removeIf { x: T -> predicate.test(x) }
    }
    fun TEST(context: Context){
        //val testIMG=com.uni.project.pricelookup.R.drawable.lidl_close_pricetag_other_text spar_big_pricetag
        val testIMG=com.uni.project.pricelookup.R.drawable.spar_some_text
        var path: Uri = Uri.parse("android.resource://com.uni.project.pricelookup/" + testIMG)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val img = InputImage.fromFilePath(context,path)
        val bitmap = BitmapFactory.decodeResource(
            context.resources,
            testIMG
        )
        val result = recognizer.process(img)
            .addOnSuccessListener { visionText ->
                detectObjects(bitmap,visionText,context)
                //ProcessResult(context,visionText,{},bitmap)
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
                    detectObjects(bitmap,visionText,context)
                    //ProcessResult(context,visionText, SuccesOCR,bitmap)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
        }
    }
    fun detectObjects(bitmap: Bitmap,text: Text,context: Context){
        val blocks=text.textBlocks;
        val inObjectBlocks= emptyMap<DetectedObject,MutableList<TextBlock>>().toMutableMap()
        //built in obejct detection
        /*val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()  // Optional
            .build()*/
        val localModel = LocalModel.Builder()
            //loaded from app/src/Main/assets folder
            .setAssetFilePath("mnasnet_1.3_224_1_metadata_1.tflite")
            // or .setAbsoluteFilePath(absolute file path to model file)
            // or .setUri(URI to model file)
            .build()
        //val objectDetector = ObjectDetection.getClient(options)
        val image = InputImage.fromBitmap(bitmap, 0)
        val customObjectDetectorOptions =
            CustomObjectDetectorOptions.Builder(localModel)
                .setDetectorMode(CustomObjectDetectorOptions.SINGLE_IMAGE_MODE)
                .enableMultipleObjects()
                .enableClassification()
                .setClassificationConfidenceThreshold(0.4f)
                .setMaxPerObjectLabelCount(4)
                .build()
        val objectDetector =
            ObjectDetection.getClient(customObjectDetectorOptions)
        val TEXTBOX_ERROR_TOLLERANCE=3
        objectDetector.process(image)
            .addOnSuccessListener { detectedObjects ->
                for (obj in detectedObjects){
                    for(block in blocks){
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
                ProcessResult(context,inObjectBlocks,{},bitmap)
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
    }

    fun findBlockBasedOnDistance(filter:Regex,allBlocks:MutableList<TextBlock>,startPoint:TextBlock,returnSmaller:Boolean): Int {
        //returns the index of the result

        //distance list from startPoint block
        val blockDistancesByTop= mutableListOf<Int>()
        val blockDistancesByBottom= mutableListOf<Int>()
        for (b in allBlocks){
            if(filter.containsMatchIn(b.text)){
                blockDistancesByTop.add(abs(startPoint!!.lines[0]!!.boundingBox!!.top-b!!.lines[0]!!.boundingBox!!.top))
                blockDistancesByBottom.add(abs(startPoint.lines[0].boundingBox!!.bottom-b!!.lines[0].boundingBox!!.bottom))
            }

        }

        //index of min distance top/bottom neighbor
        val minTopIndex=blockDistancesByTop.indexOf(blockDistancesByTop.min())
        blockDistancesByBottom[minTopIndex]=10000000 //avoid choosing same element
        val minBottomIndex=blockDistancesByBottom.indexOf(blockDistancesByBottom.min())

        val minTopText=allBlocks[minTopIndex].text
        val minBottomText=allBlocks[minBottomIndex].text


        var Index=0
        if( minTopText.length > minBottomText.length){
            if(returnSmaller){
                Index=minBottomIndex
            }else{
                Index=minTopIndex
            }

        }
        else{
            if(returnSmaller){
                Index=minTopIndex
            }else{
                Index=minBottomIndex
            }
        }
        return Index
    }
    class RGBColor(
        val red:Int,
        val green:Int,
        val blue:Int
    )
    fun getImageColors(bitmap: Bitmap): List<RGBColor> {
        var colors:MutableList<RGBColor> = mutableListOf()
        var previusColor: RGBColor? =null
        var isSimilar=false
        for (y in 0 until bitmap.height) {
            for (x in 0 until bitmap.width) {
                isSimilar=false
                val color: Int = bitmap.getColor(x, y).toArgb()
                val red: Int = Color.red(color)
                val green: Int = Color.green(color)
                val blue: Int = Color.blue(color)
                //val alpha: Int = Color.alpha(color)
                val colorOBJ=RGBColor(red=red,green=green,blue=blue)
                if(previusColor!=null){
                    val c=isColorSimilar(mutableListOf(colorOBJ),previusColor)
                    if(c==null){
                        colors.add(colorOBJ)
                        isSimilar=true
                    }
                }
                previusColor=colorOBJ
            }
        }
        return colors
    }
    fun reduceColors(colors: List<RGBColor>): MutableList<RGBColor> {
        val reducedColors:MutableList<RGBColor> = colors.toMutableList()
        for (color in colors){
            val isSimilarColorInIt = Predicate<RGBColor> { c: RGBColor ->
                val similar=isColorSimilar(reducedColors,c)
                if(similar==null) {
                    return@Predicate false
                }
                return@Predicate true
            }
            remove(reducedColors,isSimilarColorInIt)
        }
        return reducedColors
    }
    fun isColorSimilar(original: List<RGBColor>,color: RGBColor,MATCH_ERROR:Int=3): RGBColor? {
        for (origColor in original) {
            //if two channels match
            if (color.red== origColor.red && color.green == origColor.green && color.blue==origColor.blue) {
                return color
            }
            if (color.blue == origColor.blue && color.red == origColor.red) {
                if (abs(color.green - origColor.green) < MATCH_ERROR || abs(origColor.green - color.green) < MATCH_ERROR) {
                    return color
                }
            }
            if (color.blue == origColor.blue && color.green == origColor.green) {
                if (abs(color.red - origColor.red) < MATCH_ERROR || abs(origColor.red - color.red) < MATCH_ERROR) {
                    return color
                }
            }
            if (color.red == origColor.red && color.green == origColor.green) {
                if (abs(color.blue - origColor.blue) < MATCH_ERROR || abs(origColor.blue - color.blue) < MATCH_ERROR) {
                    return color
                }
            }
            if(color.red==origColor.red){
                if (abs(color.blue - origColor.blue) < MATCH_ERROR || abs(origColor.blue - color.blue) < MATCH_ERROR) {
                    if (abs(color.green - origColor.green) < MATCH_ERROR || abs(origColor.green - color.green) < MATCH_ERROR) {
                        return color
                    }
                }
            }
            if(color.blue==origColor.blue){
                if (abs(color.red - origColor.red) < MATCH_ERROR || abs(origColor.red - color.red) < MATCH_ERROR) {
                    if (abs(color.green - origColor.green) < MATCH_ERROR || abs(origColor.green - color.green) < MATCH_ERROR) {
                        return color
                    }
                }
            }
            if(color.green==origColor.green){
                if (abs(color.blue - origColor.blue) < MATCH_ERROR || abs(origColor.blue - color.blue) < MATCH_ERROR) {
                    if (abs(color.red - origColor.red) < MATCH_ERROR || abs(origColor.red - color.red) < MATCH_ERROR) {
                        return color
                    }
                }
            }
        }
        return null
    }
    fun compareColorList(original: List<RGBColor>, against: List<RGBColor>): Float {
        val matchList:MutableList<Boolean> = mutableListOf()
        for(color in against){
            val match= isColorSimilar(original,color,12)
            if(match!=null){
                matchList.add(true)
            }
        }
        return matchList.size.toFloat() / against.size.toFloat()
    }
    fun removeBlocksWidthDifferentAngle(allBlocks:MutableList<TextBlock>,referenceBlock:TextBlock,ANGLE_DIFFERENCE:Int=5): MutableList<TextBlock> {
        val notInSameAngle = Predicate<TextBlock> { b: TextBlock -> b.lines[0].angle-ANGLE_DIFFERENCE >referenceBlock!!.lines[0].angle || b.lines[0].angle+ANGLE_DIFFERENCE< referenceBlock.lines[0].angle }
        remove(allBlocks,notInSameAngle)
        return allBlocks
    }
    fun findProductNameEnd(baseName:String, productTitleIndex:Int, allBlocks: MutableList<TextBlock>,filter:Regex): String {
        var name=baseName

        if(!filter.containsMatchIn(name)){
            var i=productTitleIndex+1
            while (i<allBlocks.size){
                name=name+" "+allBlocks[i].text
                if(filter.containsMatchIn(allBlocks[i].text)){
                    return name
                }
                i++
            }
        }
        return name
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
    private fun ProcessResult(
        context: Context,
        //dictanoty deetcted obj as key and text blocks as values
        result: MutableMap<DetectedObject, MutableList<TextBlock>>,
        SuccesOCR: (Text:Text)-> Unit,
        bitmap: Bitmap){
        //TODO: if ft+price in the same block cant be found search for price block close to ft
        var currentBlock:TextBlock? = null
        val currencyregex = Regex("(\\d+)\\D*FT")
        //price tag text blocks
        val blocks=result[findCorrectObj(result)]
        var AllBlocks= mutableListOf<TextBlock>()  //first it will contain all blocks which are not currency related than it will be filtered to search for product
        //search for block that has currency in it rest will go to AllBlocks
        var currency:Int=0
        var isFT=false
        var ftBlock:TextBlock?=null //only used if FT+currency not in one block
        if (blocks != null) {
            for (b in blocks){

                val text=b.text.uppercase()
                AllBlocks.add(b)

                if(text.contains("FT") && !isFT){
                    isFT=true
                    ftBlock=b
                }

                if(currencyregex.containsMatchIn(text)){//matches curreny+FT
                    if(currency==0){ //grab frist price block because it's scanning from the top to the bottom
                        currentBlock=b
                        currency= currencyregex.find(currentBlock.text.uppercase())!!.groups[1]!!.value.toInt()
                    }
                    AllBlocks.remove(b) //remove all currency blocks
                }
            }
        }
        //if no blocks can be found with curreny+FT, search for them seperetly
        if(currentBlock==null && isFT){
            AllBlocks.remove(ftBlock)

            AllBlocks=removeBlocksWidthDifferentAngle(AllBlocks,ftBlock!!)

            //currency should be close to FT block
            val onlyNumberRegex=Regex("^\\d+\$")
            val priceIndex=findBlockBasedOnDistance(onlyNumberRegex,AllBlocks,ftBlock!!,true)
            currency=AllBlocks[priceIndex].text.replace("\n","").toInt()
            currentBlock=AllBlocks[priceIndex]
        }
        else{
            AllBlocks=removeBlocksWidthDifferentAngle(AllBlocks,currentBlock!!)
        }

        /*
        //TODO: separate barcode from other parts of the image
        //1. find all colors on currency block, create a list
        //2. if they match 80% of color of the block belongs price tag
        val currentBlockBitmap: Bitmap = Bitmap.createBitmap(
            bitmap,
            currentBlock.boundingBox!!.bottom,
            currentBlock.boundingBox!!.top,
            currentBlock.boundingBox!!.width(),
            currentBlock.boundingBox!!.height()
        )
        var currentBlockColors=getImageColors(currentBlockBitmap)
        //currentBlockColors=reduceColors(currentBlockColors)
        //allblock get rif of very diffenr color blocks
        val differentColor = Predicate<TextBlock> { b:TextBlock ->
            val blockBitmap: Bitmap = Bitmap.createBitmap(
                bitmap,
                b.boundingBox!!.bottom,
                b.boundingBox!!.top,
                b.boundingBox!!.width(),
                b.boundingBox!!.height()
            )
            var blockColors=getImageColors(blockBitmap)
            //blockColors=reduceColors(blockColors)
            val matchPercentage=compareColorList(currentBlockColors,blockColors)
            if(matchPercentage< 0.6.toFloat()){
                return@Predicate true
            }
            return@Predicate false
        }
        remove(AllBlocks,differentColor)
        */





        //remove blocks that have 3 letters or fewer
        val isGramInIt=Regex("\\d{2}\\s?g|\\d{2}\\s?G")
        val threeCharactersOrMore=Regex(".*\\D{3}.*")
        val NotThreeCharactersOrMore = Predicate<TextBlock> { b: TextBlock -> !threeCharactersOrMore.containsMatchIn(b.text.replace(" ","")) && !isGramInIt.containsMatchIn(b.text) }
        remove(AllBlocks,NotThreeCharactersOrMore)

        //lines that are closest in the img to the currency line, one of them is the title
        val productTitleIndex=findBlockBasedOnDistance(Regex(".*"),AllBlocks,currentBlock!!,false)
        var productTitle=AllBlocks[productTitleIndex].text.replace("\n","")


        //if previous block have 3 characters atleast they are part of the title
        // TODO: find better filter for this
        if(productTitleIndex-1>=0){
            if(threeCharactersOrMore.containsMatchIn(AllBlocks[productTitleIndex-1].text)){
                productTitle=productTitle+" "+AllBlocks[productTitleIndex-1].text.replace("\n","")
            }
        }

        //the xxxg text is the last part of title if it exists, so every block should be added to the title until gram can be found
        productTitle=findProductNameEnd(productTitle,productTitleIndex,AllBlocks,isGramInIt)

        Toast.makeText(context,productTitle,Toast.LENGTH_LONG).show()

        //line that has similar text to product img text
        //TODO: is good idea?

    }

}
