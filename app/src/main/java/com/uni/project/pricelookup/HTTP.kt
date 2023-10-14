package com.uni.project.pricelookup

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.result.Result

class HTTP {
     fun SendImage(path:String){
        val testIMG=com.uni.project.pricelookup.R.drawable.spar_big_pricetag
        //val testPath= Uri.parse("android.resource://com.uni.project.pricelookup/" + testIMG)
        //val stringPath=testPath.toFile().absolutePath
        val file = FileDataPart.from(path, name = "image")

        Fuel.upload("http://localhost:3000/1", Method.POST)
            .add(file)
            .responseString { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        Log.i("tagg",result.value)
                    }
                    is Result.Failure -> TODO()
                }
            }
    }
}