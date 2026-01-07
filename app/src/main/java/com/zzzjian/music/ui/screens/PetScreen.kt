package com.zzzjian.music.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzzjian.music.ui.theme.BgGray50
import com.zzzjian.music.ui.theme.Blue500
import com.zzzjian.music.ui.theme.TextGray900
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.min
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun PetScreen() {
    var touchPosition by remember { mutableStateOf(Offset.Unspecified) }
    var isHappy by remember { mutableStateOf(false) }
    var touchCount by remember { mutableStateOf(0) } // Combo counter
    var comboText by remember { mutableStateOf<String?>(null) } // Combo text popup
    val scope = rememberCoroutineScope()
    
    // Cat Style State
    var catSeed by remember { mutableStateOf(Random.nextLong()) }
    
    // Heart particles
    val hearts = remember { mutableStateListOf<HeartParticle>() }

    // Background color animation
    val bgColor by animateColorAsState(
        if (isHappy) Color(0xFFFFF0F5) else BgGray50,
        animationSpec = tween(1000)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { touchPosition = it },
                    onDragEnd = { touchPosition = Offset.Unspecified },
                    onDragCancel = { touchPosition = Offset.Unspecified },
                    onDrag = { change, _ -> 
                        touchPosition = change.position 
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        touchPosition = offset
                        tryAwaitRelease()
                        touchPosition = Offset.Unspecified
                    },
                    onTap = { offset ->
                        // Touch the cat
                        isHappy = true
                        touchCount++
                        
                        // Combo Logic
                        scope.launch {
                            // Reset combo if no click for 1s
                            delay(1000)
                            touchCount = 0
                            comboText = null
                        }
                        
                        if (touchCount > 1) {
                            comboText = "连击 x$touchCount!"
                            // Special effects for high combo
                        if (touchCount % 5 == 0) {
                            // Big Combo Effect
                            repeat(10) {
                                hearts.add(HeartParticle(
                                    id = Random.nextLong(),
                                    x = offset.x + Random.nextInt(-200, 200),
                                    y = offset.y - 100f + Random.nextInt(-100, 100)
                                ))
                            }
                        }
                        
                        if (touchCount >= 10 && touchCount % 10 == 0) {
                            // 10 combo: Show "Mega Love" text or similar visual feedback
                            comboText = "太棒了! x$touchCount"
                        }
                        
                        if (touchCount >= 20) {
                            // Frenzy mode: Cat shakes!
                             comboText = "疯狂哈基米! x$touchCount"
                        }
                        
                        // Vibration
                        // (Requires Context/Haptics, skip for now or add simple one if available)
                        }

                        scope.launch {
                            repeat(3) {
                                hearts.add(HeartParticle(
                                    id = Random.nextLong(),
                                    x = offset.x,
                                    y = offset.y - 100f
                                ))
                                delay(100)
                            }
                            delay(2000)
                            isHappy = false
                        }
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "哈基米的小窝",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray900,
                modifier = Modifier.padding(top = 20.dp)
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // The Cat
            Box(contentAlignment = Alignment.Center) {
                InteractiveCat(
                    touchPosition = touchPosition,
                    isHappy = isHappy,
                    modifier = Modifier.size(300.dp),
                    seed = catSeed
                )
                
                // Combo Text Popup
                androidx.compose.animation.AnimatedVisibility(
                    visible = comboText != null,
                    enter = androidx.compose.animation.fadeIn() + androidx.compose.animation.scaleIn(),
                    exit = androidx.compose.animation.fadeOut()
                ) {
                    Text(
                        text = comboText ?: "",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF69B4),
                        modifier = Modifier.offset(y = (-150).dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Button(
                    onClick = {
                        catSeed = Random.nextLong()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.height(50.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp), tint = TextGray900)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("换一只猫", color = TextGray900)
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }

        // Render Hearts
        hearts.forEach { particle ->
            HeartAnimation(particle) {
                hearts.remove(particle)
            }
        }
    }
}

data class HeartParticle(val id: Long, val x: Float, val y: Float)

@Composable
fun HeartAnimation(particle: HeartParticle, onFinish: () -> Unit) {
    val alpha = remember { Animatable(1f) }
    val offsetY = remember { Animatable(0f) }
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(particle) {
        launch {
            offsetY.animateTo(-300f, tween(1500, easing = EaseOutQuad))
        }
        launch {
            scale.animateTo(1.5f, tween(1500))
        }
        alpha.animateTo(0f, tween(1500))
        onFinish()
    }

    Icon(
        Icons.Default.Favorite,
        contentDescription = null,
        tint = Color(0xFFFF69B4),
        modifier = Modifier
            .offset(x = with(LocalDensity.current) { particle.x.toDp() - 12.dp }, 
                    y = with(LocalDensity.current) { (particle.y + offsetY.value).toDp() - 12.dp })
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                this.alpha = alpha.value
            }
            .size(24.dp)
    )
}

@Composable
fun InteractiveCat(
    touchPosition: Offset,
    isHappy: Boolean,
    modifier: Modifier = Modifier,
    seed: Long = 0
) {
    val density = LocalDensity.current
    
    // Generate Cat Properties based on Seed
    val random = Random(seed)
    val furColor = Color(
        random.nextInt(50, 200),
        random.nextInt(50, 200),
        random.nextInt(50, 200)
    )
    val eyeColor = Color(
        random.nextInt(100, 255),
        random.nextInt(100, 255),
        random.nextInt(100, 255)
    )
    val bodyScale = random.nextFloat() * 0.2f + 0.9f // 0.9 - 1.1
    val earScale = random.nextFloat() * 0.4f + 0.8f // 0.8 - 1.2
    
    // Blink Animation State
    var isBlinking by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(2000, 5000)) // Random interval 2-5s
            isBlinking = true
            delay(150) // Blink duration
            isBlinking = false
        }
    }
    
    // Cat breathing animation
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breath"
    )
    
    // Ear Wiggle Animation
    val earRotation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = "earWiggle"
    )

    // Tail animation
    val infiniteTransitionTail = rememberInfiniteTransition(label = "tail")
    val tailRotation by infiniteTransitionTail.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tail"
    )

    // Shake Animation (Frenzy Mode)
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(isHappy) {
        if (isHappy) {
             // Small shake on every touch
             shakeOffset.animateTo(5f, tween(50))
             shakeOffset.animateTo(-5f, tween(50))
             shakeOffset.animateTo(0f, tween(50))
        }
    }
    
    Canvas(modifier = modifier.graphicsLayer {
        scaleX = breathScale
        scaleY = breathScale
        translationX = shakeOffset.value
    }) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val headRadius = size.width * 0.35f
        
        // Colors (Use generated ones)
        val bodyColor = furColor
        val earInnerColor = Color(0xFF555555)
        val eyeWhiteColor = Color.White
        val eyePupilColor = Color.Black
        val noseColor = Color(0xFFFF9999)

        // 0. Body (Simple rounded shape at bottom)
        val bodyPath = Path().apply {
            moveTo(centerX - headRadius * 0.9f, centerY + headRadius * 0.5f)
            quadraticBezierTo(
                centerX - headRadius * 1.2f, centerY + headRadius * 1.8f, // Control point
                centerX - headRadius * 0.6f, size.height // Bottom left
            )
            lineTo(centerX + headRadius * 0.6f, size.height) // Bottom right
            quadraticBezierTo(
                centerX + headRadius * 1.2f, centerY + headRadius * 1.8f, // Control point
                centerX + headRadius * 0.9f, centerY + headRadius * 0.5f // Back to head
            )
            close()
        }
        drawPath(bodyPath, bodyColor)

        // 0.5 Tail (Animated)
        withTransform({
            rotate(tailRotation, pivot = Offset(centerX + headRadius * 0.6f, size.height - 20f))
        }) {
            val tailPath = Path().apply {
                moveTo(centerX + headRadius * 0.6f, size.height - 20f)
                quadraticBezierTo(
                    centerX + headRadius * 1.5f, size.height - 50f,
                    centerX + headRadius * 1.2f, size.height - 150f
                )
            }
            drawPath(tailPath, bodyColor, style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round))
        }

        // 1. Ears (Animated)
        // Left Ear
        withTransform({
            rotate(if (isHappy) earRotation * 5f else 0f, pivot = Offset(centerX - headRadius * 0.5f, centerY - headRadius * 0.8f))
        }) {
            val leftEarPath = Path().apply {
                moveTo(centerX - headRadius * 0.8f, centerY - headRadius * 0.5f)
                lineTo(centerX - headRadius * 0.9f, centerY - headRadius * 1.3f)
                lineTo(centerX - headRadius * 0.2f, centerY - headRadius * 0.8f)
                close()
            }
            drawPath(leftEarPath, furColor)
        }
        
        // Right Ear
        withTransform({
            rotate(if (isHappy) -earRotation * 5f else 0f, pivot = Offset(centerX + headRadius * 0.5f, centerY - headRadius * 0.8f))
        }) {
            val rightEarPath = Path().apply {
                moveTo(centerX + headRadius * 0.8f, centerY - headRadius * 0.5f)
                lineTo(centerX + headRadius * 0.9f, centerY - headRadius * 1.3f)
                lineTo(centerX + headRadius * 0.2f, centerY - headRadius * 0.8f)
                close()
            }
            drawPath(rightEarPath, furColor)
        }

        // 2. Head
        drawCircle(
            color = furColor,
            radius = headRadius,
            center = Offset(centerX, centerY)
        )

        // 3. Eyes
        val eyeOffsetX = headRadius * 0.4f
        val eyeOffsetY = -headRadius * 0.1f
        val eyeRadius = headRadius * 0.25f
        
        // Blink Logic
        // We use sin wave to simulate blink occasionally
        // Or simple random? Infinite transition is smoother.
        // Let's make it blink every 3 seconds.
        val time = System.currentTimeMillis()
        // Simple hack: blink every ~3s for 200ms
        // In real app, use a dedicated Animatable for blink state triggered by LaunchedEffect loop
        
        // Calculate Pupil Position (Tracking)
        fun getPupilOffset(eyeCenter: Offset): Offset {
            if (touchPosition == Offset.Unspecified) return Offset.Zero
            
            // Convert touch position to local canvas coordinates if needed
            // But here touchPosition is global screen coords approximately
            // Let's assume touchPosition is relative to screen top-left
            // And this Canvas is centered... 
            // Simple approximation: direction vector
            
            // Since we don't have absolute canvas position easily, we rely on relative movement logic
            // or just use the passed touchPosition which is local to the Box above
            
            val dx = touchPosition.x - eyeCenter.x // This is approximate if Canvas is not at 0,0
            val dy = touchPosition.y - eyeCenter.y // Need to adjust for Canvas position on screen
            
            // Actually, let's just make it look "towards" the touch roughly
            // We can treat touchPosition as relative to the cat's center if we assume cat is centered
            
            val angle = atan2(dy, dx)
            val distance = min(eyeRadius * 0.5f, 15f) // Limit movement
            
            return Offset(cos(angle) * distance, sin(angle) * distance)
        }

        // Left Eye
        val leftEyeCenter = Offset(centerX - eyeOffsetX, centerY + eyeOffsetY)
        
        // Right Eye
        val rightEyeCenter = Offset(centerX + eyeOffsetX, centerY + eyeOffsetY)

        if (isHappy) {
            // Happy Eyes (Curve ^ ^)
            val stroke = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            // Left ^
            drawPath(Path().apply {
                moveTo(leftEyeCenter.x - eyeRadius * 0.7f, leftEyeCenter.y + eyeRadius * 0.2f)
                quadraticBezierTo(leftEyeCenter.x, leftEyeCenter.y - eyeRadius * 0.5f, leftEyeCenter.x + eyeRadius * 0.7f, leftEyeCenter.y + eyeRadius * 0.2f)
            }, furColor, style = stroke)
             // Right ^
            drawPath(Path().apply {
                moveTo(rightEyeCenter.x - eyeRadius * 0.7f, rightEyeCenter.y + eyeRadius * 0.2f)
                quadraticBezierTo(rightEyeCenter.x, rightEyeCenter.y - eyeRadius * 0.5f, rightEyeCenter.x + eyeRadius * 0.7f, rightEyeCenter.y + eyeRadius * 0.2f)
            }, furColor, style = stroke)
        } else if (isBlinking) {
            // Blink Eyes (Line - -)
            val stroke = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            drawLine(furColor, 
                start = Offset(leftEyeCenter.x - eyeRadius, leftEyeCenter.y),
                end = Offset(leftEyeCenter.x + eyeRadius, leftEyeCenter.y),
                strokeWidth = 8.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(furColor, 
                start = Offset(rightEyeCenter.x - eyeRadius, rightEyeCenter.y),
                end = Offset(rightEyeCenter.x + eyeRadius, rightEyeCenter.y),
                strokeWidth = 8.dp.toPx(),
                cap = StrokeCap.Round
            )
        } else {
            // Normal Pupils
            // We need a way to estimate eye center in screen coords for accurate tracking
            // Since we can't easily, let's use a simpler heuristic:
            // If touch is Unspecified, look straight.
            // If touch is defined, calculate relative to center of screen (approx)
            
            val pupilOffset = if (touchPosition != Offset.Unspecified) {
                // Map touch (0..ScreenW) to (-1..1) relative to cat
                // Just use a fixed "look at finger" effect
                // Assuming cat is in middle of screen horizontally
                // We just clamp the values
                val lookX = ((touchPosition.x - centerX) / 10f).coerceIn(-15f, 15f)
                val lookY = ((touchPosition.y - centerY) / 10f).coerceIn(-15f, 15f)
                Offset(lookX, lookY)
            } else {
                Offset.Zero
            }

            drawCircle(eyeColor, eyeRadius * 0.5f, leftEyeCenter + pupilOffset)
            drawCircle(eyeColor, eyeRadius * 0.5f, rightEyeCenter + pupilOffset)
            drawCircle(Color.Black, eyeRadius * 0.2f, leftEyeCenter + pupilOffset)
            drawCircle(Color.Black, eyeRadius * 0.2f, rightEyeCenter + pupilOffset)
        }

        // 4. Nose & Mouth
        val noseCenter = Offset(centerX, centerY + headRadius * 0.3f)
        drawCircle(noseColor, headRadius * 0.08f, noseCenter)

        val mouthPath = Path().apply {
            moveTo(noseCenter.x, noseCenter.y)
            lineTo(noseCenter.x, noseCenter.y + 20f)
            quadraticBezierTo(noseCenter.x - 20f, noseCenter.y + 40f, noseCenter.x - 40f, noseCenter.y + 30f)
            moveTo(noseCenter.x, noseCenter.y + 20f)
            quadraticBezierTo(noseCenter.x + 20f, noseCenter.y + 40f, noseCenter.x + 40f, noseCenter.y + 30f)
        }
        drawPath(mouthPath, furColor, style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round))
        
        // 5. Whiskers
        val whiskerStroke = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        // Left
        drawLine(Color.Gray, Offset(centerX - headRadius * 0.4f, centerY + headRadius * 0.3f), Offset(centerX - headRadius * 1.2f, centerY + headRadius * 0.2f), strokeWidth = 3f)
        drawLine(Color.Gray, Offset(centerX - headRadius * 0.4f, centerY + headRadius * 0.35f), Offset(centerX - headRadius * 1.2f, centerY + headRadius * 0.35f), strokeWidth = 3f)
        drawLine(Color.Gray, Offset(centerX - headRadius * 0.4f, centerY + headRadius * 0.4f), Offset(centerX - headRadius * 1.2f, centerY + headRadius * 0.5f), strokeWidth = 3f)
        
        // Right
        drawLine(Color.Gray, Offset(centerX + headRadius * 0.4f, centerY + headRadius * 0.3f), Offset(centerX + headRadius * 1.2f, centerY + headRadius * 0.2f), strokeWidth = 3f)
        drawLine(Color.Gray, Offset(centerX + headRadius * 0.4f, centerY + headRadius * 0.35f), Offset(centerX + headRadius * 1.2f, centerY + headRadius * 0.35f), strokeWidth = 3f)
        drawLine(Color.Gray, Offset(centerX + headRadius * 0.4f, centerY + headRadius * 0.4f), Offset(centerX + headRadius * 1.2f, centerY + headRadius * 0.5f), strokeWidth = 3f)
    }
}
