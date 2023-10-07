package com.uni.project.pricelookup.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.uni.project.pricelookup.components.ProductPreviewCard

@Composable
fun PhotoGrid(recommendedItems: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(recommendedItems.count()) {
            ProductPreviewCard()
        }
    }
}