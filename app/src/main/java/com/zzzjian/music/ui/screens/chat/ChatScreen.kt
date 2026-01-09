package com.zzzjian.music.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zzzjian.music.ChatViewModel
import com.zzzjian.music.ui.theme.BgGray50
import com.zzzjian.music.ui.theme.Blue500
import com.zzzjian.music.ui.theme.TextGray900

import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.UploadFile
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.ExperimentalComposeUiApi
import java.io.BufferedReader
import java.io.InputStreamReader
import android.speech.RecognizerIntent
import android.content.Intent
import android.app.Activity
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(
    onBack: () -> Unit,
    vm: ChatViewModel = viewModel()
) {
    val messages by vm.messages.collectAsState()
    val streamingMsg by vm.currentStreamingMessage.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val catName by vm.catName.collectAsState()
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    
    var inputText by remember { mutableStateOf("") }
    var showConfig by remember { mutableStateOf(false) }

    // Voice Input Launcher
    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = results?.get(0)
            if (!spokenText.isNullOrBlank()) {
                inputText = if (inputText.isBlank()) spokenText else "$inputText $spokenText"
            }
        }
    }

    // Auto scroll to bottom
    LaunchedEffect(messages.size, streamingMsg) {
        if (messages.isNotEmpty() || streamingMsg.isNotEmpty()) {
            listState.animateScrollToItem((messages.size + (if (streamingMsg.isNotEmpty()) 1 else 0)).coerceAtLeast(0))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(catName, fontWeight = FontWeight.Bold, color = TextGray900)
                        Text("在线中", fontSize = 12.sp, color = Color(0xFF4CAF50)) // Green 500
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = TextGray900)
                    }
                },
                actions = {
                    IconButton(onClick = { showConfig = true }) {
                        Icon(Icons.Default.Settings, "Config", tint = TextGray900)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = TextGray900,
                    navigationIconContentColor = TextGray900,
                    actionIconContentColor = TextGray900
                )
            )
        },
        containerColor = BgGray50,
        contentWindowInsets = WindowInsets.statusBars // Handle top bar only, let content handle bottom
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding) // Applies top padding
        ) {
            // Chat List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(isUser = msg.role == "user", content = msg.content)
                }
                
                if (streamingMsg.isNotEmpty()) {
                    item {
                        ChatBubble(isUser = false, content = streamingMsg, isStreaming = true)
                    }
                }
                
                if (isLoading && streamingMsg.isEmpty()) {
                    item {
                        Text("$catName 正在输入...", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 16.dp))
                    }
                }
            }

            // Input Area
            Surface(
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding() // Only handle nav bar, system adjusts resize for IME
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Voice Button
                    IconButton(
                        onClick = {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                                putExtra(RecognizerIntent.EXTRA_PROMPT, "请说话...")
                            }
                            try {
                                voiceLauncher.launch(intent)
                            } catch (e: Exception) {
                                // Handle exception if no voice recognizer
                            }
                        }
                    ) {
                        Icon(Icons.Default.Mic, "Voice Input", tint = TextGray900)
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                    
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("和$catName 说点什么...", color = Color.Gray) },
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Blue500,
                            unfocusedBorderColor = Color.LightGray,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                vm.sendMessage(inputText)
                                inputText = ""
                                keyboardController?.hide()
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Blue500, CircleShape)
                    ) {
                        Icon(Icons.Default.Send, "Send", tint = Color.White)
                    }
                }
            }
        }
    }

    if (showConfig) {
        ConfigDialog(
            vm = vm,
            onDismiss = { showConfig = false }
        )
    }
}

@Composable
fun ChatBubble(isUser: Boolean, content: String, isStreaming: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0xFFFFC0CB), CircleShape), // Pink
                contentAlignment = Alignment.Center
            ) {
                Text("🐱", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            shape = if (isUser) RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp) 
                   else RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp),
            color = if (isUser) Color(0xFF95EC69) else Color.White, // WeChat Green for user
            shadowElevation = 1.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Box(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = content, 
                    color = Color.Black, // Fix contrast: Always Black on White/Light Green
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

@Composable
fun ConfigDialog(vm: ChatViewModel, onDismiss: () -> Unit) {
    var apiKey by remember { mutableStateOf(vm.apiKey.value) }
    var mbti by remember { mutableStateOf(vm.targetMbti.value) }
    var zodiac by remember { mutableStateOf(vm.targetZodiac.value) }
    var isCatMode by remember { mutableStateOf(vm.isCatMode.value) }
    var musicAwareness by remember { mutableStateOf(vm.musicAwareness.value) }
    
    val context = LocalContext.current
    // File Picker for Chat History
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { inputStream ->
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val content = reader.readText()
                    vm.importChatExamples(content)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val mbtiOptions = listOf(
        "INTJ", "INTP", "ENTJ", "ENTP",
        "INFJ", "INFP", "ENFJ", "ENFP",
        "ISTJ", "ISFJ", "ESTJ", "ESFJ",
        "ISTP", "ISFP", "ESTP", "ESFP"
    )
    
    val zodiacOptions = listOf(
        "白羊座", "金牛座", "双子座", "巨蟹座", 
        "狮子座", "处女座", "天秤座", "天蝎座", 
        "射手座", "摩羯座", "水瓶座", "双鱼座"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("设置", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = { Text("DeepSeek API Key") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                // MBTI Dropdown
                DropdownMenuField(
                    label = "她的 MBTI",
                    options = mbtiOptions,
                    selectedOption = mbti,
                    onOptionSelected = { mbti = it }
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Zodiac Dropdown
                DropdownMenuField(
                    label = "她的星座",
                    options = zodiacOptions,
                    selectedOption = zodiac,
                    onOptionSelected = { zodiac = it }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("聊天模式", modifier = Modifier.weight(1f))
                    Switch(
                        checked = isCatMode,
                        onCheckedChange = { isCatMode = it }
                    )
                }
                Text(
                    text = if (isCatMode) "当前：傲娇猫娘" else "当前：温柔女友",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("音乐感知", modifier = Modifier.weight(1f))
                    Switch(
                        checked = musicAwareness,
                        onCheckedChange = { musicAwareness = it }
                    )
                }
                Text(
                    text = "让 AI 知道你正在听什么歌",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Import Button
                Button(
                    onClick = { launcher.launch("text/plain") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F0F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("导入聊天记录 (学习说话风格)", color = Color.Black)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        vm.setConfig(apiKey, mbti, zodiac, isCatMode)
                        vm.updateMusicAwareness(musicAwareness)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue500)
                ) {
                    Text("保存")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = TextGray900) }, // Label color
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                focusedBorderColor = Blue500,
                unfocusedBorderColor = Color.LightGray,
                focusedTextColor = TextGray900,
                unfocusedTextColor = TextGray900
            ),
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White) // Menu background
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = TextGray900) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
