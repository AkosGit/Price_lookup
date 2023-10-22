package com.uni.project.pricelookup.ML

import android.graphics.Bitmap
import android.graphics.Color
import java.util.function.Predicate
import kotlin.math.abs

class ColorFilter {
    companion object {
        fun <T> remove(list: MutableList<T>, predicate: Predicate<T>) {
            list.removeIf { x: T -> predicate.test(x) }
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
    }
}