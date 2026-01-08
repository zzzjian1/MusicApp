package com.zzzjian.music.data.model

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    val model: String = "deepseek-chat",
    val messages: List<ChatMessage>,
    val stream: Boolean = true
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val delta: Delta,
    val finish_reason: String?
)

data class Delta(
    val content: String?
)
