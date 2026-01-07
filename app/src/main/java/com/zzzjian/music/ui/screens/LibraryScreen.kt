package com.zzzjian.music.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.animation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zzzjian.music.PlayerViewModel
import com.zzzjian.music.ui.theme.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.zzzjian.music.domain.model.Song
import com.zzzjian.music.domain.model.MockData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(vm: PlayerViewModel) {
    val songs by vm.songs.collectAsState()
    val categories = listOf("全部歌曲", "收藏", "最近播放", "下载")
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    fun filterSongs(list: List<Song>): List<Song> {
        if (searchQuery.isBlank()) return list
        return list.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
            it.artist.contains(searchQuery, ignoreCase = true) ||
            it.album.contains(searchQuery, ignoreCase = true)
        }
    }
    
    Scaffold(
        snackbarHost = { 
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    modifier = Modifier.padding(bottom = 90.dp) // Avoid overlapping with BottomBar/MiniPlayer
                )
            } 
        },
        containerColor = BgGray50,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .systemBarsPadding()
        ) {
            // Header
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "哈基米",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextGray900
                    )
                    IconButton(
                        onClick = { /* TODO */ },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFF3F4F6), CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Blue500)
                    }
                }
                Text(
                    text = "哈基米一下，烦恼全放下",
                    fontSize = 13.sp,
                    color = Blue500,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
            // Search Bar
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("搜索歌曲、艺术家、专辑", color = TextGray500, fontSize = 15.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Categories
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories.size) { idx ->
                    val isSelected = pagerState.currentPage == idx
                    val bg = if (isSelected) Blue500 else White
                    val text = if (isSelected) White else Color(0xFF4B5563)
                    val border = if (isSelected) Color.Transparent else BorderGray100
                    
                    Surface(
                        color = bg,
                        shape = RoundedCornerShape(50),
                        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, border) else null,
                        modifier = Modifier
                            .height(36.dp)
                            .clickable { 
                                scope.launch { pagerState.animateScrollToPage(idx) }
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 20.dp)) {
                            Text(text = categories[idx], color = text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Pager Content
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) { page ->
                // Only page 0 uses real data for now
                val currentList = when (page) {
                    0 -> songs
                    1 -> MockData.favoriteSongs
                    2 -> MockData.recentSongs
                    3 -> MockData.downloadedSongs
                    else -> emptyList()
                }
                
                SongList(
                    songs = filterSongs(currentList),
                    vm = vm,
                    isDeletable = page == 0,
                    onDelete = { song, index ->
                        vm.deleteSong(song)
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "已删除: ${song.title}",
                                actionLabel = "撤销",
                                duration = SnackbarDuration.Short
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                vm.restoreSong(song, index)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SongList(
    songs: List<Song>,
    vm: PlayerViewModel,
    isDeletable: Boolean = false,
    onDelete: (Song, Int) -> Unit = { _, _ -> }
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 100.dp) // Space for MiniPlayer
    ) {
        itemsIndexed(songs, key = { _, item -> item.id }) { idx, s ->
            HoldToDeleteItem(
                song = s,
                index = idx,
                onClick = { vm.play(songs, idx) },
                onDelete = { onDelete(s, idx) },
                enabled = isDeletable
            )
        }
    }
}

@Composable
fun HoldToDeleteItem(
    song: Song,
    index: Int,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    enabled: Boolean
) {
    val haptic = LocalHapticFeedback.current
    var isHolding by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    val interactionSource = remember { MutableInteractionSource() }
    
    // Shake Animation
    val shakeOffset = remember { Animatable(0f) }
    
    LaunchedEffect(isHolding) {
        if (isHolding && enabled) {
            // Start charging
            launch {
                progress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                )
                // If we reach here, delete!
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onDelete()
                // Reset immediately for UI stability (though item will be removed)
                progress.snapTo(0f)
                isHolding = false
            }
            // Start shaking
            launch {
                while (isHolding) {
                    val intensity = progress.value * 10f
                    shakeOffset.animateTo(intensity, tween(50))
                    shakeOffset.animateTo(-intensity, tween(50))
                }
                shakeOffset.snapTo(0f)
            }
        } else {
            // Reset if released
            progress.snapTo(0f)
            shakeOffset.snapTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .graphicsLayer { translationX = shakeOffset.value }
            .background(
                // Dynamic background based on progress
                if (progress.value > 0f) Color.Red.copy(alpha = 0.1f + progress.value * 0.2f) else Color.Transparent
            )
    ) {
        // Progress Indicator Background
        if (progress.value > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.value)
                    .matchParentSize()
                    .background(Color.Red.copy(alpha = 0.2f))
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .indication(interactionSource, LocalIndication.current)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { onClick() },
                        onLongPress = {
                            isHolding = true
                        },
                        onPress = { offset ->
                            val press = androidx.compose.foundation.interaction.PressInteraction.Press(offset)
                            interactionSource.emit(press)
                            
                            try {
                                kotlinx.coroutines.coroutineScope {
                                    val holdJob = launch {
                                        delay(200)
                                        isHolding = true
                                    }
                                    try {
                                        val released = tryAwaitRelease()
                                        if (released) {
                                            interactionSource.emit(androidx.compose.foundation.interaction.PressInteraction.Release(press))
                                        } else {
                                            interactionSource.emit(androidx.compose.foundation.interaction.PressInteraction.Cancel(press))
                                        }
                                    } finally {
                                        holdJob.cancel()
                                        isHolding = false
                                    }
                                }
                            } finally {
                                isHolding = false
                            }
                        }
                    )
                }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cover
            Box {
                AsyncImage(
                    model = song.coverUrl ?: "https://c-ssl.duitang.com/uploads/blog/202111/11/20211111134414_00463.jpg", // Cute Cat
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .scale(1f - progress.value * 0.2f) // Shrink effect
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                if (progress.value > 0.1f) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Red.copy(alpha = progress.value * 0.5f))
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (progress.value > 0.8f) "松手取消" else song.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (progress.value > 0.5f) Color.Red else TextGray900,
                    maxLines = 1
                )
                Text(
                    text = "${song.artist} · ${song.album}",
                    fontSize = 14.sp,
                    color = TextGray500,
                    maxLines = 1
                )
            }
            
            IconButton(onClick = { /* Menu */ }) {
                Icon(Icons.Default.MoreHoriz, null, tint = Color.LightGray)
            }
        }
    }
}

@Composable
fun PlaceholderPage(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🐱", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = text, color = TextGray500, fontSize = 16.sp)
        }
    }
}
