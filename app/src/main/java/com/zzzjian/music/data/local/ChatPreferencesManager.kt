package com.zzzjian.music.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "chat_settings")

class ChatPreferencesManager(private val context: Context) {
    companion object {
        val API_KEY = stringPreferencesKey("api_key")
        val TARGET_MBTI = stringPreferencesKey("target_mbti")
        val TARGET_ZODIAC = stringPreferencesKey("target_zodiac")
        val IS_CAT_MODE = booleanPreferencesKey("is_cat_mode")
        val CHAT_EXAMPLES = stringPreferencesKey("chat_examples")
    }

    val apiKey: Flow<String> = context.dataStore.data.map { it[API_KEY] ?: "" }
    val targetMbti: Flow<String> = context.dataStore.data.map { it[TARGET_MBTI] ?: "INFP" }
    val targetZodiac: Flow<String> = context.dataStore.data.map { it[TARGET_ZODIAC] ?: "双鱼座" }
    val isCatMode: Flow<Boolean> = context.dataStore.data.map { it[IS_CAT_MODE] ?: true }
    val chatExamples: Flow<String> = context.dataStore.data.map { it[CHAT_EXAMPLES] ?: "" }

    suspend fun saveConfig(key: String, mbti: String, zodiac: String, isCat: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[API_KEY] = key
            prefs[TARGET_MBTI] = mbti
            prefs[TARGET_ZODIAC] = zodiac
            prefs[IS_CAT_MODE] = isCat
        }
    }

    suspend fun saveChatExamples(examples: String) {
        context.dataStore.edit { prefs ->
            prefs[CHAT_EXAMPLES] = examples
        }
    }
}
