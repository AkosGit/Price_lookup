package com.uni.project.pricelookup.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
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
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.uni.project.pricelookup.PreferencesManager
import io.moyuru.cropify.Cropify
import io.moyuru.cropify.CropifyOption
import io.moyuru.cropify.rememberCropifyState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import scannerboundpath.ScannerBounds
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraCapture(
    onImageCaptured: (Bitmap) -> Unit,
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
        onImageCaptured: (Bitmap) -> Unit,
        onError: (ImageCaptureException) -> Unit,
        IsPhotoTaken: MutableState<String>,
        PhotoPath: MutableState<Bitmap?>,
    ) {

        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback(){
                @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply{
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                    }
                    val buffer: ByteBuffer = image.image!!.getPlanes().get(0).getBuffer()
                    val bytes = ByteArray(buffer.capacity())
                    buffer[bytes]
                    val bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)

                    val rotatedBitmap = Bitmap.createBitmap(
                        bitmapImage,
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )
                    //onImageCaptured(rotatedBitmap)

                    CoroutineScope(Dispatchers.Main).launch {
                        IsPhotoTaken.value="yes"
                        PhotoPath.value=rotatedBitmap
                    }
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e("kilo", "Take photo error:", exception)
                    onError(exception)
                }
            }
        )

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
        val PhotoPath :MutableState<Bitmap?> = remember {
            mutableStateOf(null)
        }

        if (IsPhototaken.value=="no"){
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
            }
        }

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
                    PhotoPath=PhotoPath,
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
            val cropifyState = rememberCropifyState()
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ){
                AsyncImage(
                    model = PhotoPath.value,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Cropify(
//                    option = CropifyOption(
//
//                    ),
                    modifier = Modifier
                        .fillMaxSize()
                    ,
                    bitmap = PhotoPath.value!!.asImageBitmap(),
                    state = cropifyState,
                    onImageCropped = {
                        onImageCaptured(it.asAndroidBitmap())
                    },
                )

                //BUTTONS
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 40.dp)
                ) {
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
                                    contentDescription = "retry photo icon",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        )
                        ElevatedButton(
                            onClick = {
                                cropifyState.crop()
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
                                    contentDescription = "send image icon",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        )
                    }
                }
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