package com.ravimaurya.urjanext.presentation.scanner

import android.Manifest

import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HdrOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.components.BigButton
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import com.ravimaurya.urjanext.theme.Green40

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.min


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(navController: NavController, mainNavController: NavController) {

    var scannedText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

        if (!cameraPermissionState.status.isGranted) {
            Button(
                onClick = {
                    cameraPermissionState.launchPermissionRequest()
                }
            ) {
                Text(text = "Camera Permission")
            }
        }


        Spacer(modifier = Modifier.height(10.dp))

        if (cameraPermissionState.status.isGranted && scannedText.isEmpty()) {
            CameraPreview() {
                scannedText = it
            }
        }


        AnimatedVisibility(scannedText.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QRCodeScannedText(scannedText)
                Spacer(Modifier.height(16.dp))

                // Continue Payment Button
                BigButton(
                    label = R.string.continue_,
                    onClick = {
                        mainNavController.navigate(NavRoutes.TRANSACTION_SCREEN) {
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))

                // Cancel Scanner
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    onClick = {
                        scannedText = ""
                    },
                    shape = ShapeDefaults.Large
                ) {
                    Text("Cancel")
                }
            }

        }

    }


}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun QRCodeScannedText(text: String = "1234567890 wertyui sdghjk sghjk") {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(Modifier.fillMaxWidth().padding(10.dp)) {
            Text(text)
        }
    }
}


@Composable
fun CameraPreview(
    hasScanned: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    val barCodeVal = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {


        AndroidView(
            factory = { AndroidViewContext ->
                PreviewView(AndroidViewContext).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                    )
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                val cameraSelector: CameraSelector = CameraSelector.Builder()
                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                    .build()
                val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
                val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                    ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({
                    preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                    val barcodeAnalyser = BarCodeAnalyser { barcodes ->
                        barcodes.forEach { barcode ->
                            barcode.rawValue?.let { barcodeValue ->
                                barCodeVal.value = barcodeValue
                                hasScanned(barcodeValue)
//                            Toast.makeText(context, "$barcodeValue Success", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                        }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )




        AnimatedQRScannerOverlay(Modifier.size(200.dp))


    }

}


// Define a modern green color for the scanner
val ScannerGreen = Color(0xFF00E676)
val ScannerLightGreen = Color(0xFF69F0AE)

@Composable
fun AnimatedQRScannerOverlay(
    modifier: Modifier = Modifier,
    cornerColor: Color = ScannerGreen,
    pulseColor: Color = ScannerLightGreen.copy(alpha = 0.5f),
) {
    // Create multiple animations for different effects

    // 1. Pulse animation for scanner effect
    val pulseAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // 2. Corner movement animation for a "scanning" effect
    val cornerAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // 3. Line thickness animation
    val thicknessAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = 2f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)),
            repeatMode = RepeatMode.Reverse
        )
    )

    // 4. Color intensity animation
    val colorIntensity by rememberInfiniteTransition().animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(modifier = modifier.size(250.dp)) {
        val cw = size.width
        val ch = size.height

        // Base line length - 15% of the smallest dimension
        val baseLineLength = min(cw, ch) * 0.15f

        // Animated line length for dynamic effect
        val lineLength = baseLineLength * (0.8f + cornerAnimation * 0.4f)

        // Animated stroke width
        val strokeWidth = thicknessAnimation.dp.toPx()

        // Animated color
        val animatedColor = cornerColor.copy(alpha = colorIntensity)

        // Draw corners
        // Top-Start
        cornerIndicatorLine(
            Offset(0f, 0f),
            Offset(lineLength, 0f),
            color = animatedColor,
            strokeWidth = strokeWidth
        )
        cornerIndicatorLine(
            Offset(0f, 0f),
            Offset(0f, lineLength),
            color = animatedColor,
            strokeWidth = strokeWidth
        )

        // Top-End
        cornerIndicatorLine(
            Offset(cw, 0f),
            Offset(cw - lineLength, 0f),
            color = animatedColor,
            strokeWidth = strokeWidth
        )
        cornerIndicatorLine(
            Offset(cw, 0f),
            Offset(cw, lineLength),
            color = animatedColor,
            strokeWidth = strokeWidth
        )

        // Bottom-Start
        cornerIndicatorLine(
            Offset(0f, ch),
            Offset(lineLength, ch),
            color = animatedColor,
            strokeWidth = strokeWidth
        )
        cornerIndicatorLine(
            Offset(0f, ch),
            Offset(0f, ch - lineLength),
            color = animatedColor,
            strokeWidth = strokeWidth
        )

        // Bottom-End
        cornerIndicatorLine(
            Offset(cw, ch),
            Offset(cw - lineLength, ch),
            color = animatedColor,
            strokeWidth = strokeWidth
        )
        cornerIndicatorLine(
            Offset(cw, ch),
            Offset(cw, ch - lineLength),
            color = animatedColor,
            strokeWidth = strokeWidth
        )

        // Draw scanning effect - horizontal animated line
        val scanLineY = ch * pulseAnimation
        drawLine(
            color = pulseColor,
            start = Offset(0f, scanLineY),
            end = Offset(cw, scanLineY),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Draw subtle grid pattern overlay
        drawScannerGrid(pulseAnimation, pulseColor.copy(alpha = 0.15f))
    }
}

fun DrawScope.cornerIndicatorLine(
    start: Offset,
    end: Offset,
    color: Color = ScannerGreen,
    strokeWidth: Float = 3.dp.toPx(),
) {
    drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}

fun DrawScope.drawScannerGrid(animProgress: Float, color: Color) {
    val cw = size.width
    val ch = size.height
    val gridSize = 20.dp.toPx()
    val lineAlpha = 0.2f * (1 - ((animProgress * 2) % 1))

    // Vertical lines
    for (x in 0..cw.toInt() step gridSize.toInt()) {
        drawLine(
            color = color.copy(alpha = lineAlpha),
            start = Offset(x.toFloat(), 0f),
            end = Offset(x.toFloat(), ch),
            strokeWidth = 0.5.dp.toPx()
        )
    }

    // Horizontal lines
    for (y in 0..ch.toInt() step gridSize.toInt()) {
        drawLine(
            color = color.copy(alpha = lineAlpha),
            start = Offset(0f, y.toFloat()),
            end = Offset(cw, y.toFloat()),
            strokeWidth = 0.5.dp.toPx()
        )
    }
}