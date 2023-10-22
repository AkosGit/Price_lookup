package com.uni.project.pricelookup.components

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
import com.smarttoolfactory.cropper.ImageCropper
import com.smarttoolfactory.cropper.model.OutlineType
import com.smarttoolfactory.cropper.model.RectCropShape
import com.smarttoolfactory.cropper.settings.CropDefaults
import com.smarttoolfactory.cropper.settings.CropOutlineProperty
import com.uni.project.pricelookup.ImageSaver
import com.uni.project.pricelookup.ML.OCR
import com.uni.project.pricelookup.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import scannerboundpath.ScannerBounds
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


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
            //https://github.com/SmartToolFactory/Compose-Cropper/blob/master/app/src/main/java/com/smarttoolfactory/composecropper/demo/ImageCropDemo.kt
            val cropStyle by remember { mutableStateOf(CropDefaults.style()) }
            val ocr=OCR()
            val bitmap=ocr.makeBitmapFromPath(PhotoPath.value)
            val handleSize: Float = LocalDensity.current.run { 20.dp.toPx() }
            var cropProperties =remember {
                mutableStateOf(
                    CropDefaults.properties(
                        cropOutlineProperty = CropOutlineProperty(
                            OutlineType.Rect,
                            RectCropShape(0, "Rect")
                        ),
                        handleSize = handleSize
                    )
                )
            }


            var crop by remember { mutableStateOf(false) }
            var showDialog by remember { mutableStateOf(false) }
            var isCropping by remember { mutableStateOf(false) }

            ImageCropper(
                modifier = Modifier
                    .fillMaxWidth(),
                imageBitmap = bitmap.asImageBitmap() ,
                contentDescription = "Image Cropper",
                cropStyle = cropStyle,
                cropProperties = cropProperties.value,
                crop = crop,
                onCropStart = {
                    isCropping = true
                },
                onCropSuccess = {
                    isCropping = false
                    crop = false
                    showDialog = true
                    val res=it.asAndroidBitmap()

                    /*val croppedBitmap: Bitmap = Bitmap.createBitmap(
                        res, rectf.left * ratio,
                        rectf.top * ratio, rectf.width() * ratio, rectf.height() * ratio, null, false
                    )*/
                    val f=ImageSaver(context).
                    setFileName("myImage.png").
                    setDirectoryName("images").
                    save(res);
                    val img=
                    onImageCaptured(Uri.parse(f))
                },
            )
            if (isCropping) {
                CircularProgressIndicator()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ){

                /*AsyncImage(
                    model = cuttedBitmap,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )*/

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
                                    contentDescription = "AddTask Icon",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        )
                        ElevatedButton(
                            onClick = {
                                crop=true
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