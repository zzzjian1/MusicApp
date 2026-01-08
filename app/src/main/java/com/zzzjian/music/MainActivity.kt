package com.zzzjian.music

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.zzzjian.music.utils.ArtistImageMapper
import com.zzzjian.music.ui.screens.LibraryScreen
import com.zzzjian.music.ui.screens.PlayerScreen
import com.zzzjian.music.ui.screens.PetScreen
import com.zzzjian.music.ui.theme.*

import androidx.compose.foundation.interaction.MutableInteractionSource

import android.content.Intent
import com.zzzjian.music.service.MediaPlaybackService
import com.zzzjian.music.ui.screens.chat.ChatScreen

class MainActivity : ComponentActivity() {
    private val vm: PlayerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ensurePermissions()
        startMusicService()
        setContent {
            MusicTheme {
                App(vm)
            }
        }
    }

    private fun startMusicService() {
        val intent = Intent(this, MediaPlaybackService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun ensurePermissions() {
        val perms = if (Build.VERSION.SDK_INT >= 33) arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO) else arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val need = perms.any { ContextCompat.checkSelfPermission(this, it) != android.content.pm.PackageManager.PERMISSION_GRANTED }
        if (need) ActivityCompat.requestPermissions(this, perms, 1001)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(vm: PlayerViewModel) {
    val nav = rememberNavController()
    LaunchedEffect(Unit) { vm.initialize() }
    
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = { BottomBar(nav) },
        containerColor = BgGray50,
        contentWindowInsets = WindowInsets(0, 0, 0, 0) // We handle insets manually or let background fill
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            NavGraph(nav, vm)
            
            // MiniPlayer Overlay (Only show if not on Player screen and song is playing/selected)
            val state by vm.playback.collectAsState()
            if (currentRoute != "player" && currentRoute != "pet" && currentRoute != "chat" && state.currentSong != null) {
                MiniPlayer(
                    vm = vm,
                    onClick = { nav.navigate("player") },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                )
            }
        }
    }
}

@Composable
fun MiniPlayer(vm: PlayerViewModel, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val state by vm.playback.collectAsState()
    val song = state.currentSong ?: return

    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.9f),
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            // Spin animation logic can be added later
            val artistImage = ArtistImageMapper.getArtistImage(song.artist)
            val fallbackImage = artistImage ?: com.zzzjian.music.R.drawable.ic_hajimi_logo

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.coverUrl)
                    .error(fallbackImage)
                    .fallback(fallbackImage)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextGray900,
                    maxLines = 1
                )
                Text(
                    text = song.artist,
                    fontSize = 12.sp,
                    color = TextGray500,
                    maxLines = 1
                )
            }
            
            IconButton(onClick = { vm.playPause() }) {
                Icon(
                    if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    null,
                    tint = TextGray900
                )
            }
            
            IconButton(onClick = { vm.next() }) {
                Icon(Icons.Default.SkipNext, null, tint = TextGray900)
            }
        }
    }
}

data class TabItem(val route: String, val title: String, val icon: ImageVector)

val tabs = listOf(
    TabItem("library", "乐库", Icons.Default.Home),
    TabItem("player", "播放", Icons.Default.Album),
    TabItem("pet", "猫窝", Icons.Default.Favorite) // Using Favorite icon as placeholder for Pet/Face
)

@Composable
fun BottomBar(nav: NavHostController) {
    val navBackStackEntry by nav.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp), // Match prototype height
        color = Color.White,
        shadowElevation = 0.dp, // Flat as per prototype HTML border-t
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6)) // border-gray-100
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp, top = 8.dp), // Adjust padding for visual balance
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { item ->
                BottomNavItem(
                    item = item,
                    isSelected = currentRoute == item.route,
                    onClick = { nav.navigate(item.route) }
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(item: TabItem, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = if (isSelected) Blue500 else TextGray500,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = item.title,
            fontSize = 10.sp,
            color = if (isSelected) Blue500 else TextGray500,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun NavGraph(nav: NavHostController, vm: PlayerViewModel) {
    NavHost(navController = nav, startDestination = "library") {
        composable("library") { LibraryScreen(vm) }
        composable("player") { PlayerScreen(vm) }
        composable("pet") { 
            PetScreen(
                onChatClick = { nav.navigate("chat") }
            ) 
        }
        composable("chat") { 
            ChatScreen(
                onBack = { nav.popBackStack() }
            ) 
        }
    }
}
