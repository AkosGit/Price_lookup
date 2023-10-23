package com.uni.project.pricelookup.ML

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.BitmapPainter
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.util.function.Predicate
import kotlin.math.abs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.sp

fun Modifier.boundingBox(
    color: Color = Color.Magenta,
    strokeWidth: Float = 3f,
    size: Size,
    topleft:Offset,
    text: String
) = drawWithCache {
    val stroke = Stroke(strokeWidth)
    val paint = Paint()
    paint.setTextSize(35f)
    paint.color = android.graphics.Color.GREEN
    onDrawWithContent {
        this.drawContext.canvas.nativeCanvas.drawText(
            "${text}",
            topleft.x, topleft.y, paint
        )
        drawContent()
        drawRect(
            topLeft = topleft,
            color = Color.Magenta,
            size = size,
            style = stroke
        )
    }
}


class OCR()  {
    fun <T> remove(list: MutableList<T>, predicate: Predicate<T>) {
        list.removeIf { x: T -> predicate.test(x) }
    }

    fun makeBitmapFromPath(ImagePath: Uri): Bitmap {
        val image = ImagePath.path?.let { File(it) }
        val bmOptions = BitmapFactory.Options()
        var bitmap = BitmapFactory.decodeFile(image!!.absolutePath, bmOptions)
        return bitmap
    }
    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun TEST(context: Context,testIMG:Int){
        val blocks:MutableState<Text?> = remember {
            mutableStateOf(null)
        }

        var path: Uri = Uri.parse("android.resource://com.uni.project.pricelookup/" + testIMG)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val img = InputImage.fromFilePath(context,path)
        var bitmap = BitmapFactory.decodeResource(
            context.resources,
            testIMG
        )
        /*val res=bitmap.copy(Bitmap.Config.ARGB_8888,true).asImageBitmap()
        val canvas= Canvas(res)
        val rect= Rect(0.toFloat(), bitmap.height.toFloat(), bitmap.width.toFloat(), 0.toFloat())
        canvas.drawImage(bitmap, rect, rect, null)*/
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.surface)
//        ){
//            if(blocks.value != null){
//                val mod=Modifier
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                ){
//                    Image(
//                        painter = BitmapPainter(bitmap.asImageBitmap()),
//                        contentDescription = ""
//                    )
//                    for (block in blocks.value!!.textBlocks){
//                        val box = block.boundingBox
//
//                        val w = box!!.width()
//                        val h = box.height()
//                        val size = Size(w.toFloat(), h.toFloat())
//                        val topLeft = Offset(box.left.toFloat(),box.top.toFloat())
//
//                        Box(modifier = mod.boundingBox(Color.Magenta, 5f, size, topLeft, block.text))
//                    }
//                }
//            }
//        }




        //bitmap=cutInputImg(bitmap)

        val result = recognizer.process(img)
            .addOnSuccessListener { visionText ->
                blocks.value = visionText
                ProcessResult(context,visionText.textBlocks,{product: String, price:Int ->},bitmap)
            }
    }
    fun MakeOCR(ImagePath:String,context:Context,SuccesOCR: (product:String, price:Int) ->Unit,onFailure: (msg:String) -> Unit) {
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val file = File(ImagePath).exists()
            if (file) {
                //Toast.makeText(context, "ITSSSSS ALIVEEEEE!!! Run for your life! ITS GONNA ORK YOU!!!", Toast.LENGTH_LONG) .show()
                val bitmap = makeBitmapFromPath(Uri.parse(ImagePath))

                val img = InputImage.fromBitmap(bitmap, 0)
                val result = recognizer.process(img)
                    .addOnSuccessListener { visionText ->
                        try {
                            ProcessResult(context, visionText.textBlocks, SuccesOCR, bitmap)
                        }
                        catch (err:Exception){
                            onFailure.invoke(err.message.toString())
                        }
                    }
                    .addOnFailureListener { e ->
                        onFailure.invoke(e.message.toString())
                    }
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
            else{
                blockDistancesByTop.add(9999999)
                blockDistancesByBottom.add(9999999)
            }
        }

        //index of min distance top/bottom neighbor
        val minTopIndex=blockDistancesByTop.indexOf(blockDistancesByTop.min())
        blockDistancesByBottom[minTopIndex]=9999999 //avoid choosing same element
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


    private fun ProcessResult(
        context: Context,
        //dictanoty deetcted obj as key and text blocks as values
        blocks: MutableList<TextBlock>?,
        SuccesOCR: (product:String, price:Int) -> Unit,
        bitmap: Bitmap
    ){
        //TODO: if ft+price in the same block cant be found search for price block close to ft
        var currentBlock:TextBlock? = null
        val currencyregex = Regex("(\\d+)\\D*FT")
        //price tag text blocks
        var AllBlocks:MutableList<TextBlock> = mutableListOf()  //first it will contain all blocks which are not currency related than it will be filtered to search for product
        //search for block that has currency in it rest will go to AllBlocks
        for(b in blocks!!){
            AllBlocks.add(b)
        }
        var currency: Int? = null
        var isFT=false
        var ftBlock:TextBlock?=null //only used if FT+currency not in one block

        val biggestBoxByArea =blocks!!.maxBy { b: TextBlock -> (b.boundingBox!!.height())*b.boundingBox!!.width() }
        currency = biggestBoxByArea.text.uppercase().replace("FT","").replace(" ","").replace(".","").toIntOrNull()

        if(currency == null){
            for (b in blocks){
                val text=b.text.uppercase()

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

            //if no blocks can be found with curreny+FT, search for them seperetly
            if(currentBlock==null && isFT){
                AllBlocks.remove(ftBlock)

                AllBlocks=removeBlocksWidthDifferentAngle(AllBlocks,ftBlock!!)

                //currency should be close to FT block
                val onlyNumberRegex=Regex("^\\d+\$")
                val priceIndex=findBlockBasedOnDistance(onlyNumberRegex,AllBlocks,ftBlock!!,true)
                currency=AllBlocks[priceIndex].text.replace("\n","").toInt()
                currentBlock=AllBlocks[priceIndex]
                AllBlocks=removeBlocksWidthDifferentAngle(AllBlocks,currentBlock!!)
            }
        }
        else{
            currentBlock=biggestBoxByArea
            AllBlocks=removeBlocksWidthDifferentAngle(AllBlocks,currentBlock!!)
        }
        if (currentBlock == null){
            throw Exception("Price cannot be found! Please take a new picture!")
        }
        AllBlocks.remove(currentBlock)

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
        /* val NotThreeCharactersOrMore = Predicate<TextBlock> { b: TextBlock -> !threeCharactersOrMore.containsMatchIn(b.text.replace(" ","")) && !isGramInIt.containsMatchIn(b.text) }
      remove(AllBlocks,NotThreeCharactersOrMore)*/
        // TODO: discount filter regex???
        //lines that are closest in the img to the currency line, one of them is the title
        val titleRegex = "[A-Z].*[a-zA-Z]{3}"
        val productTitleIndex=findBlockBasedOnDistance(Regex("${titleRegex}"),AllBlocks,currentBlock!!,false)
        var productTitle=AllBlocks[productTitleIndex].text.replace("\n","")


        //if previous block have 3 characters atleast they are part of the title
        if(productTitleIndex-1>=0){
            if(Regex("${titleRegex}").containsMatchIn(AllBlocks[productTitleIndex-1].text)){
                productTitle=AllBlocks[productTitleIndex-1].text.replace("\n"," ")+" "+productTitle
            }
        }
        if(productTitleIndex-2>=0){
            if(Regex("${titleRegex}").containsMatchIn(AllBlocks[productTitleIndex-2].text)){
                productTitle=AllBlocks[productTitleIndex-2].text.replace("\n"," ")+" "+productTitle
            }
        }

        //the xxxg text is the last part of title if it exists, so every block should be added to the title until gram can be found
        productTitle=findProductNameEnd(productTitle,productTitleIndex,AllBlocks,isGramInIt)

        //remove diveder part
        if ("|" in productTitle) {
            productTitle = productTitle.split("|")[1]
            if (productTitle[0] == ' ') {
                productTitle = productTitle.substring(1, productTitle.length)
            }
        }

        //
        if("- " in productTitle || " -" in productTitle){
            productTitle=productTitle.replace("- ","-").replace(" -","-")
        }

        //Toast.makeText(context,productTitle,Toast.LENGTH_LONG).show()
        SuccesOCR.invoke(productTitle,currency!!)
        //line that has similar text to product img text
        //TODO: is good idea?

    }

}
