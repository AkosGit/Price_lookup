package com.uni.project.pricelookup.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PhotoGrid(recommendedItems: List<String>,navigation: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(130.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),

        content = {
            items(recommendedItems.count()) {
                ProductPreviewCard(navigation=navigation)

            }
        }
    )
}