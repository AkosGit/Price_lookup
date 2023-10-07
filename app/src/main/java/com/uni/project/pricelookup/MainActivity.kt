package com.uni.project.pricelookup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uni.project.pricelookup.Views.MainPage
import com.uni.project.pricelookup.ui.theme.PriceLookupTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PriceLookupTheme {
                val navController = rememberNavController()
                Column {
                    TopAppBar(
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            titleContentColor = MaterialTheme.colorScheme.primary,
                        ),
                        title = {
                            Text("Price checker")
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                            // TODO: will need to implement: openNavDrawer()
                            }) {
                                Icon(
                                    Icons.Rounded.Menu,
                                    contentDescription = "Localized description"
                                )
                            }
                        },
                        )

                    //basically that's the router :)
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
}