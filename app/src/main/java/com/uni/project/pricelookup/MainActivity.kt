package com.uni.project.pricelookup

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.uni.project.pricelookup.Views.*
import com.uni.project.pricelookup.components.CameraCapture
import com.uni.project.pricelookup.components.SearchWidget
import com.uni.project.pricelookup.ui.theme.PriceLookupTheme
import java.io.File


class MainActivity : ComponentActivity() {
    //camera permisson stuff
    private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i("kilo", "Permission granted")
            shouldShowCamera.value = true
        } else {
            Log.i("kilo", "Permission denied")
        }
    }
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("kilo", "Permission previously granted")
                shouldShowCamera.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> Log.i("kilo", "Show camera permissions dialog")

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestCameraPermission()
        setContent {
            PriceLookupTheme {
                //saving output dir for saving files
                val context=LocalContext.current
                val preferencesManager = remember { PreferencesManager(context) }
                preferencesManager.saveData("outputDir",getOutputDirectory().absolutePath)

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

                    Row {
                        val searchText= remember {
                            mutableStateOf("")
                        }
                        Button(onClick = { navController.navigate("BarcodeCameraView") }) {

                        }
                        SearchWidget(
                            text = searchText.value,
                            onTextChange = {
                                searchText.value=it
                                ;
                            },
                            onSearchClicked = {
                                navController.navigate("SearchScreen/{query}".replace(
                                    oldValue = "{query}",
                                    newValue = it
                                ))
                            },
                            onCloseClicked = {
                                searchText.value=""
                            }
                        )

                    }

                    //basically that's the router :)
                    NavHost(navController = navController, startDestination = "MainPage") {
                        composable("MainPage") {
                            MainPage(navigation = navController)

                        }
                        composable("SearchScreen/{query}") {backStackEntry ->
                            SearchScreen(navigation = navController,backStackEntry.arguments?.getString("query"))
                        }
                        composable("ItemEditScreen/{photoLoc}") {backStackEntry ->
                            ItemEditScreen(navigation = navController,backStackEntry.arguments?.getString("photoLoc"))
                        }

                        composable("ItemDetailsScreen/{itemId}") {backStackEntry ->
                            ItemDetailsScreen(navigation = navController,backStackEntry.arguments?.getString("itemId"))
                        }
                        composable("BarcodeCameraView") {backStackEntry ->
                            BarcodeCameraView(navigation = navController)
                        }
                        }
                    }
                }

            }
        }
    }