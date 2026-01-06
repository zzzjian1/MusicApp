package com.zzzjian.music.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzzjian.music.PlayerViewModel
import com.zzzjian.music.ui.theme.*

@Composable
fun PlaylistsScreen(vm: PlayerViewModel) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Lists, 1: Settings

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgGray50)
            .padding(horizontal = 20.dp)
            .systemBarsPadding()
    ) {
        // Header
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "播放列表",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray900
            )
            Text(
                text = "新建",
                color = Blue500,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { /* Create */ }
            )
        }

        // Segmented Control
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                PlaylistTab(
                    text = "我的列表",
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f)
                )
                PlaylistTab(
                    text = "相关设置",
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Content
        if (selectedTab == 0) {
            PlaylistListContent()
        } else {
            SettingsContent()
        }
    }
}

@Composable
fun PlaylistTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) White else Color.Transparent)
            .clickable(onClick = onClick)
            .then(if (selected) Modifier.shadow(1.dp, RoundedCornerShape(8.dp)) else Modifier)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) TextGray900 else TextGray500
        )
    }
}

@Composable
fun PlaylistListContent() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Create New
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(White, RoundedCornerShape(16.dp))
                .clickable { }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(BgGray50, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, null, tint = Color.Gray)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("创建新歌单", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Blue500)
                Text("添加你喜欢的音乐", fontSize = 13.sp, color = Color.Gray)
            }
        }

        // Example List Item
        PlaylistItem(title = "我喜欢的音乐", subtitle = "128 首歌曲", color = Color(0xFF8B5CF6))
        PlaylistItem(title = "车载劲爆", subtitle = "45 首歌曲", color = Color(0xFFEC4899))
    }
}

@Composable
fun PlaylistItem(title: String, subtitle: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(16.dp))
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(color, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Icon placeholder
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = TextGray900)
            Text(subtitle, fontSize = 13.sp, color = TextGray500)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
    }
}

@Composable
fun SettingsContent() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SettingsGroup {
            SettingsItem(title = "深色模式", hasSwitch = true)
            Divider(color = BgGray50)
            SettingsItem(title = "音效调节", hasArrow = true)
        }

        SettingsGroup {
            SettingsItem(title = "扫描本地音乐", subtitle = "自动")
            Divider(color = BgGray50)
            SettingsItem(title = "高音质优先", hasSwitch = true)
        }
        
        Text(
            text = "Version 1.0.0 (Build 20240101)",
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Composable
fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
    ) {
        content()
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String? = null,
    hasSwitch: Boolean = false,
    hasArrow: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Icon could go here
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextGray900)
        }
        
        if (hasSwitch) {
            Switch(checked = false, onCheckedChange = {})
        } else if (hasArrow) {
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        } else if (subtitle != null) {
            Text(subtitle, fontSize = 14.sp, color = Color.Gray)
        }
    }
}
