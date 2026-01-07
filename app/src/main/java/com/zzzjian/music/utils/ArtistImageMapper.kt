package com.zzzjian.music.utils

import androidx.annotation.DrawableRes
import com.zzzjian.music.R

object ArtistImageMapper {
    /**
     * 歌手名到图片资源的映射表
     * 用户可以在这里添加自己的歌手图片
     * 1. 将图片放入 res/drawable 文件夹 (例如: jay_chou.jpg)
     * 2. 在这里添加映射: "周杰伦" to R.drawable.jay_chou
     */
    private val artistMap = mapOf<String, Int>(
        // 示例：
        // "周杰伦" to R.drawable.jay_chou,
        // "陈奕迅" to R.drawable.eason_chan,
        // "Taylor Swift" to R.drawable.taylor,
        
        // 默认映射一些测试数据 (如果有的话)
        "哈基米" to R.drawable.ic_hajimi_logo,
        "王艳薇" to R.drawable.wangyanwei,
        "周杰伦" to R.drawable.zhoujielun
    )

    /**
     * 获取歌手对应的图片资源ID
     * @param artist 歌手名
     * @return 资源ID，如果没有匹配则返回 null
     */
    @DrawableRes
    fun getArtistImage(artist: String?): Int? {
        if (artist.isNullOrBlank()) return null
        
        // 1. 尝试直接匹配
        artistMap[artist]?.let { return it }
        
        // 2. 尝试忽略大小写匹配
        val lowerArtist = artist.lowercase()
        artistMap.entries.find { it.key.lowercase() == lowerArtist }?.let { return it.value }
        
        // 3. 尝试模糊匹配 (比如 "周杰伦 Jay" -> 匹配 "周杰伦")
        // artistMap.entries.find { artist.contains(it.key, ignoreCase = true) }?.let { return it.value }

        return null
    }
}
