package com.uni.project.pricelookup.models

import android.graphics.Bitmap
import java.util.Date

data class Product(

    val id: Int,
    val ProductName:String,
    val createdAt: Date,
    val updatedAt: Date,
    val Pictures:List<SearchImage>,
    val Prices:List<Price>
);
data class SearchImage(
    val id: Int,
    val createdAt: Date,
    val updatedAt: Date,
    val image: String
)
data class Price(
    val id: Int,
    val createdAt: Date,
    val updatedAt: Date,
    val Price: Int,
    val ShopName: String,
    val ItemId: Int
)


data class SearchResult(
    val searchResult: List<Product>
)
data class Recommendations(
    val recommendations: List<Product>
)
