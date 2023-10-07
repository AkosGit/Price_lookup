package com.uni.project.pricelookup.models

import android.graphics.Bitmap

data class Product(
    val productName: String,
    val prices:Map<String,Int>,
    val images: List<Bitmap>
)
