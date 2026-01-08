package com.zzzjian.music.data.api

import com.zzzjian.music.data.model.ChatRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Streaming

interface DeepSeekApiService {
    @Streaming
    @POST("chat/completions")
    @Headers("Content-Type: application/json")
    fun streamChat(
        @Header("Authorization") apiKey: String,
        @Body request: ChatRequest
    ): Call<ResponseBody>
}
