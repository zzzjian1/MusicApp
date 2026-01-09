package com.zzzjian.music

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zzzjian.music.data.model.ChatMessage
import com.zzzjian.music.data.repository.ChatRepository
import com.zzzjian.music.data.db.AppDatabase
import com.zzzjian.music.data.db.entity.ChatHistoryEntity
import com.zzzjian.music.data.local.ChatPreferencesManager
import com.zzzjian.music.player.PlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ChatViewModel(app: Application) : AndroidViewModel(app) {
    private val repo by lazy { ChatRepository() }
    private val prefs by lazy { ChatPreferencesManager(app) }
    private val db by lazy { AppDatabase.getDatabase(app) }
    private val chatDao by lazy { db.chatDao() }
    
    // Config
    private val _apiKey = MutableStateFlow("") // Should be stored securely
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()
    
    private val _targetMbti = MutableStateFlow("INFP")
    val targetMbti: StateFlow<String> = _targetMbti.asStateFlow()
    
    private val _targetZodiac = MutableStateFlow("双鱼座")
    val targetZodiac: StateFlow<String> = _targetZodiac.asStateFlow()
    
    private val _isCatMode = MutableStateFlow(true) // true=猫娘, false=正常女生
    val isCatMode: StateFlow<Boolean> = _isCatMode.asStateFlow()
    
    private val _chatExamples = MutableStateFlow("")
    val chatExamples: StateFlow<String> = _chatExamples.asStateFlow()
    
    private val _catName = MutableStateFlow("哈基米")
    val catName: StateFlow<String> = _catName.asStateFlow()
    
    private val _musicAwareness = MutableStateFlow(true)
    val musicAwareness: StateFlow<Boolean> = _musicAwareness.asStateFlow()
    
    // Chat State
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _currentStreamingMessage = MutableStateFlow("")
    val currentStreamingMessage: StateFlow<String> = _currentStreamingMessage.asStateFlow()

    init {
        viewModelScope.launch {
            launch { prefs.apiKey.collectLatest { _apiKey.value = it } }
            launch { prefs.targetMbti.collectLatest { _targetMbti.value = it } }
            launch { prefs.targetZodiac.collectLatest { _targetZodiac.value = it } }
            launch { prefs.isCatMode.collectLatest { _isCatMode.value = it } }
            launch { prefs.chatExamples.collectLatest { _chatExamples.value = it } }
            launch { prefs.catName.collectLatest { _catName.value = it } }
            launch { prefs.musicAwareness.collectLatest { _musicAwareness.value = it } }
            launch { 
                chatDao.getAllMessages().collectLatest { entities ->
                    _messages.value = entities.map { ChatMessage(it.role, it.content) }
                }
            }
        }
    }

    fun setConfig(key: String, mbti: String, zodiac: String, isCat: Boolean) {
        viewModelScope.launch {
            prefs.saveConfig(key, mbti, zodiac, isCat)
        }
    }

    fun importChatExamples(content: String) {
        // Limit to 5000 chars to avoid token limit issues
        val truncated = if (content.length > 5000) content.takeLast(5000) else content
        viewModelScope.launch {
            prefs.saveChatExamples(truncated)
        }
    }
    
    fun updateCatName(name: String) {
        viewModelScope.launch {
            prefs.saveCatName(name)
        }
    }

    fun updateMusicAwareness(enabled: Boolean) {
        viewModelScope.launch {
            prefs.saveMusicAwareness(enabled)
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank() || _apiKey.value.isBlank()) return
        
        viewModelScope.launch {
            // Save user message to DB
            chatDao.insertMessage(ChatHistoryEntity(role = "user", content = content))
            
            // Note: _messages flow will auto-update via Room observer
            // We use current value for API context
            val currentList = _messages.value.toMutableList()
            currentList.add(ChatMessage("user", content))
            
            val targetProfile = "她的性格MBTI: ${_targetMbti.value}, 她的星座: ${_targetZodiac.value}"
            val persona = if (_isCatMode.value) "傲娇猫娘" else "温柔女友"
            
            // Get current playing song info for music awareness (only if enabled)
            val songTitle: String? = if (_musicAwareness.value) {
                val currentSong = PlayerManager.state.value.currentSong
                currentSong?.title
            } else {
                null
            }
            val songArtist: String? = if (_musicAwareness.value) {
                val currentSong = PlayerManager.state.value.currentSong
                currentSong?.artist
            } else {
                null
            }
            
            repo.streamChat(_apiKey.value, currentList, targetProfile, persona, _chatExamples.value, _catName.value, songTitle, songArtist)
                .onStart { 
                    _isLoading.value = true 
                    _currentStreamingMessage.value = ""
                }
                .onCompletion { 
                    _isLoading.value = false
                    // Save assistant message to DB
                    if (_currentStreamingMessage.value.isNotEmpty()) {
                        chatDao.insertMessage(ChatHistoryEntity(role = "assistant", content = _currentStreamingMessage.value))
                        _currentStreamingMessage.value = ""
                    }
                }
                .catch { e ->
                    _currentStreamingMessage.value = "喵？出错了：${e.message}"
                }
                .collect { chunk ->
                    _currentStreamingMessage.value += chunk
                }
        }
    }
}
