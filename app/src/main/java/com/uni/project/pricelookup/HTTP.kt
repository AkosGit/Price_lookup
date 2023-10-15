package com.uni.project.pricelookup

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result
import com.uni.project.pricelookup.models.*

class HTTP(
    var baseURL:String = "http://192.168.0.184:3000"
) {
    private fun LOG(msg:String){
        Log.i("SEND_HTTP",msg)
    }
    //ItemEditScreen
     fun sendImage(path:String,ItemId:Int){
         //sends product image to pictures table
         LOG("sending image: "+path)
         val file = FileDataPart.from(path, name = "file")
         Fuel.upload("$baseURL/api/img/$ItemId", Method.POST)
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
    fun updateProduct(productName: String,shop:String,price:Int,productImagePath: String?){
        //updates or adds product to d
        //returns: OK
        LOG("updateing product: "+productName)
        Fuel.put("$baseURL/api/product/update")
            .jsonBody(NewProduct(productName,shop,price))
            .responseObject<ItemId> { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        if(productImagePath!=null){
                            sendImage(productImagePath,result.value.ItemId)
                        }
                    }
                    is Result.Failure -> {
                        if(result.error.response.statusCode==406){
                            addProduct(productName,shop,price,productImagePath)
                        }
                    }
                }
            }
    }
    private fun addProduct(productName: String,shop:String,price:Int,productImagePath: String?){
        LOG("sending new product: "+productName)
        Fuel.put("$baseURL/api/product/create")
            .jsonBody(NewProduct(productName,shop,price))
            .responseObject<ItemId> { _, _, result ->
                when (result) {
                    is Result.Success -> {

                        if(productImagePath!=null){
                            sendImage(productImagePath,result.value.ItemId)
                        }

                    }
                    is Result.Failure -> {

                    }
                }
            }
    }



    //itemDetails Screen and search screen
    fun getAllImagesByItemId(itemID:Int){
        LOG("reuqest sent for get images")
        Fuel.get("$baseURL/api/img/1")
            .responseObject<Images>{ _, _, result ->
                for (img in result.get()!!.images){
                    LOG(img)
                }
            }
    }
    fun searchProductByname(productName:String){
        //returns: one image(pictures table random), prices(prices table(shop_name, price)), productname, ItemId
        LOG("sending new product: "+productName)
        Fuel.get("$baseURL/api/product/search/byname/", listOf("productName" to productName))
            .responseObject<SearchResult> { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        LOG(result.value.searchResult[0].ProductName)
                    }
                    is Result.Failure -> TODO()
                }
            }
    }


    //Main Screen
    fun getRecommendations(){
        LOG("getting recommendations")
        Fuel.get("$baseURL/api/recommendations")
            .responseObject<Recommendations> { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        LOG(result.value.recommendations[0].ProductName)
                    }
                    is Result.Failure -> TODO()
                }
            }
    }
}