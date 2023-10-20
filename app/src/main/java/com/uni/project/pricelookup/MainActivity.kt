package com.uni.project.pricelookup

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.compose.PriceLookupTheme
import com.uni.project.pricelookup.ML.OCR
import com.uni.project.pricelookup.Views.*
import com.uni.project.pricelookup.components.DrawerCard
import com.uni.project.pricelookup.components.SearchWidget
import com.uni.project.pricelookup.components.selecRandomImg
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.roundToInt

//this is professional work :(
class MainActivity : ComponentActivity() {
    companion object{
        fun CleanUpImages(context: Context){
            val preferencesManager = PreferencesManager(context)
            preferencesManager.saveData("photoLoc", "")
            preferencesManager.saveData("product","")
            val jpgFiles = File(preferencesManager.getData("outputDir","")).listFiles { file -> file.name.endsWith(".jpg") }
            jpgFiles?.forEach { file -> file.delete() }

        }
    }

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
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        requestCameraPermission()
        setContent {
            PriceLookupTheme {
                OCR().TEST(LocalContext.current)
                ///HTTP().getAllImagesByItemId(1)
                //saving output dir for saving files
                val context=LocalContext.current
                CleanUpImages(context)
                val preferencesManager = remember { PreferencesManager(context) }
                preferencesManager.saveData("outputDir",getOutputDirectory().absolutePath)

                val navController = rememberNavController()
                val NavigationState= navController.currentBackStackEntryAsState()

                Surface (
                    color = MaterialTheme.colorScheme.background,
                ){
                    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
                    val items= listOf(
                        DrawerMenuItem(name = "Main page",path="MainPage", icon = Icons.Filled.Home),
                        DrawerMenuItem(name = "Main page",path="MainPage", icon = Icons.Filled.Home),
                        DrawerMenuItem(name = "Main page",path="MainPage", icon = Icons.Filled.Home)
                    )
                    val selectedItem = remember { mutableStateOf(items[0]) }
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val scope = rememberCoroutineScope()
                    ModalNavigationDrawer(
                            drawerState = drawerState,
                            drawerContent = {
                                ModalDrawerSheet(
                                    drawerContainerColor = MaterialTheme.colorScheme.background,
                                    drawerContentColor = MaterialTheme.colorScheme.onBackground
                                ) {
//                                    Spacer(Modifier.height(12.dp))
                                    val itemPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                                    val drawerWidth = 260.dp

                                    DrawerCard(drawerWidth)
                                    items.forEach { item ->
                                        NavigationDrawerItem(
                                            colors = NavigationDrawerItemDefaults.colors(
                                                unselectedContainerColor = MaterialTheme.colorScheme.background,
                                                unselectedIconColor = MaterialTheme.colorScheme.primary,
                                                unselectedTextColor = MaterialTheme.colorScheme.primary,

                                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                                selectedTextColor = MaterialTheme.colorScheme.onPrimary
                                            ),
                                            shape = RectangleShape,
                                            icon = { Icon(item.icon, contentDescription = null) },
                                            label = { Text(item.name) },
                                            selected = item == selectedItem.value,
                                            onClick = {
                                                scope.launch { drawerState.close() }
                                                selectedItem.value = item
                                                navController.navigate(item.path)
                                            },
                                            modifier = Modifier
                                                .padding(itemPadding)
                                                .width(drawerWidth)
                                        )
                                    }
                                }
                            },
                            content = {
                                Scaffold(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .nestedScroll(appBarScrollBehavior.nestedScrollConnection),
                                    topBar = {
                                        TopAppBar(
                                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                containerColor = MaterialTheme.colorScheme.primaryContainer,
//                                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                                            ),
                                            title = {
                                                Text("Price checker")
                                            },
                                            navigationIcon = {
                                                IconButton(onClick = {
                                                    scope.launch { drawerState.open() }
                                                }) {
                                                    Icon(
                                                        Icons.Rounded.Menu,
                                                        contentDescription = "Localized description",
                                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                                    )
                                                }
                                            },
                                            actions = {
                                                IconButton(
                                                    onClick = {
                                                        navController.navigate("BarcodeCameraView")
                                                    },
//                                        shape = RoundedCornerShape(size = 20.dp),
                                                    content = {
                                                        Icon(
                                                            Icons.Rounded.CameraAlt,
                                                            contentDescription = "Localized description",
                                                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                                                        )
                                                    }
                                                )
                                            },
                                            scrollBehavior = appBarScrollBehavior
                                        )
                                    },
                                ) {values ->
                                    Column (
                                        modifier = Modifier.padding(values)
                                    ){
                                        val searchText= remember {
                                            mutableStateOf("")
                                        }

                                        //textField default height = 56.dp, textField padding(bottom=5.dp). Ezekre rájön egy alap padding amin nem lehet változtatni még akkor sem ha külön beaállítjuk, h ne legyen (top=3.dp) -> 56+5+3=64.dp
                                        val searchBarHeightDp = 64.dp
                                        val searchBarHeightPx = with(LocalDensity.current) { searchBarHeightDp.roundToPx().toFloat() }

                                        val searchBarOffsetHeightPx = remember { mutableStateOf(0f) }
                                        val searchBarNestedScrollConnection = remember {
                                            object : NestedScrollConnection {
                                                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                                                    val delta = available.y
                                                    val newOffset = searchBarOffsetHeightPx.value + delta
                                                    searchBarOffsetHeightPx.value = newOffset.coerceIn(-searchBarHeightPx, 0f)
                                                    return Offset.Zero
                                                }
                                            }
                                        }
                                        Column(
                                            modifier = Modifier
                                                .nestedScroll(searchBarNestedScrollConnection)
                                                .offset {
                                                    IntOffset(
                                                        x = 0,
                                                        y = searchBarOffsetHeightPx.value.roundToInt()
                                                    )
                                                },
                                        ){
                                            //TODO: search not showing on ItemDetailsScreen
                                            if(NavigationState.value?.destination ==navController.findDestination("SearchScreen") || NavigationState.value?.destination==navController.findDestination("MainPage") || NavigationState.value?.destination==navController.findDestination("ItemDetailsScreen")){
                                                SearchWidget(
                                                    text = searchText.value,
                                                    onTextChange = {
                                                        searchText.value=it;
                                                    },
                                                    onSearchClicked = {
                                                        navController.navigate("SearchScreen/{query}".replace(
                                                            oldValue = "{query}",
                                                            newValue = it
                                                        ))
                                                    },
                                                    onCloseClicked = {
                                                        searchText.value=""
                                                    },
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
                                                composable("ItemEditScreen") {
                                                    ItemEditScreen(navigation = navController)
                                                }

                                                composable("ItemDetailsScreen/{itemId}") {backStackEntry ->
                                                    ItemDetailsScreen(navigation = navController,backStackEntry.arguments?.getString("itemId"))
                                                }
                                                composable("BarcodeCameraView") {backStackEntry ->
                                                    BarcodeCameraView(navigation = navController)
                                                }
                                                composable("ProductCameraView") {backStackEntry ->
                                                    ProductCameraView(navigation = navController)
                                                }
                                            }
                                        }


                                    }
                                }
                            }
                        )

                }

                }

            }
        }
    }