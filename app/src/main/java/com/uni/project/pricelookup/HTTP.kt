package com.uni.project.pricelookup

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.result.Result

class HTTP(
    var baseURL:String = "http://192.168.0.184:3000"
) {
    private fun LOG(msg:String){
        Log.i("SEND_HTTP",msg)
    }

    // TODO - még ezek kellenek az apiban:
    //ItemEditScreen
     fun sendImage(path:String){
         //sends product image to pictures table
         LOG("sending image: "+path)
         val file = FileDataPart.from(path, name = "file")
         Fuel.upload("$baseURL/api/img/1", Method.POST)
             .add(file)
             .responseString { _, _, result ->
                 when (result) {
                     is Result.Success -> {
                         LOG(result.value)
                     }
                     is Result.Failure -> TODO()
                 }
             }
    }
}