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
import java.util.function.Predicate
import kotlin.math.abs


class OCR()  {
    fun <T> remove(list: MutableList<T>, predicate: Predicate<T>) {
        list.removeIf { x: T -> predicate.test(x) }
    }
    fun TEST(context: Context){
        //val testIMG=com.uni.project.pricelookup.R.drawable.lidl_close_pricetag_other_text spar_big_pricetag
        val testIMG=com.uni.project.pricelookup.R.drawable.lidl_close_pricetag_other_text
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
        //TODO: if ft+price in the same block cant be found search for price block close to ft
        var currentBlock:TextBlock? = null
        val currencyregex = Regex("(\\d*)\\D*FT")
        var AllBlocks= mutableListOf<TextBlock>() //first it will contain all blocks which are not currency related than it will be filtered to search for product
        //search for block that has currency in it rest will go to AllBlocks
        var currency:Int=0
        for (b in blocks){
            val text=b.text.uppercase()
            AllBlocks.add(b)
            if(currencyregex.containsMatchIn(text)){
                if(currency==0){ //grab frist price block because irs scanning from the top to the bottom
                    currentBlock=b
                    currency= currencyregex.find(currentBlock.text.uppercase())!!.groups[1]!!.value.toInt()
                }
                AllBlocks.remove(b) //remove all currency blocks
            }
        }


        //remove blocks that have very different text angle to the currency block
        val ANGLE_DIFFERENCE=5
        val notInSameAngle = Predicate<TextBlock> { b: TextBlock -> b.lines[0].angle-ANGLE_DIFFERENCE >currentBlock!!.lines[0].angle || b.lines[0].angle+ANGLE_DIFFERENCE< currentBlock.lines[0].angle }
        remove(AllBlocks,notInSameAngle)


        //remove blocks that have 3 letters or fewer
        val threeCharactersOrMore=Regex(".*\\D{3}.*")
        val isGramInIt=Regex("\\d{2}\\s?g|\\d{2}\\s?G")
        val NotThreeCharactersOrMore = Predicate<TextBlock> { b: TextBlock -> !threeCharactersOrMore.containsMatchIn(b.text.replace(" ","")) && !isGramInIt.containsMatchIn(b.text) }
        remove(AllBlocks,NotThreeCharactersOrMore)
        //lines that are closest in the img to the currency line, one of them is the title
        val blockDistancesByTop= mutableListOf<Int>()
        val blockDistancesByBottom= mutableListOf<Int>()
        for (b in AllBlocks){
            blockDistancesByTop.add(abs(currentBlock!!.lines[0]!!.boundingBox!!.top-b!!.lines[0]!!.boundingBox!!.top))
            blockDistancesByBottom.add(abs(currentBlock.lines[0].boundingBox!!.bottom-b!!.lines[0].boundingBox!!.bottom))
        }


        //index of min distance top/bottom neighbor
        val minTopIndex=blockDistancesByTop.indexOf(blockDistancesByTop.min())
        blockDistancesByBottom[minTopIndex]=10000000 //avoid choosing same element
        val minBottomIndex=blockDistancesByBottom.indexOf(blockDistancesByBottom.min())

        val minTopText=AllBlocks[minTopIndex].text
        val minBottomText=AllBlocks[minBottomIndex].text


        var productTitleIndex=0
        var productTitle=""
        if( minTopText.length > minBottomText.length){
            productTitleIndex=minTopIndex
        }
        else{
            productTitleIndex=minBottomIndex
        }
        productTitle=AllBlocks[productTitleIndex].text.replace("\n","")


        //if previous block have 3 characters atleast they are part of the title
        // TODO: find better filter for this
        if(productTitleIndex-1>=0){
            if(threeCharactersOrMore.containsMatchIn(AllBlocks[productTitleIndex-1].text)){
                productTitle=productTitle+" "+AllBlocks[productTitleIndex-1].text.replace("\n","")
            }
        }

        //the xxxg text is the last part of title, so every block should be added to the title until gram can be found
        if(!isGramInIt.containsMatchIn(productTitle)){
            var i=productTitleIndex+1
            var found=false
            while (!found && i<AllBlocks.size){
                productTitle=productTitle+" "+AllBlocks[i].text
                if(isGramInIt.containsMatchIn(AllBlocks[i].text)){
                    found=true
                }
                i++
            }
        }

        Toast.makeText(context,productTitle,Toast.LENGTH_LONG).show()

        //line that has similar text to product img text
        //TODO: is good idea?

        }

    }
