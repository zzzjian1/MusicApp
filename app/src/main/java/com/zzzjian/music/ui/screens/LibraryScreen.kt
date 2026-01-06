package com.zzzjian.music.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.zzzjian.music.PlayerViewModel
import com.zzzjian.music.ui.theme.*

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.zzzjian.music.domain.model.Song

import kotlinx.coroutines.launch

import com.zzzjian.music.domain.model.MockData

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(vm: PlayerViewModel) {
    val songs by vm.songs.collectAsState()
    val categories = listOf("全部歌曲", "收藏", "最近播放", "下载")
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgGray50)
            .padding(horizontal = 20.dp)
            .systemBarsPadding() // Handle status bar inset
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
            value = "",
            onValueChange = {},
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
        Spacer(modifier = Modifier.height(8.dp)) // Reduced spacing
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> SongList(songs, vm)
                1 -> SongList(MockData.favoriteSongs, vm)
                2 -> SongList(MockData.recentSongs, vm)
                3 -> SongList(MockData.downloadedSongs, vm)
            }
        }
    }
}

@Composable
fun SongList(songs: List<Song>, vm: PlayerViewModel) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 100.dp) // Space for MiniPlayer
    ) {
        itemsIndexed(songs) { idx, s ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { vm.play(idx) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cover
                AsyncImage(
                    model = s.coverUrl ?: "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=128&h=128&fit=crop", // Cute Cat
                    contentDescription = null,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = s.title,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextGray900,
                        maxLines = 1
                    )
                    Text(
                        text = "${s.artist} · ${s.album}",
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
