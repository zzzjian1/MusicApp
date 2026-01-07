package com.zzzjian.music.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zzzjian.music.PlayerViewModel
import com.zzzjian.music.domain.model.RepeatMode
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer

import com.zzzjian.music.ui.theme.*

import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

import androidx.compose.foundation.interaction.MutableInteractionSource

@Composable
fun PlayerScreen(vm: PlayerViewModel) {
    val state by vm.playback.collectAsState()
    val song = state.currentSong
    val context = LocalContext.current
    var isLiked by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Blurred Background
        if (song != null) {
            AsyncImage(
                model = song.coverUrl ?: "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=800&h=800&fit=crop", // Cute Cat
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.2f)
                    .blur(50.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.White.copy(alpha = 0.3f), Color.White.copy(alpha = 0.8f))
                        )
                    )
            )
        } else {
            Box(modifier = Modifier.fillMaxSize().background(BgGray50))
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding() // Add status bar padding
                .padding(horizontal = 32.dp)
                .padding(top = 20.dp, bottom = 100.dp), // Reduce top padding as we now have status bar padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Drag Indicator
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.5f))
            )
            
            Spacer(modifier = Modifier.height(20.dp)) // Fixed spacer instead of weight

            // Album Art
            Box(
                modifier = Modifier
                    .weight(1.2f) // Increased weight to make album art larger
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
                    modifier = Modifier
                        .aspectRatio(1f) // Maintain square aspect ratio
                        .fillMaxHeight(0.95f) // Slightly reduce max height to avoid touching edges
                ) {
                    AsyncImage(
                        model = song?.coverUrl ?: "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=600&h=600&fit=crop", // Cute Cat
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))

            // Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = song?.title ?: "Not Playing",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextGray900,
                        maxLines = 1
                    )
                    Text(
                        text = song?.artist ?: "Unknown Artist",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextGray500,
                        maxLines = 1
                    )
                }
                IconButton(
                    onClick = {
                        isLiked = !isLiked
                        if (isLiked) {
                            Toast.makeText(context, "哈基米～ 🐱", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Gray200.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null,
                        tint = if (isLiked) Color.Red else TextGray500
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Progress (Dummy for now)
            LinearProgressIndicator(
                progress = 0.35f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = TextGray900,
                trackColor = Gray200
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("1:24", fontSize = 12.sp, color = TextGray500, fontWeight = FontWeight.SemiBold)
                Text("4:03", fontSize = 12.sp, color = TextGray500, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Shuffle */ }) {
                    Icon(Icons.Default.Shuffle, null, tint = TextGray500, modifier = Modifier.size(24.dp))
                }
                
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    IconButton(onClick = { vm.previous() }) {
                        Icon(Icons.Default.SkipPrevious, null, tint = TextGray900, modifier = Modifier.size(36.dp))
                    }
                    
                    IconButton(
                        onClick = { vm.playPause() },
                        modifier = Modifier
                            .size(80.dp)
                            .shadow(elevation = 10.dp, shape = CircleShape)
                            .background(TextGray900, CircleShape)
                    ) {
                        Icon(
                            if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            null,
                            tint = White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    
                    IconButton(onClick = { vm.next() }) {
                        Icon(Icons.Default.SkipNext, null, tint = TextGray900, modifier = Modifier.size(36.dp))
                    }
                }
                
                IconButton(onClick = { /* List */ }) {
                    Icon(Icons.Default.List, null, tint = TextGray500, modifier = Modifier.size(24.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Repeat Segmented Control
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(44.dp) // Force height to not be compressed
                    .background(Color(0xFFE5E7EB), RoundedCornerShape(22.dp))
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RepeatOption(
                        text = "无循环",
                        selected = state.repeatMode == RepeatMode.NONE,
                        onClick = { vm.setRepeat(RepeatMode.NONE) },
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    RepeatOption(
                        text = "单曲",
                        selected = state.repeatMode == RepeatMode.ONE,
                        onClick = { vm.setRepeat(RepeatMode.ONE) },
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    RepeatOption(
                        text = "列表",
                        selected = state.repeatMode == RepeatMode.ALL,
                        onClick = { vm.setRepeat(RepeatMode.ALL) },
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp)) // Fixed spacer instead of weight
        }
    }
}

@Composable
fun RepeatOption(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (selected) Color.White else Color.Transparent
    val textColor = if (selected) Color.Black else Color(0xFF6B7280)
    val elevation = if (selected) 2.dp else 0.dp

    Box(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .shadow(elevation, RoundedCornerShape(18.dp), clip = false)
            .background(backgroundColor, RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            maxLines = 1
        )
    }
}
