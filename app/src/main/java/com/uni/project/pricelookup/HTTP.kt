package com.uni.project.pricelookup

import android.content.Context
import android.util.Log
import androidx.compose.runtime.remember
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FileDataPart
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result
import com.uni.project.pricelookup.models.*

class HTTP(
    context: Context
) {
    val preferencesManager = PreferencesManager(context)
    //var baseURL:String = "http://192.168.0.235:3000" //akos
    //var baseURL:String = "http://192.168.0.184:3000"   //csabi
    val settings=preferencesManager.getData("SERVER_URL","").replace("http://","").replace(":3000","")
    val default = "prapi.szerver.cc"
    var baseURL:String = "http://${if (settings=="") default else settings}:3000" //from settings

    private fun LOG(msg:String){
        Log.i("SEND_HTTP",msg)
    }
    //ItemEditScreen
     fun sendImage(path:String,ItemId:Int,onSuccess:(res:Int) -> Unit,onFailure: ()-> Unit,onNetworkError: (statusCode: Int)-> Unit){
         //sends product image to pictures table
         LOG("sending image: "+path)
         val file = FileDataPart.from(path, name = "file")
         Fuel.upload("$baseURL/api/img/$ItemId", Method.POST)
             .add(file)
             .responseString { _, _, result ->
                 when (result) {
                     is Result.Success -> {
                         onSuccess.invoke(1)
                     }
                     is Result.Failure -> {
                         if(result.error.response.statusCode==406){
                             onFailure.invoke()
                         }
                         else{
                             onNetworkError.invoke(result.error.response.statusCode)
                         }

                     }
                 }
             }
    }
    fun getProduct(ItemId:String,onSuccess:(res:Product) -> Unit,onFailure: ()-> Unit,onNetworkError: (statusCode: Int)-> Unit){
        Fuel.get("$baseURL/api/product/GetById/$ItemId")
            .responseObject<Product> { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        onSuccess.invoke(result.value)
                    }
                    is Result.Failure -> {
                        if(result.error.response.statusCode==406){
                            onFailure.invoke()
                        }
                        else{
                            onNetworkError.invoke(result.error.response.statusCode)
                        }

                    }
                }
            }
    }
    fun updateProduct(productName: String,shop:String,price:Int,productImagePath: String?,onSuccess:(res:Int) -> Unit,onFailure: ()-> Unit,onNetworkError: (statusCode: Int)-> Unit){
        //updates or adds product to d
        //returns: OK
        LOG("updateing product: "+productName)
        Fuel.put("$baseURL/api/product/update")
            .jsonBody(NewProduct(productName,shop,price))
            .responseObject<ItemId> { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        if(productImagePath!=null){
                            sendImage(productImagePath,result.value.itemId,onSuccess,onFailure,onNetworkError)
                        }
                    }
                    is Result.Failure -> {
                        if(result.error.response.statusCode==406){
                            addProduct(productName,shop,price,productImagePath,onSuccess,onFailure,onNetworkError)
                        }
                        else{
                            onNetworkError.invoke(result.error.response.statusCode)
                        }
                    }
                }
            }
    }
    private fun addProduct(productName: String,shop:String,price:Int,productImagePath: String?,onSuccess:(res:Int) -> Unit,onFailure: ()-> Unit,onNetworkError: (statusCode: Int)-> Unit){
        LOG("sending new product: "+productName)
        Fuel.post("$baseURL/api/product/create")
            .jsonBody(NewProduct(productName,shop,price))
            .responseObject<ItemId> { _, _, result ->
                when (result) {
                    is Result.Success -> {

                        if(productImagePath!=null){
                            sendImage(productImagePath,result.value.itemId,onSuccess,onFailure,onNetworkError)
                        }
                    }
                    is Result.Failure -> {
                        if(result.error.response.statusCode==406){
                            onFailure.invoke()
                        }
                        else{
                            onNetworkError.invoke(result.error.response.statusCode)
                        }
                    }
                }
            }
    }



    //itemDetails Screen and search screen
    fun getAllImagesByItemId(itemID:Int,onSuccess:(res:Images) -> Unit,onFailure: ()-> Unit,onNetworkError: (statusCode: Int)-> Unit){
        LOG("reuqest sent for get images")
        Fuel.get("$baseURL/api/img/1")
            .responseObject<Images>{ _, _, result ->
                when (result) {
                    is Result.Success -> {
                        onSuccess.invoke(result.value)
                    }
                    is Result.Failure -> {
                        if(result.error.response.statusCode==406){
                            onFailure.invoke()
                        }
                        else{
                            onNetworkError.invoke(result.error.response.statusCode)
                        }
                    }
                }
            }
    }
    fun searchProductByname(productName:String, onSuccess:(res:SearchResult) -> Unit,onFailure: ()-> Unit,onNetworkError: (statusCode: Int)-> Unit){
        //returns: one image(pictures table random), prices(prices table(shop_name, price)), productname, ItemId
        LOG("sending new product: "+productName)
        val req=Fuel.get("$baseURL/api/product/search/byname", listOf("productName" to productName))
            .responseObject<SearchResult> { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        onSuccess.invoke(result.value)
                    }
                    is Result.Failure -> {
                        if(result.error.response.statusCode==406){
                            onFailure.invoke()
                        }
                        else{
                            onNetworkError(result.error.response.statusCode)
                        }
                    }
                }
            }
    }


    //Main Screen
    fun getRecommendations(onSuccess:(res:SearchResult) -> Unit,onFailure: ()-> Unit,onNetworkError: (statusCode: Int)-> Unit){
        LOG("getting recommendations")
        Fuel.get("$baseURL/api/recommendations")
            .responseObject<SearchResult> { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        onSuccess.invoke(result.value)
                    }
                    is Result.Failure -> {
                        if(result.error.response.statusCode==406){
                            onFailure.invoke()
                        }
                        else{
                            onNetworkError.invoke(result.error.response.statusCode)
                        }
                    }
                }
            }
    }
}