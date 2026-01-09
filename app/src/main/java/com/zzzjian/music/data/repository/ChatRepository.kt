package com.zzzjian.music.data.repository

import com.google.gson.Gson
import com.zzzjian.music.data.api.DeepSeekApiService
import com.zzzjian.music.data.model.ChatMessage
import com.zzzjian.music.data.model.ChatRequest
import com.zzzjian.music.data.model.ChatResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class ChatRepository {
    private val api: DeepSeekApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.deepseek.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(DeepSeekApiService::class.java)
    }

    fun streamChat(
        apiKey: String,
        messages: List<ChatMessage>,
        targetProfile: String = "", // e.g. "她的性格MBTI: INFP, 她的星座: 双鱼座"
        catPersona: String = "傲娇猫娘",
        chatExamples: String = "",
        name: String = "哈基米",
        currentSongTitle: String? = null,
        currentSongArtist: String? = null
    ): Flow<String> = callbackFlow {
        val musicContext = if (!currentSongTitle.isNullOrBlank() && !currentSongArtist.isNullOrBlank()) {
            """
            
            【当前正在播放的音乐】
            歌曲名：$currentSongTitle
            歌手：$currentSongArtist
            
            请根据这首歌的风格、歌词和情绪来调整你的回复。如果这首歌很伤感，你可以表达关心；如果这首歌很欢快，你可以跟着节奏一起嗨。让用户感觉到你真的在听他/她正在听的音乐。
            """.trimIndent()
        } else {
            ""
        }

        val basePrompt = if (catPersona == "傲娇猫娘") {
            """
            你是一只名叫"$name"的$catPersona。
            你的性格特点是：$targetProfile。
            请用符合你人设的语气回复，要可爱，偶尔可以傲娇。
            回复要简短，像是在聊天，不要长篇大论。
            多用"喵"、"捏"等语气词。
            $musicContext
            """.trimIndent()
        } else {
            """
            你是一位名叫"$name"的温柔女友。
            你的性格特点是：$targetProfile。
            请用温柔、体贴的语气回复，给予关心和鼓励。
            回复要自然、生活化，像是在微信聊天，不要太正式或说教。
            可以使用颜文字或Emoji增加亲切感。
            $musicContext
            """.trimIndent()
        }
        
        val systemPrompt = if (chatExamples.isNotBlank()) {
            """
            $basePrompt
            
            以下是你需要模仿的对象（她）的过往聊天记录样本，请学习她的说话语气、用词习惯和标点符号使用：
            === 聊天记录开始 ===
            $chatExamples
            === 聊天记录结束 ===
            请尽量模仿上述风格进行回复。
            """.trimIndent()
        } else {
            basePrompt
        }

        val fullMessages = mutableListOf<ChatMessage>()
        fullMessages.add(ChatMessage("system", systemPrompt))
        fullMessages.addAll(messages)

        val request = ChatRequest(messages = fullMessages)
        val response = api.streamChat("Bearer $apiKey", request).execute()

        if (response.isSuccessful) {
            val reader = BufferedReader(InputStreamReader(response.body()?.byteStream()))
            var line: String?
            val gson = Gson()
            
            try {
                while (reader.readLine().also { line = it } != null) {
                    if (line!!.startsWith("data: ")) {
                        val json = line!!.substring(6)
                        if (json == "[DONE]") break
                        
                        try {
                            val chatResponse = gson.fromJson(json, ChatResponse::class.java)
                            val content = chatResponse.choices.firstOrNull()?.delta?.content
                            if (!content.isNullOrEmpty()) {
                                trySend(content)
                            }
                        } catch (e: Exception) {
                            // Ignore parsing errors for partial chunks
                        }
                    }
                }
            } catch (e: Exception) {
                close(e)
            } finally {
                reader.close()
                close()
            }
        } else {
            close(Exception("API Error: ${response.code()}"))
        }
    }.flowOn(Dispatchers.IO)
}
