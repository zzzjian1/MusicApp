package com.zzzjian.music.domain.model

object MockData {
    val favoriteSongs = listOf(
        Song(0, "喵喵之歌", "哈基米", "萌宠专辑", 180000, "", null, "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=128&h=128&fit=crop"),
        Song(1, "午后阳光", "橘猫", "懒洋洋", 200000, "", null, "https://images.unsplash.com/photo-1573865526739-10659fec78a5?w=128&h=128&fit=crop"),
        Song(2, "呼噜声ASMR", "英短", "治愈系", 150000, "", null, "https://images.unsplash.com/photo-1495360019602-e001c276375f?w=128&h=128&fit=crop")
    )

    val recentSongs = listOf(
        Song(3, "追逐激光笔", "小黑", "运动时间", 120000, "", null, "https://images.unsplash.com/photo-1533738363-b7f9aef128ce?w=128&h=128&fit=crop"),
        Song(4, "罐头开盖声", "吃货喵", "美食", 5000, "", null, "https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=128&h=128&fit=crop")
    )

    val downloadedSongs = listOf(
        Song(5, "踩奶进行曲", "奶牛猫", "按摩大师", 240000, "", null, "https://images.unsplash.com/photo-1529778873920-4da4926a7071?w=128&h=128&fit=crop")
    )
}