package com.uni.project.pricelookup.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddTask
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.uni.project.pricelookup.ImageSaver
import com.uni.project.pricelookup.ML.OCR
import com.uni.project.pricelookup.PreferencesManager
import io.moyuru.cropify.Cropify
import io.moyuru.cropify.rememberCropifyState
import kotlinx.coroutines.*
import scannerboundpath.ScannerBounds
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun CameraCapture(
    onImageCaptured: (Uri) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {

    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val outputDirectoryPath=preferencesManager.getData("outputDir","")

    val outputDirFile=File(outputDirectoryPath)
    var cameraExecutor = Executors.newSingleThreadExecutor()

    // 1
    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    var cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    // 2
    fun takePhoto(
        filenameFormat: String,
        imageCapture: ImageCapture,
        outputDirectory: File,
        executor: Executor,
        onImageCaptured: (Uri) -> Unit,
        onError: (ImageCaptureException) -> Unit,
        IsPhotoTaken: MutableState<String>,
        PhotoPath: MutableState<Uri>
    ) {

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, executor, object: ImageCapture.OnImageSavedCallback {
            override fun onError(exception: ImageCaptureException) {
                Log.e("kilo", "Take photo error:", exception)
                onError(exception)
            }

            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                CoroutineScope(Dispatchers.Main).launch {
                    //onImageCaptured(savedUri)
                    IsPhotoTaken.value="yes"
                    PhotoPath.value=savedUri

                }

            }
        })
    }
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )

        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    //lifecycleOwner.lifecycle.coroutineScope.coroutineContext.cancel()
    // 3
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {

        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        val IsPhototaken= remember {
            mutableStateOf("no")
        }
        val PhotoPath= remember {
        mutableStateOf(Uri.EMPTY)
        }
        /*
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ){
            Image(
                imageVector = ScannerBounds,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.scale(1.3f)
            )
        }*/

        IconButton(
            modifier = Modifier.padding(bottom = 20.dp),
            onClick = {
                Log.i("kilo", "ON CLICK")
                takePhoto(
                    filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                    imageCapture = imageCapture,
                    outputDirectory = outputDirFile,
                    executor = cameraExecutor,
                    onImageCaptured = onImageCaptured,
                    onError = onError,
                    IsPhotoTaken=IsPhototaken,
                    PhotoPath=PhotoPath
                )
            },
            content = {
                Icon(
                    imageVector = Icons.Sharp.Lens,
                    contentDescription = "Take picture",
                    tint = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(1.dp)
                        .border(1.dp, Color.White, CircleShape)
                )
            }
        )
        if(IsPhototaken.value=="yes"){
            val ocr=OCR()
            val bitmap=ocr.makeBitmapFromPath(PhotoPath.value)
            val state = rememberCropifyState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ){

                /*AsyncImage(
                    model = cuttedBitmap,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )*/

                //#region BUTTONS
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 40.dp)
                ) {
                    Cropify(
                        modifier = Modifier
                            .fillMaxSize()
                        ,
                        bitmap = bitmap.asImageBitmap(),
                        state = state,
                        onImageCropped = {
                            val f=ImageSaver(context).
                            setFileName("myImage.png").
                            setDirectoryName("images").
                            save(it.asAndroidBitmap());
                            val img=
                                onImageCaptured(Uri.parse(f))
                        },
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                    ){
                        ElevatedButton(
                            onClick = {
                                IsPhototaken.value="no"
                                //TODO: delete old file
                            },
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                                disabledContentColor = MaterialTheme.colorScheme.inversePrimary
                            ),
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(20.dp))
                                .size(70.dp)
                            ,
                            content = {
                                Icon(
                                    Icons.Rounded.Autorenew,
                                    contentDescription = "AddTask Icon",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        )
                        ElevatedButton(
                            onClick = {
                                state.crop()
                                lifecycleOwner.lifecycleScope.coroutineContext.cancel()
                                cameraExecutor.shutdownNow()
                            },
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.inversePrimary,
                                disabledContentColor = MaterialTheme.colorScheme.inversePrimary
                            ),
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(20.dp))
                                .size(70.dp)
                            ,
                            content = {
                                Icon(
                                    Icons.Rounded.AddTask,
                                    contentDescription = "AddTask Icon",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        )
                    }
                }
                //#endregion
            }

        }
    }
}


private suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener({
            continuation.resume(cameraProvider.get())
        }, ContextCompat.getMainExecutor(this))
    }
}