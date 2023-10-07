package com.uni.project.pricelookup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uni.project.pricelookup.ui.theme.PriceLookupTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // szia Ákos! :)
        super.onCreate(savedInstanceState)
        setContent {
            PriceLookupTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "MainPage") {
                    composable("MainPage") {
                        MainPage(navigation = navController)
                    }
                    /* composable("ProductDetailsScreen") {
                        ProductDetailsScreen(navigation = navController)
                    }
                    composable("SearchScreen") {
                        SearchScreen(navigation = navController)
                    }
                    composable("EditProductScreen") {
                        EditProductScreen(navigation = navController)
                    }
                    }*/
                }
            }
        }
    }
}